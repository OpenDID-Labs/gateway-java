
package io.opendid.web2gateway.oraclebodyhandler.homesubscription;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareRemoveConsumerBodyRespDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.ConsumerWrapBodyReqDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapRemoveConsumerBodyDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRemoveConsumerInterface;
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

@Component(HomeChainName.FLARE + HomeChainRemoveConsumerInterface.BEAN_SUFFIX)
public class FlareRemoveConsumerHandlerHome implements HomeChainRemoveConsumerInterface {

  private static final Logger logger = LoggerFactory.getLogger(FlareRemoveConsumerHandlerHome.class);

  @Resource
  private HomeWrapConsumerBodyClient homeWrapConsumerBodyClient;
  @Resource
  private VnGatewayClient vnGatewayClient;

  @Override
  public WarpFlareRemoveConsumerBodyRespDTO wrapBody(
      OracleWrapRemoveConsumerBodyDTO oracleWrapRemoveConsumerBodyDTO
  ) throws Exception {

    String  homeChainPublicKey = oracleWrapRemoveConsumerBodyDTO.getWalletPublicKey();
    ConsumerWrapBodyReqDTO reqDTO = new ConsumerWrapBodyReqDTO();
    reqDTO.setPublicKey(homeChainPublicKey);
    reqDTO.setVnCode(oracleWrapRemoveConsumerBodyDTO.getVnCode());
    reqDTO.setSubId(oracleWrapRemoveConsumerBodyDTO.getSubId());
    reqDTO.setWalletAddress(oracleWrapRemoveConsumerBodyDTO.getConsumerAddress());
    JsonRpc2Response request = homeWrapConsumerBodyClient.getRemoveConsumerWarpReqBody(reqDTO);

    WarpFlareRemoveConsumerBodyRespDTO respDTO = JSONObject.parseObject(request.getResult().toString(), WarpFlareRemoveConsumerBodyRespDTO.class);

    logger.info("Remove Consumer FlareChainMsg process warpResponse={}", JSON.toJSONString(respDTO));

    return respDTO;
  }

  @Override
  public OracleRemoveConsumerSignatureResDTO signature(
      OracleRemoveConsumerSignatureDTO oracleRemoveConsumerSignatureDTO
  ) throws Exception {

    // 1,Get private key
    String walletPrivateKey = oracleRemoveConsumerSignatureDTO.getVnWalletPrivateKey();

    // 2,Create Credentials
    Credentials credentials = Credentials.create(walletPrivateKey);
    byte[] signedMessage;
    RawTransaction rawTransaction = oracleRemoveConsumerSignatureDTO.getRawTransaction();
    Long chainId = oracleRemoveConsumerSignatureDTO.getChainId();

    // 3, Sign
    if (chainId != null) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }

    String signatureStr = Numeric.toHexString(signedMessage);

    // 4, Return signature string
    OracleRemoveConsumerSignatureResDTO signatureResDTO = new OracleRemoveConsumerSignatureResDTO();
    signatureResDTO.setSignature(signatureStr);

    return signatureResDTO;
  }

  @Override
  public JsonRpc2Response sendMessage(OracleRemoveConsumerSendDTO oracleRemoveConsumerSendDTO)
      throws Exception {

    OracleRemoveConsumerMetaDataDTO metaData = oracleRemoveConsumerSendDTO.getMetaData();

    JsonRpc2Request requestClientBody = oracleRemoveConsumerSendDTO.getRequestClientBody();

    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", oracleRemoveConsumerSendDTO.getSignatureResDTO().getSignature().toString());
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
      logger.error("FlareChain Remove Consumer request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
          "Method Remove Consumer call vn result is null");
    }

    logger.info("Remove Consumer Method process send result={}",JSONObject.toJSONString(respResult.getResult()));

    return respResult;
  }


}
