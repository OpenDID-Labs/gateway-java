package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.status.CallbackProcessStatus;
import io.opendid.web2gateway.common.enums.status.CancelStatusEnum;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.OracleCallBackResultDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleCallbackCancelResultDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateCancelEventLogDTO;
import io.opendid.web2gateway.model.dto.vnclient.OracleRequetCancelCallbackResDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnCallbackSignPayloadDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.security.checkaspect.MethodPublic;
import io.opendid.web2gateway.service.OracleContractCancelEventLogService;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.ORACLE_REQUEST_CANCEL_CALLBACK + Web2Method.BEAN_SUFFIX)
@MethodPublic
public class MethodOracleRequestCancelCallback implements Web2CallbackMethod {

  private Logger logger = LoggerFactory.getLogger(MethodOracleRequestCancelCallback.class);

  @Value("${service-key.privatekey}")
  private String localPrivateKey;

  @Autowired
  private OracleContractCancelEventLogService oracleContractEventLogService;

  @Override
  public Object process(JsonRpc2Request request) {

    logger.info("ORACLE_REQUEST_CANCEL_CALLBACK receive params: {}",
        JSONObject.toJSONString(request));

    OracleRequetCancelCallbackResDTO callBackRespDTO = new OracleRequetCancelCallbackResDTO();
    try {
      LinkedHashMap params = request.getParams();
      String requestId = params.get("requestId").toString();
      String cancelTxHash = params.get("cancelTxHash").toString();


      UpdateCancelEventLogDTO updateCancelEventLogDTO = new UpdateCancelEventLogDTO();
      updateCancelEventLogDTO.setRequestId(requestId);
      updateCancelEventLogDTO.setCancelOracleHash(cancelTxHash);

      if (params.get("status") != null) {
        Integer status = (Integer) params.get("status");
        updateCancelEventLogDTO.setCancelStatus(status);
      }else{
        updateCancelEventLogDTO.setCancelStatus(CancelStatusEnum.SUCCESSFULLY.getCode());
      }


      oracleContractEventLogService.updateCancelEventLogByRequestId(updateCancelEventLogDTO);
      callBackRespDTO.setSuccessfully(CallbackProcessStatus.SUCCESSFUL.getCode());
    } catch (Exception e) {
      logger.error("ORACLE_REQUEST_CANCEL_CALLBACK process error:", e);
      callBackRespDTO.setSuccessfully(CallbackProcessStatus.FAILED.getCode());
      callBackRespDTO.setCallbackMessage(e.getMessage());
    }
    return callBackRespDTO;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    return null;
  }

  @Override
  public VnCallbackSignPayloadDTO buildSignPayload(String decryptStr) {

    OracleCallbackCancelResultDTO cancelResultDTO =
        JSONObject.parseObject(decryptStr, OracleCallbackCancelResultDTO.class);

    VnCallbackSignPayloadDTO vnCallbackSignPayloadDTO = new VnCallbackSignPayloadDTO();
    vnCallbackSignPayloadDTO.setRequestId(cancelResultDTO.getRequestId());
    vnCallbackSignPayloadDTO.setVnSignData(cancelResultDTO.getSignData());

    String signStr = cancelResultDTO.getRequestId()
        + cancelResultDTO.getCancelTxHash()
        + cancelResultDTO.getStatus();
    vnCallbackSignPayloadDTO.setSignStr(signStr);

    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("requestId", cancelResultDTO.getRequestId());
    linkedHashMap.put("signData", cancelResultDTO.getCancelTxHash());
    linkedHashMap.put("cancelTxHash", cancelResultDTO.getCancelTxHash());

    vnCallbackSignPayloadDTO.setMethodJsonRpc2Params(linkedHashMap);

    return vnCallbackSignPayloadDTO;
  }
}
