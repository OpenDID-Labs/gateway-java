package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.status.CallbackProcessStatus;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.utils.ECIESUtil;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.UpdateEventLogDTO;
import io.opendid.web2gateway.model.dto.vnclient.OracleCallBackRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.Web2MethodProcess;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component(Web2MethodName.ORACLE_CALL_BACK+ Web2MethodProcess.BEAN_SUFFIX)
public class MethodOracleCallBack implements Web2MethodProcess {

  private Logger logger = LoggerFactory.getLogger(MethodOracleCallBack.class);

  @Value("${service-key.privatekey}")
  private String localPrivateKey;

  @Autowired
  private OracleContractEventLogService oracleContractEventLogService;

  @Override
  public Object process(JsonRpc2Request request) {

    logger.info("MethodOracleCallBack receive params: {}", JSONObject.toJSONString(request));

    OracleCallBackRespDTO callBackRespDTO = new OracleCallBackRespDTO();
    try {
      LinkedHashMap params = request.getParams();
      String requestIdEncryptStr = params.get("requestId").toString();
      String txHashEncryptStr = params.get("oracleFulfillTxHash").toString();
      String dataEncryptStr = params.get("data").toString();

      // decrypt params
      String requestId = ECIESUtil.hexDecrypt(localPrivateKey, requestIdEncryptStr);
      String responseBody = ECIESUtil.hexDecrypt(localPrivateKey, dataEncryptStr);
      String callbackOracleHash = ECIESUtil.hexDecrypt(localPrivateKey, txHashEncryptStr);

      UpdateEventLogDTO updateEventLogDTO = new UpdateEventLogDTO();
      updateEventLogDTO.setProcessStatus(ProcessStatusEnum.PROCESSED.getCode());
      updateEventLogDTO.setRequestId(requestId);
      updateEventLogDTO.setResponseBody(responseBody);
      updateEventLogDTO.setCallbackOracleHash(callbackOracleHash);
      oracleContractEventLogService.updateEventLogByRequestId(updateEventLogDTO);
      callBackRespDTO.setSuccessfully(CallbackProcessStatus.SUCCESSFUL.getCode());
    } catch (Exception e) {
      logger.error("MethodOracleCallBack process error:", e);
      callBackRespDTO.setSuccessfully(CallbackProcessStatus.FAILED.getCode());
      callBackRespDTO.setCallbackMessage(e.getMessage());
    }
    return JSONObject.toJSONString(callBackRespDTO);
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    return null;
  }
}
