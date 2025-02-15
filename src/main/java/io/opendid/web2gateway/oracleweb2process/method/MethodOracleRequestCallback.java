package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.status.CallbackProcessStatus;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.utils.ECIESUtil;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.OracleCallBackResultDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateEventLogDTO;
import io.opendid.web2gateway.model.dto.vnclient.OracleCallBackRespDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnCallbackSignPayloadDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodPublic;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component(Web2MethodName.ORACLE_CALL_BACK+ Web2Method.BEAN_SUFFIX)
@MethodPublic
public class MethodOracleRequestCallback implements Web2CallbackMethod {

  private Logger logger = LoggerFactory.getLogger(MethodOracleRequestCallback.class);


  @Autowired
  private OracleContractEventLogService oracleContractEventLogService;

  @Override
  public Object process(JsonRpc2Request request) {

    logger.info("MethodOracleCallBack receive params: {}", JSONObject.toJSONString(request));

    OracleCallBackRespDTO callBackRespDTO = new OracleCallBackRespDTO();
    try {
      LinkedHashMap params = request.getParams();
      String requestId = params.get("requestId").toString();
      String callbackOracleHash = params.get("oracleFulfillTxHash").toString();
      String aptosVersion = params.get("aptosVersion").toString();
      String responseBody = params.get("data").toString();

      UpdateEventLogDTO updateEventLogDTO = new UpdateEventLogDTO();
      updateEventLogDTO.setProcessStatus(ProcessStatusEnum.PROCESSED.getCode());
      updateEventLogDTO.setRequestId(requestId);
      updateEventLogDTO.setResponseBody(responseBody);
      updateEventLogDTO.setCallbackOracleHash(callbackOracleHash);
      updateEventLogDTO.setRequestAptosVersion(aptosVersion);
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

  @Override
  public VnCallbackSignPayloadDTO buildSignPayload(String decryptStr) {

    OracleCallBackResultDTO oracleCallBackResultDTO =
            JSONObject.parseObject(decryptStr, OracleCallBackResultDTO.class);

    VnCallbackSignPayloadDTO vnCallbackSignPayloadDTO = new VnCallbackSignPayloadDTO();
    vnCallbackSignPayloadDTO.setRequestId(oracleCallBackResultDTO.getRequestId());
    vnCallbackSignPayloadDTO.setVnSignData(oracleCallBackResultDTO.getSignData());

    String signStr = oracleCallBackResultDTO.getRequestId()
            + oracleCallBackResultDTO.getOracleFulfillTxHash()
            + oracleCallBackResultDTO.getData()
            + oracleCallBackResultDTO.getStatus();

    vnCallbackSignPayloadDTO.setSignStr(signStr);

    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();

    linkedHashMap.put("requestId",oracleCallBackResultDTO.getRequestId());
    linkedHashMap.put("oracleFulfillTxHash",oracleCallBackResultDTO.getOracleFulfillTxHash());
    linkedHashMap.put("data",oracleCallBackResultDTO.getData());

    vnCallbackSignPayloadDTO.setMethodJsonRpc2Params(linkedHashMap);

    return vnCallbackSignPayloadDTO;
  }
}
