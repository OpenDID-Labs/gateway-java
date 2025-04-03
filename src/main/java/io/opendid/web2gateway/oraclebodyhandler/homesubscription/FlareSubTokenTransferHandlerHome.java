
package io.opendid.web2gateway.oraclebodyhandler.homesubscription;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapSubTokenTransferBodyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubTokenTransferWrapBodyReqDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareAddConsumerBodyRespDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareSubTokenTransferBodyRespDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainSubTokenTransferInterface;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapConsumerBodyClient;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapSubTokenTransferBodyClient;
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

@Component(HomeChainName.FLARE + HomeChainSubTokenTransferInterface.BEAN_SUFFIX)
public class FlareSubTokenTransferHandlerHome implements HomeChainSubTokenTransferInterface {

  private static final Logger logger = LoggerFactory.getLogger(FlareSubTokenTransferHandlerHome.class);

  @Resource
  private HomeWrapSubTokenTransferBodyClient homeWrapSubTokenTransferBodyClient;

  @Resource
  private VnGatewayClient vnGatewayClient;


  @Override
  public WarpFlareSubTokenTransferBodyRespDTO wrapBody(
      OracleWrapSubTokenTransferBodyDTO oracleWrapSubTokenTransferBodyDTO
  ) throws Exception {

    String  homeChainPublicKey = oracleWrapSubTokenTransferBodyDTO.getWalletPublicKey();

    SubTokenTransferWrapBodyReqDTO reqDTO = new SubTokenTransferWrapBodyReqDTO();
    reqDTO.setPublicKey(homeChainPublicKey);
    reqDTO.setVnCode(oracleWrapSubTokenTransferBodyDTO.getVnCode());
    reqDTO.setSubId(oracleWrapSubTokenTransferBodyDTO.getSubId());
    reqDTO.setWalletAddress(oracleWrapSubTokenTransferBodyDTO.getWalletAddress());
    reqDTO.setAmount(oracleWrapSubTokenTransferBodyDTO.getAmount());

    JsonRpc2Response request = homeWrapSubTokenTransferBodyClient.getSubTokenTransferReqBody(reqDTO);

    WarpFlareSubTokenTransferBodyRespDTO respDTO = JSONObject.parseObject(request.getResult().toString(), WarpFlareSubTokenTransferBodyRespDTO.class);

    logger.info("SubTokenTransfer FlareChainMsg process warpResponse={}", JSON.toJSONString(respDTO));

    return respDTO;
  }

  @Override
  public OracleSubTokenTransferSignatureResDTO signature(
      OracleSubTokenTransferSignatureDTO oracleSubTokenTransferSignatureDTO
  ) throws Exception {

    // 1,Get private key
    String walletPrivateKey = oracleSubTokenTransferSignatureDTO.getVnWalletPrivateKey();

    // 2,Create Credentials
    Credentials credentials = Credentials.create(walletPrivateKey);
    byte[] signedMessage;
    RawTransaction rawTransaction = oracleSubTokenTransferSignatureDTO.getRawTransaction();
    Long chainId = oracleSubTokenTransferSignatureDTO.getChainId();

    // 3, Sign
    if (chainId != null) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }

    String signatureStr = Numeric.toHexString(signedMessage);

    // 4, Return signature string
    OracleSubTokenTransferSignatureResDTO signatureResDTO = new OracleSubTokenTransferSignatureResDTO();
    signatureResDTO.setSignature(signatureStr);

    return signatureResDTO;
  }

  @Override
  public JsonRpc2Response sendMessage(
      OracleSubTokenTransferSendDTO oracleSubTokenTransferSendDTO
  ) throws Exception {

    OracleSubTokenTransferMetaDataDTO metaData = oracleSubTokenTransferSendDTO.getMetaData();

    JsonRpc2Request requestClientBody = oracleSubTokenTransferSendDTO.getRequestClientBody();

    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", oracleSubTokenTransferSendDTO.getSignatureResDTO().getSignature().toString());
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
      logger.error("FlareChain SubTokenTransfer request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
          "Method SubTokenTransfer call vn result is null");
    }

    logger.info("SubTokenTransfer Method process send result={}",JSONObject.toJSONString(respResult.getResult()));

    return respResult;
  }
}
