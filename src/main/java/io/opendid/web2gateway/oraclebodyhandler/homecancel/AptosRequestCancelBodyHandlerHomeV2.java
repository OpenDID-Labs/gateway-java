package io.opendid.web2gateway.oraclebodyhandler.homecancel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.utils.GatewayKeyVaultUtil;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterface;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterfaceV2;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapCancelBodyClient;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.service.HomeChainKeyManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;


@Component(HomeChainName.APTOS + HomeChainRequestCancelBodyInterfaceV2.BEAN_SUFFIX)
public class AptosRequestCancelBodyHandlerHomeV2 implements HomeChainRequestCancelBodyInterfaceV2 {

  private static  Logger logger=LoggerFactory.getLogger(AptosRequestCancelBodyHandlerHomeV2.class);

  @Autowired
  private HomeWrapCancelBodyClient cancelBodyClient;

  @Autowired
  private VnGatewayClient vnGatewayClient;



  @Override
  public OracleWrapCancelBodyResDTO wrapBody(OracleWrapCancelBodyDTO oracleWrapCancelBodyDTO) throws Exception {




    String vnCode = oracleWrapCancelBodyDTO.getVnCode();
    String  homeChainPublicKey = oracleWrapCancelBodyDTO.getWalletPublicKey();

    CancelWarpReqBodyRequestDTO cancelWarpReqBodyRequestDTO = new CancelWarpReqBodyRequestDTO();
    cancelWarpReqBodyRequestDTO.setRequestId(oracleWrapCancelBodyDTO.getRequestId());
    cancelWarpReqBodyRequestDTO.setJobId(oracleWrapCancelBodyDTO.getJobId());
    cancelWarpReqBodyRequestDTO.setPublicKey(homeChainPublicKey);
    cancelWarpReqBodyRequestDTO.setVnCode(vnCode);


    JsonRpc2Response request = cancelBodyClient.getCancelWarpReqBody(cancelWarpReqBodyRequestDTO);
    WarpAptosReqBodyResponseDTO warpReqBody =
            JSONObject.parseObject(request.getResult().toString(), WarpAptosReqBodyResponseDTO.class);

    logger.info("Cancel AptosChainMsg process warp RequestBody={}",JSON.toJSONString(warpReqBody));
    OracleWrapCancelBodyResDTO oracleWrapCancelBodyResDTO = new OracleWrapCancelBodyResDTO();
    oracleWrapCancelBodyResDTO.setWrapData(warpReqBody);
    return oracleWrapCancelBodyResDTO;
  }

  @Override
  public OracleCancelSignatureResDTO signature(OracleCancelSignatureDTO oracleCancelSignatureDTO) throws Exception, JsonRpc2ServerErrorException {

//    ChainKeyDTO chainKeyByKeyCode = GatewayKeyVaultUtil.getChainKeyByKeyCode(oracleChainCancelMsgDTO.getKeyCode());
//    String walletPrivateKey=chainKeyByKeyCode.getPrivateKey();

    WarpAptosReqBodyResponseDTO warpReqBody = (WarpAptosReqBodyResponseDTO)oracleCancelSignatureDTO.getWrapData();
    byte[] signature = AptosUtils.ed25519Sign(
            AptosUtils.hexToByteArray(oracleCancelSignatureDTO.getVnWalletPrivateKey()),
            AptosUtils.hexToByteArray(warpReqBody.getEncodeHash()));

    warpReqBody.getSignature().setSignature(AptosUtils.byteArrayToHexWithPrefix(signature));
    OracleCancelSignatureResDTO resDTO=new OracleCancelSignatureResDTO();
    resDTO.setReWrapOriginWrapData(warpReqBody);
    resDTO.setSignature(signature);
    return resDTO;
  }

  @Override
  public JsonRpc2Response sendMessage(OracleCancelSendDTO oracleCancelSendDTO) throws Exception, JsonRpc2ServerErrorException {

    OracleRequestCancelMetaDataDTO metaData = oracleCancelSendDTO.getMetaData();

    JsonRpc2Request requestClientBody = oracleCancelSendDTO.getRequestClientBody();
    WarpAptosReqBodyResponseDTO reWrapOriginWrapData =(WarpAptosReqBodyResponseDTO)
            oracleCancelSendDTO.getSignatureResDTO().getReWrapOriginWrapData();


    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", JSON.toJSONString(reWrapOriginWrapData));
    linkedHashMap.put("metadata", metaData);

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setJobId(metaData.getJobId());
    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
            requestClientBody.getId(),
            requestClientBody.getMethod(),
            linkedHashMap,
            ""
    );
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);
    vnClientJobIdDTO.setVnCode(metaData.getVnCode());

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
}
