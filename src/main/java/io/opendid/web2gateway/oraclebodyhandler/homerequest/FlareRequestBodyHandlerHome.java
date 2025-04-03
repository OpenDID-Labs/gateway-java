package io.opendid.web2gateway.oraclebodyhandler.homerequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.utils.ECDSAUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.config.WalletPrivateKeyConfig;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestBodyInterface;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapRequestBodyClient;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.service.VngatewayRouteInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component(HomeChainName.FLARE + HomeChainRequestBodyInterface.BEAN_SUFFIX)
public class FlareRequestBodyHandlerHome implements HomeChainRequestBodyInterface {

  private Logger logger = LoggerFactory.getLogger(FlareRequestBodyHandlerHome.class);

  @Autowired
  private HomeWrapRequestBodyClient requestBodyClient;
  @Autowired
  private VnGatewayClient vnGatewayClient;
  @Autowired
  private WalletPrivateKeyConfig walletPrivateKeyConfig;
  @Autowired
  private VngatewayRouteInfoService vngatewayRouteInfoService;


  @Override
  public OracleWrapRequestBodyResDTO wrapBody(OracleWrapRequestBodyDTO oracleWrapRequestBodyDTO) throws Exception {


    WarpAptosReqBodyRequestDTO warpAptosReqBodyRequestDTO = new WarpAptosReqBodyRequestDTO();
    warpAptosReqBodyRequestDTO.setJobId(oracleWrapRequestBodyDTO.getJobId());
    warpAptosReqBodyRequestDTO.setPublicKey(oracleWrapRequestBodyDTO.getPublicKey());
    warpAptosReqBodyRequestDTO.setData(oracleWrapRequestBodyDTO.getData());
    warpAptosReqBodyRequestDTO.setVnCode(oracleWrapRequestBodyDTO.getVnCode());
    warpAptosReqBodyRequestDTO.setGenerateClaim(oracleWrapRequestBodyDTO.getGenerateClaim());
    warpAptosReqBodyRequestDTO.setSubId(oracleWrapRequestBodyDTO.getSubId());

    logger.info("FlareRequestBodyHandlerHome MethodOracleRequest process warp RequestBody prams={}",
        JSON.toJSONString(warpAptosReqBodyRequestDTO));

    // Get warp request body
    JsonRpc2Response request = requestBodyClient.getWarpReqBody(
        warpAptosReqBodyRequestDTO);
    WarpFlareReqBodyResponseDTO warpReqBody =
        JSONObject.parseObject(request.getResult().toString(), WarpFlareReqBodyResponseDTO.class);
    logger.info("FlareRequestBodyHandlerHome MethodOracleRequest process warp RequestBody={}", JSON.toJSONString(warpReqBody));

    OracleWrapRequestBodyResDTO requestBodyResDTO = new OracleWrapRequestBodyResDTO();
    requestBodyResDTO.setWrapData(warpReqBody);
    requestBodyResDTO.setClaimFee(warpReqBody.getClaimFee());
    requestBodyResDTO.setClaimFeeFree(warpReqBody.getClaimFeeFree());
    requestBodyResDTO.setJobIdFee(warpReqBody.getJobIdFee());
    requestBodyResDTO.setJobIdFree(warpReqBody.getJobIdFree());

    return requestBodyResDTO;

  }

  @Override
  public OracleRequestSignatureResDTO signature(OracleRequestSignatureDTO oracleRequestSignatureDTO) throws Exception, JsonRpc2ServerErrorException {

    // 1,Get private key
    String walletPrivateKey = oracleRequestSignatureDTO.getVnWalletPrivateKey();

    WarpFlareReqBodyResponseDTO wrapRequestBodyResDTO =
        (WarpFlareReqBodyResponseDTO) oracleRequestSignatureDTO.getWrapData();

    // 2,Create Credentials
    Credentials credentials = Credentials.create(walletPrivateKey);
    byte[] signedMessage;
    RawTransaction rawTransaction = wrapRequestBodyResDTO.getRawTransaction();
    Long chainId = wrapRequestBodyResDTO.getChainId();

    // 3, Sign
    if (chainId != null) {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
    } else {
      signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
    }

    String signatureStr = Numeric.toHexString(signedMessage);

    // 4, Return signature string
    OracleRequestSignatureResDTO requestSignatureResDTO = new OracleRequestSignatureResDTO();
    requestSignatureResDTO.setSignature(signatureStr);

    return requestSignatureResDTO;
  }


  @Override
  public JsonRpc2Response sendMessage(OracleRequestSendMessageDTO sendMessageDTO) throws Exception, JsonRpc2ServerErrorException {

    OracleRequestMetaDataDTO metaData = sendMessageDTO.getMetaData();
    OracleRequestSignatureResDTO signatureResDTO = sendMessageDTO.getSignatureResDTO();
    JsonRpc2Request requestClientBody = sendMessageDTO.getRequestClientBody();

    LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("signedTx", signatureResDTO.getSignature().toString());
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
    JsonRpc2Response respResult = vnGatewayClient.requestJobSend(vnClientJobIdDTO);

    return respResult;
  }


  @Override
  public List<GeneratePubKeyAndAddrDTO> generatePublicKeyAndAddr() throws Exception {

    List<Map<String,String>> walletPrivateKeyList = walletPrivateKeyConfig.getPrivatekey();

    List<VngatewayRouteInfo> vngatewayRouteInfos = vngatewayRouteInfoService.selectAllVn();

    // The number of configured private keys must be consistent with the number of vn
    if (vngatewayRouteInfos.size() != walletPrivateKeyList.size()){
      throw new RuntimeException("In the configuration: wallet.privatekey, " +
          "the number of private keys included must be consistent with the number of Vn." +
          "Vn quantity: "+vngatewayRouteInfos.size());
    }


    List<GeneratePubKeyAndAddrDTO> resultList = new ArrayList<>();

    for (int i = 0; i < walletPrivateKeyList.size(); i++) {

      String privateKeyCode= walletPrivateKeyList.get(i).get(WalletPrivateKeyConfig.KEY_NAME);
      if (StringUtils.isBlank(privateKeyCode)){
        logger.warn(" KeyCode is missing");
        continue;
      }
      String walletPrivateKey = walletPrivateKeyList.get(i).get(WalletPrivateKeyConfig.KEY_VALUE);
      VngatewayRouteInfo vngatewayRouteInfo = vngatewayRouteInfos.get(i);

      // Generate public key
      String pubKeyStr = ECDSAUtils.getHexPubKey(walletPrivateKey);

      // Generate address
      String address = ECDSAUtils.getHexAddress(pubKeyStr);


      GeneratePubKeyAndAddrDTO generatePubKeyAndAddrDTO = new GeneratePubKeyAndAddrDTO();
      generatePubKeyAndAddrDTO.setPublicKey(pubKeyStr);
      generatePubKeyAndAddrDTO.setWalletAddress(address);
      generatePubKeyAndAddrDTO.setVnCode(vngatewayRouteInfo.getVnCode());
      generatePubKeyAndAddrDTO.setPrivateKey(walletPrivateKey);
      generatePubKeyAndAddrDTO.setPrivateKeyCode(privateKeyCode);

      resultList.add(generatePubKeyAndAddrDTO);
    }

    return resultList;
  }

}
