package io.opendid.web2gateway.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.ECDSAUtils;
import io.opendid.web2gateway.common.vnclient.VnGlobalMapping;
import io.opendid.web2gateway.contextfilter.api.SystemContextHolder;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InternalErrorException;
import io.opendid.web2gateway.model.dto.vnclient.VnCallbackSignPayloadDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.method.Web2CallbackMethod;
import io.opendid.web2gateway.oracleweb2process.methodfactory.Web2MethodProcessFactory;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.service.CallbackParamsDecryptService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;


/**
*  EncryptDataProcess
* @author Dong-Jianguo
* @Date: 2025/1/9
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Aspect
@Component
public class EncryptDataProcessAop {

  private Logger logger = LoggerFactory.getLogger(EncryptDataProcessAop.class);

  @Autowired
  private CallbackParamsDecryptService callbackParamsDecryptService;
  @Autowired
  private VnGlobalMapping vnGlobalMapping;
  @Autowired
  private OdOracleContractEventlogMapper odOracleContractEventlogMapper;


  @Pointcut("@annotation(io.opendid.web2gateway.security.checkaspect.PublicApiSecurityCheck)")
  private void encryptDataProcess() {

  }


  @Before("encryptDataProcess()")
  public void beforeMethod(JoinPoint joinPoint) throws Exception {

    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

    logger.info("EncryptDataProcess start");

    // 1.Get request data
    JsonRpc2Request jsonRpc2Request =
    JSONObject.parseObject(joinPoint.getArgs()[0].toString(), JsonRpc2Request.class);

    String encryptData = jsonRpc2Request.getParams().get("encryptData").toString();
    logger.info("EncryptDataProcess response={}",encryptData);

    // 2.Decrypting Data
    String decryptData = callbackParamsDecryptService.decryptRequestCallbackParams(encryptData);
    String method = jsonRpc2Request.getMethod();
    Web2CallbackMethod methodProcess = (Web2CallbackMethod) Web2MethodProcessFactory.getMethodProcess(method);
    VnCallbackSignPayloadDTO signPayloadDTO = methodProcess.buildSignPayload(decryptData);
    // 3.Get event log info by requestId
    logger.info("EncryptDataProcess requestId={}",signPayloadDTO.getRequestId());
    OdOracleContractEventlog odOracleContractEventlog =
        odOracleContractEventlogMapper.selectByRequestId(signPayloadDTO.getRequestId());

    if (odOracleContractEventlog == null){
      // 3.1 Event log info not exist
      logger.info("EncryptDataProcess odOracleContractEventlog is null");
      throw new JsonRpc2InternalErrorException(traceId,"requestId not exist!");
    }

    // 4.Get vn public key info by jobId
    logger.info("EncryptDataProcess jobId={}",odOracleContractEventlog.getJobId());
    VngatewayRouteInfo vnRouteByJobId = vnGlobalMapping.getVnRouteByJobId(odOracleContractEventlog.getJobId());

    String vnPublicKey = vnRouteByJobId.getVnPublicKey();
    if (vnPublicKey.startsWith("0x")){
      vnPublicKey = vnPublicKey.replace("0x","");
    }

    // 5.Verify sign data
    String signData = signPayloadDTO.getVnSignData();
    boolean verify = ECDSAUtils.verify(signPayloadDTO.getSignStr(), new BigInteger(vnPublicKey,16).toString(), signData);

    if (!verify){
      // 5.1 Failed the signature verification
      logger.info("EncryptDataProcess verify sign fail");
      throw new JsonRpc2InternalErrorException(traceId,"Verify sign fail!");
    }

    // 6.Build result data
    jsonRpc2Request.setParams(signPayloadDTO.getMethodJsonRpc2Params());

    SystemContextHolder.getContext().setCallBackDataDecrypted(JSON.toJSONString(jsonRpc2Request));
  }


}
