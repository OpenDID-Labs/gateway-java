package io.opendid.web2gateway.oraclebodyhandler.homecancel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.catches.ChainSignKeyVaultCache;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.model.dto.oracle.CancelWarpReqBodyRequestDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleCancelSendMessageDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleChainCancelMsgDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestCancelMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.WarpAptosReqBodyRequestDTO;
import io.opendid.web2gateway.model.dto.oracle.WarpAptosReqBodyResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterface;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapCancelBodyClient;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.service.HomeChainKeyManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component(HomeChainName.APTOS + HomeChainRequestCancelBodyInterface.BEAN_SUFFIX)
@Deprecated
public class AptosRequestCancelBodyHandlerHome implements HomeChainRequestCancelBodyInterface {

  @Value("${oracle.callBack.url}")
  private String callBackUrl;
  @Autowired
  private VnGatewayClient vnGatewayClient;

  @Autowired
  private HomeWrapCancelBodyClient cancelBodyClient;
  @Autowired
  private HomeChainKeyManageService homeChainKeyManageService;

  private Logger logger = LoggerFactory.getLogger(HomeChainRequestCancelBodyInterface.class);


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
    vnClientJobIdDTO.setVnCode(sendMessageDTO.getVnCode());

    JsonRpc2Response respResult = vnGatewayClient.requestJobSend(vnClientJobIdDTO);

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


    String vnCode = oracleChainCancelMsgDTO.getVnCode();
    GatewayHomechainKeyManage gatewayHomechainKeyManage = homeChainKeyManageService.selectByVnCode(vnCode);
    String  homeChainPublicKey = gatewayHomechainKeyManage.getWalletPublicKey();

    WarpAptosReqBodyRequestDTO warpAptosReqBodyRequestDTO = new WarpAptosReqBodyRequestDTO();

    logger.info("Cancel AptosChainMsg process warp RequestBody prams={}", JSON.toJSONString(warpAptosReqBodyRequestDTO));

    CancelWarpReqBodyRequestDTO cancelWarpReqBodyRequestDTO = new CancelWarpReqBodyRequestDTO();
    cancelWarpReqBodyRequestDTO.setRequestId(oracleChainCancelMsgDTO.getRequestId());
    cancelWarpReqBodyRequestDTO.setJobId(oracleChainCancelMsgDTO.getJobId());
    cancelWarpReqBodyRequestDTO.setPublicKey(homeChainPublicKey);
    cancelWarpReqBodyRequestDTO.setVnCode(vnCode);
//    cancelWarpReqBodyRequestDTO.setKeyCode(oracleChainCancelMsgDTO.getKeyCode());


    JsonRpc2Response request = cancelBodyClient.getCancelWarpReqBody(cancelWarpReqBodyRequestDTO);

    WarpAptosReqBodyResponseDTO warpReqBody =
            JSONObject.parseObject(request.getResult().toString(), WarpAptosReqBodyResponseDTO.class);

    logger.info("Cancel AptosChainMsg process warp RequestBody={}",JSON.toJSONString(warpReqBody));

//     1,Signing with a private key
    ChainKeyDTO chainKeyByKeyCode = ChainSignKeyVaultCache.getChainKeyByKeyCode(oracleChainCancelMsgDTO.getKeyCode());
    String walletPrivateKey=chainKeyByKeyCode.getPrivateKey();
//    String walletPrivateKey = GatewayKeyVaultUtil.getServiceKey(vnCode + GatewayKeyVaultUtil.walletPrivateKey);
    byte[] signature = AptosUtils.ed25519Sign(
        AptosUtils.hexToByteArray(walletPrivateKey),
        AptosUtils.hexToByteArray(warpReqBody.getEncodeHash()));

    warpReqBody.getSignature().setSignature(AptosUtils.byteArrayToHexWithPrefix(signature));

    return JSON.toJSONString(warpReqBody);
  }

  @Override
  public OracleRequestCancelMetaDataDTO wrapBody(OracleChainCancelMsgDTO oracleChainCancelMsgDTO) {

//    String  homeChainPublicKey = GatewayKeyVaultUtil.getKey(GatewayKeyVaultUtil.walletPublicKey);
    GatewayHomechainKeyManage gatewayHomechainKeyManage = homeChainKeyManageService.selectByVnCode(oracleChainCancelMsgDTO.getVnCode());
    String  homeChainPublicKey = gatewayHomechainKeyManage.getWalletPublicKey();

    OracleRequestCancelMetaDataDTO metaData = new OracleRequestCancelMetaDataDTO();
    metaData.setPublicKey(homeChainPublicKey);
    metaData.setCallbackUrl(callBackUrl);
    metaData.setRequestId(oracleChainCancelMsgDTO.getRequestId());
    /*metaData.setJobId(oracleChainCancelMsgDTO.getJobId());
    metaData.setNonce(oracleChainCancelMsgDTO.getNonce());*/

    return metaData;
  }



}
