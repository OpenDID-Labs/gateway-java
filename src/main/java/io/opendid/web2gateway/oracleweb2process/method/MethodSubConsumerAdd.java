package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.catches.SubIdCache;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.ConsumerStatusEnum;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubConsumerAddMethodResResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapAddConsumerBodyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareAddConsumerBodyRespDTO;
import io.opendid.web2gateway.model.dto.subscription.SubIdCacheDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainAddConsumerFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainAddConsumerInterface;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.repository.model.SubscriptionConsumer;
import io.opendid.web2gateway.service.ConsumerDataService;
import io.opendid.web2gateway.service.HomeChainKeyManageService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.SUB_ADD_CONSUMER + Web2Method.BEAN_SUFFIX)
@MethodSubscription
public class MethodSubConsumerAdd implements Web2SubscriptionMethod {

  private static final Logger logger = LoggerFactory.getLogger(MethodSubConsumerAdd.class);

  @Resource
  private HomeChainKeyManageService homeChainKeyManageService;
  @Autowired
  private ConsumerDataService consumerDataService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    HomeChainAddConsumerInterface subscriptionHandler = HomeChainAddConsumerFactory.createSubscriptionHandler();

    // select key manage
    String walletAddress = request.getParams().get("walletAddress").toString();
    String subId = request.getParams().get("subId").toString();

    SubIdCacheDTO subIdCacheDTO = SubIdCache.getServiceKey(subId);

    if (subIdCacheDTO == null) {
      logger.info("The subId {} get Cache is null", walletAddress);
      throw new JsonRpc2ServerErrorException(
          -32000,
          null,
          "The subId get Cache is null",
          null
      );
    }

    GatewayHomechainKeyManage gatewayHomechainKeyManage = homeChainKeyManageService.selectByWalletAddress(
        walletAddress
    );

    // check walletAddress
    if (gatewayHomechainKeyManage == null) {
      logger.info("The walletAddress {} is not adapted", walletAddress);
      throw new JsonRpc2ServerErrorException(
          -32000,
          null,
          "The walletAddress is not adapted",
          null
      );
    }

    OracleWrapAddConsumerBodyDTO wrapAddConsumerBodyDTO = new OracleWrapAddConsumerBodyDTO();
    wrapAddConsumerBodyDTO.setConsumerAddress(walletAddress);
    wrapAddConsumerBodyDTO.setSubId(subId);
    wrapAddConsumerBodyDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());
    wrapAddConsumerBodyDTO.setWalletPublicKey(subIdCacheDTO.getWalletPubKey());
    // wrap body
    WarpFlareAddConsumerBodyRespDTO warpFlareAddConsumerBodyRespDTO = subscriptionHandler.wrapBody(
        wrapAddConsumerBodyDTO
    );

    // get wallet PrivateKey
    String walletPrivateKey = subIdCacheDTO.getWalletPrivKey();

    OracleAddConsumerSignatureDTO addConsumerSignatureDTO = new OracleAddConsumerSignatureDTO();
    addConsumerSignatureDTO.setChainId(warpFlareAddConsumerBodyRespDTO.getChainId());
    addConsumerSignatureDTO.setRawTransaction(warpFlareAddConsumerBodyRespDTO.getRawTransaction());
    addConsumerSignatureDTO.setVnWalletPrivateKey(walletPrivateKey);
    // signature
    OracleAddConsumerSignatureResDTO signature = subscriptionHandler.signature(
        addConsumerSignatureDTO);

    // Assemble the send message parameter
    OracleAddConsumerMetaDataDTO metaDataDTO = new OracleAddConsumerMetaDataDTO();
    metaDataDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());

    OracleAddConsumerSignatureResDTO signatureResDTO = new OracleAddConsumerSignatureResDTO();
    signatureResDTO.setSignature(signature.getSignature());

    OracleAddConsumerSendDTO sendDTO = new OracleAddConsumerSendDTO();
    sendDTO.setMetaData(metaDataDTO);
    sendDTO.setSignatureResDTO(signatureResDTO);
    sendDTO.setRequestClientBody(request);

    // send message
    JsonRpc2Response respResult = subscriptionHandler.sendMessage(sendDTO);

    JSONObject respResultJson = JSONObject.parseObject(
        JSONObject.toJSONString(respResult.getResult()));

    if (respResultJson.get("failReason") != null
        && !"".equals(respResultJson.get("failReason"))) {

      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getCode(),
          "VN not return a hash",
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getMessage(),
          null);
    }

    String transactionHash = respResultJson.getString("transactionHash");

    SubscriptionConsumer subscriptionConsumer = new SubscriptionConsumer();
    subscriptionConsumer.setConsumerAddress(walletAddress);
    subscriptionConsumer.setSubId(subId);
    subscriptionConsumer.setLastTxHash(transactionHash);
    subscriptionConsumer.setConsumerStatus(ConsumerStatusEnum.BINDING.getCode());

    consumerDataService.updateStatus(subscriptionConsumer);

    OracleSubConsumerAddMethodResResDTO resDTO = new OracleSubConsumerAddMethodResResDTO();
    resDTO.setTransactionHash(transactionHash);

    return resDTO;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    Object walletAddress = request.getParams().get("walletAddress");
    if (walletAddress == null || StringUtils.isBlank(String.valueOf(walletAddress))) {
      return "walletAddress is empty";
    }
    return null;
  }


}
