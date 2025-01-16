package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.CancelStatusEnum;
import io.opendid.web2gateway.common.enums.status.ChainErrorEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.ContractEventLogUpdateForCancelDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleCancelSendMessageDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleChainCancelMsgDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleNonceUpdateDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestCancelMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestCancelRespDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestErrorDataDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclechainmsghandler.factory.ChainRequestCancelBodyFactory;
import io.opendid.web2gateway.oraclechainmsghandler.interfaces.ChainRequestCancelBodyHandler;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import io.opendid.web2gateway.service.OracleNonceService;
import java.util.LinkedHashMap;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  private Logger logger = LoggerFactory.getLogger(MethodOracleRequestCancel.class);

  @Override
  public OracleRequestCancelRespDTO process(JsonRpc2Request request)
      throws Exception, JsonRpc2ServerErrorException {

    if (request.getParams() == null) {
      return new OracleRequestCancelRespDTO();
    }

    ChainRequestCancelBodyHandler cancelBodyHandler = ChainRequestCancelBodyFactory.createCancelBodyHandler(
        HomeChainName.APTOS);

    LinkedHashMap params = request.getParams();
    String requestId = MapUtils.getString(params, "requestId");

    logger.info("MethodOracleRequestCancel process requestId={}", requestId);

    OdOracleContractEventlog odOracleContractEventlog =
        odOracleContractEventlogMapper.selectByRequestId(requestId);

    OracleChainCancelMsgDTO oracleChainCancelMsgDTO = new OracleChainCancelMsgDTO();
    oracleChainCancelMsgDTO.setRequestId(requestId);
    oracleChainCancelMsgDTO.setJobId(odOracleContractEventlog.getJobId());

    // Signature transaction
    String signature;

    try {

      // Send transaction
      signature = cancelBodyHandler.signature(oracleChainCancelMsgDTO);

    } catch (JsonRpc2ServerErrorException jsonRpc2ServerErrorException) {

      if(jsonRpc2ServerErrorException.getData()!=null){

        String data = jsonRpc2ServerErrorException.getData().toString();

        if (data.contains(ChainErrorEnum.NOT_EXPIRED.getCode())
            || data.contains(ChainErrorEnum.INSUFFICIENT_BALANCE.getCode())) {

          ContractEventLogUpdateForCancelDTO updateForCancelDTO = new ContractEventLogUpdateForCancelDTO();
          updateForCancelDTO.setRequestId(requestId);
          updateForCancelDTO.setCancelStatus(CancelStatusEnum.PENDING.getCode());
          eventLogService.cancelTxLogHandle(updateForCancelDTO);

        }

      }

      throw jsonRpc2ServerErrorException;
    }

    OracleChainCancelMsgDTO oracleChainMsgDTO = new OracleChainCancelMsgDTO();
    oracleChainMsgDTO.setJobId(odOracleContractEventlog.getJobId());
    oracleChainMsgDTO.setRequestId(requestId);

    // Assembly parameters
    OracleRequestCancelMetaDataDTO metaData = cancelBodyHandler.assemblyMessage(oracleChainMsgDTO);

    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", signature);
    linkedHashMap.put("metadata", metaData);

    OracleCancelSendMessageDTO sendMessageDTO = new OracleCancelSendMessageDTO();
    sendMessageDTO.setId(request.getId());
    sendMessageDTO.setMethod(request.getMethod());
    sendMessageDTO.setJobId(odOracleContractEventlog.getJobId());
    sendMessageDTO.setLinkedHashMap(linkedHashMap);

    // Send transaction
    JsonRpc2Response respResult = cancelBodyHandler.sendMessage(sendMessageDTO);

    JSONObject respResultJson = JSONObject.parseObject(
        JSONObject.toJSONString(respResult.getResult()));

    ContractEventLogUpdateForCancelDTO updateForCancelDTO = new ContractEventLogUpdateForCancelDTO();
    updateForCancelDTO.setRequestId(requestId);
    updateForCancelDTO.setRequestBody(JSONObject.toJSONString(request));

    if (respResultJson.get("failReason") != null && !"".equals(respResultJson.get("failReason"))) {
      updateForCancelDTO.setCancelErrorMsg(respResultJson.getString("failReason"));
      updateForCancelDTO.setCancelStatus(CancelStatusEnum.FAIL.getCode());
    } else {
      updateForCancelDTO.setCancelOracleHash(respResultJson.getString("oracleRequestHash"));
      updateForCancelDTO.setCancelStatus(CancelStatusEnum.PENDING.getCode());
    }

    // update cancel transaction log
    eventLogService.cancelTxLogHandle(updateForCancelDTO);

    if (respResultJson.get("failReason") != null && !"".equals(respResultJson.get("failReason"))) {

      OracleRequestErrorDataDTO oracleRequestErrorDataDTO = new OracleRequestErrorDataDTO();
      oracleRequestErrorDataDTO.setRequestId(requestId);

      String errorData = JSON.toJSONString(oracleRequestErrorDataDTO);
      logger.info("MethodOracleRequestCancel process error data={}", errorData);

      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getCode(),
          "",
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32001.getMessage(),
          oracleRequestErrorDataDTO);
    }

    return JSONObject.parseObject(respResult.getResult().toString(),
        OracleRequestCancelRespDTO.class);
  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    return null;
  }

  private Long getNonce(String publicKey) throws Exception {

    String address = AptosUtils.generateAddressFromPublicKey(publicKey);

    OracleNonceUpdateDTO oracleNonceUpdateDTO = new OracleNonceUpdateDTO();
    oracleNonceUpdateDTO.setPublicKey(publicKey);
    oracleNonceUpdateDTO.setAddress(address);

    return oracleNonceService.updateOracleNonce(oracleNonceUpdateDTO);
  }
}
