package io.opendid.web2gateway.oraclebodyhandler.homecancel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterfaceV2;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapCancelBodyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.util.LinkedHashMap;


@Component(HomeChainName.FLARE + HomeChainRequestCancelBodyInterfaceV2.BEAN_SUFFIX)
public class FlareRequestCancelBodyHandlerHome implements HomeChainRequestCancelBodyInterfaceV2 {

  private static  Logger logger=LoggerFactory.getLogger(FlareRequestCancelBodyHandlerHome.class);

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
    WarpFlareReqCancelBodyResponseDTO warpReqBody =
            JSONObject.parseObject(request.getResult().toString(), WarpFlareReqCancelBodyResponseDTO.class);

    logger.info("Cancel AptosChainMsg process warp RequestBody={}",JSON.toJSONString(warpReqBody));
    OracleWrapCancelBodyResDTO oracleWrapCancelBodyResDTO = new OracleWrapCancelBodyResDTO();
    oracleWrapCancelBodyResDTO.setWrapData(warpReqBody);
    return oracleWrapCancelBodyResDTO;
  }

  @Override
  public OracleCancelSignatureResDTO signature(OracleCancelSignatureDTO oracleCancelSignatureDTO) throws Exception, JsonRpc2ServerErrorException {

    // 1,Get private key
    String walletPrivateKey = oracleCancelSignatureDTO.getVnWalletPrivateKey();

    // 2,Get wait signature data
    WarpFlareReqCancelBodyResponseDTO warpReqBody = (WarpFlareReqCancelBodyResponseDTO)oracleCancelSignatureDTO.getWrapData();

    // 3,Create Credentials
    Credentials credentials = Credentials.create(walletPrivateKey);
    byte[] signedMessage;
    RawTransaction rawTransaction = warpReqBody.getRawTransaction();
    Long chainId = warpReqBody.getChainId();

    // 4, Sign
    if (chainId != null) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }

    String signatureStr = Numeric.toHexString(signedMessage);

    // 5, Return signature string
    OracleCancelSignatureResDTO requestSignatureResDTO = new OracleCancelSignatureResDTO();
    requestSignatureResDTO.setSignature(signatureStr);

    return requestSignatureResDTO;
  }

  @Override
  public JsonRpc2Response sendMessage(OracleCancelSendDTO oracleCancelSendDTO) throws Exception, JsonRpc2ServerErrorException {

    OracleRequestCancelMetaDataDTO metaData = oracleCancelSendDTO.getMetaData();

    JsonRpc2Request requestClientBody = oracleCancelSendDTO.getRequestClientBody();

    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", oracleCancelSendDTO.getSignatureResDTO().getSignature().toString());
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
      logger.error("FlareChainRequestCancelBody request vn gateway return null");
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
