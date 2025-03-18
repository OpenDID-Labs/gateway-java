package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.CancelStatusEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.utils.GatewayKeyVaultUtil;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidRequestException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainRequestCancelBodyFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterface;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterfaceV2;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.service.CheckRepeatService;
import io.opendid.web2gateway.service.HomeChainKeyManageService;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import io.opendid.web2gateway.service.OracleNonceService;
import java.util.Date;
import java.util.LinkedHashMap;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.REQUEST_CANCEL + Web2Method.BEAN_SUFFIX)
@MethodPrivate
public class MethodOracleRequestCancel implements Web2Method {

  @Autowired
  private OracleNonceService oracleNonceService;

  @Autowired
  private OdOracleContractEventlogMapper odOracleContractEventlogMapper;
  @Autowired
  private OracleContractEventLogService eventLogService;
  @Autowired
  private CheckRepeatService checkRepeatService;

  @Autowired
  private HomeChainKeyManageService homeChainKeyManageService;

  private Logger logger = LoggerFactory.getLogger(MethodOracleRequestCancel.class);
  @Value("${oracle.callBack.url}")
  private String callBackUrl;
  @Override
  public OracleRequestCancelRespDTO process(JsonRpc2Request request)
      throws Exception {

    if (request.getParams() == null) {
      return new OracleRequestCancelRespDTO();
    }

    HomeChainRequestCancelBodyInterfaceV2 cancelBodyHandler =
            HomeChainRequestCancelBodyFactory.createCancelBodyHandler();

    LinkedHashMap params = request.getParams();
    String oracleRequestTxHash = MapUtils.getString(params, "oracleRequestTxHash");
    String requestId = MapUtils.getString(params, "requestId");

    try {

      boolean executable = checkRepeatService.checkRepeatCancel(requestId);

      if (!executable) {
        logger.info("The transaction is being cancelled");
        throw new JsonRpc2ServerErrorException(-32000, null, "The current status of the transaction cannot be cancelled", null);
      }

      checkRepeatService.setCache(requestId,"cancel");

      OdOracleContractEventlog odOracleContractEventlog = null;
      if (StringUtils.isNotBlank(oracleRequestTxHash)) {
        logger.info("MethodOracleRequestCancel process oracleRequestTxHash={}",
            oracleRequestTxHash);
        odOracleContractEventlog = odOracleContractEventlogMapper.selectByRequestOracleHash(
            oracleRequestTxHash);
      } else {
        if (StringUtils.isNotBlank(requestId)) {
          logger.info("MethodOracleRequestCancel process requestId={}", requestId);

          odOracleContractEventlog =
              odOracleContractEventlogMapper.selectByRequestId(requestId);

        }
      }

      checkRequestStatus(odOracleContractEventlog);

//      OracleChainCancelMsgDTO oracleChainCancelMsgDTO = new OracleChainCancelMsgDTO();
//      oracleChainCancelMsgDTO.setRequestId(odOracleContractEventlog.getRequestId());
//      oracleChainCancelMsgDTO.setJobId(odOracleContractEventlog.getJobId());
//      oracleChainCancelMsgDTO.setVnCode(odOracleContractEventlog.getVnCode());
//      oracleChainCancelMsgDTO.setKeyCode(odOracleContractEventlog.getKeyCode());

      //1. wrap body
      GatewayHomechainKeyManage gatewayHomechainKeyManage =
              homeChainKeyManageService.selectByVnCode(odOracleContractEventlog.getVnCode());
      OracleWrapCancelBodyDTO cancelBodyDTO=new OracleWrapCancelBodyDTO();
      cancelBodyDTO.setRequestId(odOracleContractEventlog.getRequestId());
      cancelBodyDTO.setJobId(odOracleContractEventlog.getJobId());
      cancelBodyDTO.setWalletPublicKey(gatewayHomechainKeyManage.getWalletPublicKey());
      cancelBodyDTO.setVnCode(odOracleContractEventlog.getVnCode());
      OracleWrapCancelBodyResDTO oracleWrapCancelBodyResDTO =
              cancelBodyHandler.wrapBody(cancelBodyDTO);


      // 2. signature
      ChainKeyDTO chainKeyByKeyCode = GatewayKeyVaultUtil.getChainKeyByKeyCode(odOracleContractEventlog.getKeyCode());
      String walletPrivateKey=chainKeyByKeyCode.getPrivateKey();

      OracleCancelSignatureDTO signatureDTO=new OracleCancelSignatureDTO();
      signatureDTO.setVnWalletPrivateKey(walletPrivateKey);
      signatureDTO.setWrapData(oracleWrapCancelBodyResDTO.getWrapData());

      OracleCancelSignatureResDTO signatureResDTO = cancelBodyHandler.signature(signatureDTO);

      //3. send transaction


      //3.1 metaData
      String  homeChainPublicKey = gatewayHomechainKeyManage.getWalletPublicKey();
      OracleRequestCancelMetaDataDTO metaData = new OracleRequestCancelMetaDataDTO();
      metaData.setPublicKey(homeChainPublicKey);
      metaData.setCallbackUrl(callBackUrl);
      metaData.setRequestId(odOracleContractEventlog.getRequestId());
      metaData.setJobId(odOracleContractEventlog.getJobId());
      metaData.setVnCode(odOracleContractEventlog.getVnCode());

      OracleCancelSendDTO sendDTO=new OracleCancelSendDTO();
      sendDTO.setSignatureResDTO(signatureResDTO);
      sendDTO.setMetaData(metaData);
      sendDTO.setRequestClientBody(request);

      JsonRpc2Response respResult = cancelBodyHandler.sendMessage(sendDTO);
      JSONObject respResultJson = JSONObject.parseObject(
          JSONObject.toJSONString(respResult.getResult()));


      logger.info("Request cancel opendid return value : {}", respResultJson.toJSONString());

      OdOracleContractEventlogWithBLOBs contractEventlog = new OdOracleContractEventlogWithBLOBs();
      contractEventlog.setLogId(odOracleContractEventlog.getLogId());
      contractEventlog.setCancelCreateDate(new Date());
      contractEventlog.setCancelRequestBody(JSONObject.toJSONString(request));

      if (respResultJson.get("failReason") != null && !"".equals(
          respResultJson.get("failReason"))) {

        contractEventlog.setCancelErrorMsg(respResultJson.getString("failReason"));
        contractEventlog.setCancelStatus(CancelStatusEnum.FAIL.getCode());
      } else {

        contractEventlog.setCancelOracleHash(respResultJson.getString("oracleRequestTxHash"));
        respResultJson.put("cancelOracleHash", respResultJson.getString("oracleRequestTxHash"));
        contractEventlog.setCancelStatus(CancelStatusEnum.PENDING.getCode());
      }

      // update cancel transaction log
      eventLogService.updateByPrimaryKeySelective(contractEventlog);

      if (respResultJson.get("failReason") != null && !"".equals(
          respResultJson.get("failReason"))) {

        OracleRequestErrorDataDTO oracleRequestErrorDataDTO = new OracleRequestErrorDataDTO();
        oracleRequestErrorDataDTO.setRequestId(odOracleContractEventlog.getRequestId());

        String errorData = JSON.toJSONString(oracleRequestErrorDataDTO);
        logger.info("MethodOracleRequestCancel process error data={}", errorData);

        throw new JsonRpc2ServerErrorException(
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getCode(),
            "",
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getMessage(),
            oracleRequestErrorDataDTO);
      }

      return respResultJson.toJavaObject(OracleRequestCancelRespDTO.class);

    } catch (JsonRpc2ServerErrorException jsonRpc2ServerErrorException) {

      throw jsonRpc2ServerErrorException;

    } finally {
      // Clear Cache
      checkRepeatService.removeCache(requestId);
    }

  }

  private static void checkRequestStatus(OdOracleContractEventlog odOracleContractEventlog)
      throws Exception {


    if (odOracleContractEventlog == null) {
      throw new JsonRpc2ServerErrorException(
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getCode(),
              MDC.get(LogTraceIdConstant.TRACE_ID),
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getMessage(),
              "Transaction does not exist");
    }

    if (odOracleContractEventlog.getCancelStatus() == CancelStatusEnum.PENDING.getCode()) {
      throw new JsonRpc2InvalidRequestException(
          MDC.get(LogTraceIdConstant.TRACE_ID),
          "Transaction in progress： requestId is " +
              odOracleContractEventlog.getRequestId()
      );
    }
    if (odOracleContractEventlog.getCancelStatus() == CancelStatusEnum.SUCCESSFULLY.getCode()) {
      throw new JsonRpc2InvalidRequestException(
          MDC.get(LogTraceIdConstant.TRACE_ID),
          "This transaction has been canceled： cancelTransactionHash is " +
              odOracleContractEventlog.getCancelOracleHash()
      );
    }


  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    return null;
  }


}
