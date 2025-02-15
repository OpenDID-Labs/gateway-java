package io.opendid.web2gateway.oraclechainmsghandler.aptos.cancel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.utils.GatewayKeyVaultUtil;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.CancelWarpReqBodyRequestDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleCancelSendMessageDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleChainCancelMsgDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleChainMsgDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleNonceUpdateDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestCancelMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleSendMessageDTO;
import io.opendid.web2gateway.model.dto.oracle.WarpReqBodyRequestDTO;
import io.opendid.web2gateway.model.dto.oracle.WarpReqBodyResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclechainmsghandler.interfaces.ChainRequestCancelBodyHandler;
import io.opendid.web2gateway.service.OracleNonceService;
import io.opendid.web2gateway.service.WarpRequestBodyService;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component(HomeChainName.APTOS + ChainRequestCancelBodyHandler.BEAN_SUFFIX)
public class AptosChainRequestCancelBody implements ChainRequestCancelBodyHandler {

  @Value("${wallet.privatekey}")
  private String homeChainPrivateKey;
  @Value("${oracle.callBack.url}")
  private String callBackUrl;
  @Autowired
  private VnGatewayClient vnGatewayClient;

  @Autowired
  private WarpRequestBodyService warpRequestBodyService;

  private Logger logger = LoggerFactory.getLogger(ChainRequestCancelBodyHandler.class);


  @Override
  public JsonRpc2Response sendMessage(OracleCancelSendMessageDTO sendMessageDTO) throws Exception, JsonRpc2ServerErrorException {

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setJobId(sendMessageDTO.getJobId());
    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        sendMessageDTO.getId(),
        sendMessageDTO.getMethod(),
        sendMessageDTO.getLinkedHashMap(),
        ""
    );
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);

    JsonRpc2Response respResult = vnGatewayClient.request(vnClientJobIdDTO);

    if (respResult == null){
      logger.error("AptosChainRequestCancelBody request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
          "Method cancelRequest call vn result is null");
    }

    logger.info("Cancel MethodOracleRequest process send result={}",JSONObject.toJSONString(respResult.getResult()));

    return respResult;
  }

  @Override
  public String signature(OracleChainCancelMsgDTO oracleChainCancelMsgDTO) throws Exception, JsonRpc2ServerErrorException {

    String  homeChainPublicKey = GatewayKeyVaultUtil.getKey(GatewayKeyVaultUtil.walletPublicKey);

    WarpReqBodyRequestDTO warpReqBodyRequestDTO = new WarpReqBodyRequestDTO();

    logger.info("Cancel AptosChainMsg process warp RequestBody prams={}", JSON.toJSONString(warpReqBodyRequestDTO));

    CancelWarpReqBodyRequestDTO cancelWarpReqBodyRequestDTO = new CancelWarpReqBodyRequestDTO();
    cancelWarpReqBodyRequestDTO.setRequestId(oracleChainCancelMsgDTO.getRequestId());
    cancelWarpReqBodyRequestDTO.setJobId(oracleChainCancelMsgDTO.getJobId());
    cancelWarpReqBodyRequestDTO.setPublicKey(homeChainPublicKey);

    // cancelWarpReqBodyRequestDTO.setNonce(oracleChainCancelMsgDTO.getNonce().toString());

    WarpReqBodyResponseDTO warpReqBody = warpRequestBodyService.getCancelWarpReqBody(cancelWarpReqBodyRequestDTO);

    logger.info("Cancel AptosChainMsg process warp RequestBody={}",JSON.toJSONString(warpReqBody));

    // 1,Signing with a private key
    byte[] signature = AptosUtils.ed25519Sign(
        AptosUtils.hexToByteArray(homeChainPrivateKey),
        AptosUtils.hexToByteArray(warpReqBody.getEncodeHash()));

    warpReqBody.getSignature().setSignature(AptosUtils.byteArrayToHexWithPrefix(signature));

    return JSON.toJSONString(warpReqBody);
  }

  @Override
  public OracleRequestCancelMetaDataDTO assemblyMessage(OracleChainCancelMsgDTO oracleChainCancelMsgDTO) {

    String  homeChainPublicKey = GatewayKeyVaultUtil.getKey(GatewayKeyVaultUtil.walletPublicKey);

    OracleRequestCancelMetaDataDTO metaData = new OracleRequestCancelMetaDataDTO();
    metaData.setPublicKey(homeChainPublicKey);
    metaData.setCallbackUrl(callBackUrl);
    metaData.setRequestId(oracleChainCancelMsgDTO.getRequestId());
    /*metaData.setJobId(oracleChainCancelMsgDTO.getJobId());
    metaData.setNonce(oracleChainCancelMsgDTO.getNonce());*/

    return metaData;
  }



}
