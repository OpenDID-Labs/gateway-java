
package io.opendid.web2gateway.oraclebodyhandler.homesubscription;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.ConsumerWrapBodyReqDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapAddConsumerBodyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareAddConsumerBodyRespDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainAddConsumerInterface;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapConsumerBodyClient;
import jakarta.annotation.Resource;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

@Component(HomeChainName.FLARE + HomeChainAddConsumerInterface.BEAN_SUFFIX)
public class FlareAddConsumerHandlerHome implements HomeChainAddConsumerInterface {

  private static final Logger logger = LoggerFactory.getLogger(FlareAddConsumerHandlerHome.class);

  @Resource
  private HomeWrapConsumerBodyClient homeWrapConsumerBodyClient;

  @Resource
  private VnGatewayClient vnGatewayClient;

  @Override
  public WarpFlareAddConsumerBodyRespDTO wrapBody(
      OracleWrapAddConsumerBodyDTO oracleWrapAddConsumerBodyDTO) throws Exception {

    String  homeChainPublicKey = oracleWrapAddConsumerBodyDTO.getWalletPublicKey();
    ConsumerWrapBodyReqDTO reqDTO = new ConsumerWrapBodyReqDTO();
    reqDTO.setPublicKey(homeChainPublicKey);
    reqDTO.setVnCode(oracleWrapAddConsumerBodyDTO.getVnCode());
    reqDTO.setSubId(oracleWrapAddConsumerBodyDTO.getSubId());
    reqDTO.setWalletAddress(oracleWrapAddConsumerBodyDTO.getConsumerAddress());
    JsonRpc2Response request = homeWrapConsumerBodyClient.getAddConsumerWarpReqBody(reqDTO);

    WarpFlareAddConsumerBodyRespDTO respDTO = JSONObject.parseObject(request.getResult().toString(), WarpFlareAddConsumerBodyRespDTO.class);

    logger.info("Add Consumer FlareChainMsg process warpResponse={}", JSON.toJSONString(respDTO));

    return respDTO;
  }

  @Override
  public OracleAddConsumerSignatureResDTO signature(
      OracleAddConsumerSignatureDTO oracleAddConsumerSignatureDTO
  ) throws Exception {

    // 1,Get private key
    String walletPrivateKey = oracleAddConsumerSignatureDTO.getVnWalletPrivateKey();

    // 2,Create Credentials
    Credentials credentials = Credentials.create(walletPrivateKey);
    byte[] signedMessage;
    RawTransaction rawTransaction = oracleAddConsumerSignatureDTO.getRawTransaction();
    Long chainId = oracleAddConsumerSignatureDTO.getChainId();

    // 3, Sign
    if (chainId != null) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }

    String signatureStr = Numeric.toHexString(signedMessage);

    // 4, Return signature string
    OracleAddConsumerSignatureResDTO signatureResDTO = new OracleAddConsumerSignatureResDTO();
    signatureResDTO.setSignature(signatureStr);

    return signatureResDTO;
  }

  @Override
  public JsonRpc2Response sendMessage(OracleAddConsumerSendDTO oracleAddConsumerSendDTO)
      throws Exception {

    OracleAddConsumerMetaDataDTO metaData = oracleAddConsumerSendDTO.getMetaData();

    JsonRpc2Request requestClientBody = oracleAddConsumerSendDTO.getRequestClientBody();

    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", oracleAddConsumerSendDTO.getSignatureResDTO().getSignature().toString());
    linkedHashMap.put("metadata", metaData);

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        requestClientBody.getId(),
        requestClientBody.getMethod(),
        linkedHashMap,
        ""
    );
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);
    vnClientJobIdDTO.setVnCode(metaData.getVnCode());

    JsonRpc2Response respResult = vnGatewayClient.requestSubscriptionSend(vnClientJobIdDTO);

    if (respResult == null){
      logger.error("FlareChain Add Consumer request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
          "Method Add Consumer call vn result is null");
    }

    logger.info("Add Consumer Method process send result={}",JSONObject.toJSONString(respResult.getResult()));

    return respResult;
  }
}
