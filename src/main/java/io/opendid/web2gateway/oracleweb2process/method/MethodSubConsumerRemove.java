package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.catches.SubIdCache;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.ConsumerStatusEnum;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareRemoveConsumerBodyRespDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubConsumerRemoveMethodResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapRemoveConsumerBodyDTO;
import io.opendid.web2gateway.model.dto.subscription.SubIdCacheDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainRemoveConsumerFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRemoveConsumerInterface;
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

@Component(Web2MethodName.SUB_REMOVE_CONSUMER + Web2Method.BEAN_SUFFIX)
@MethodSubscription
public class MethodSubConsumerRemove implements Web2SubscriptionMethod {

  private static final Logger logger = LoggerFactory.getLogger(MethodSubConsumerRemove.class);
  @Resource
  private HomeChainKeyManageService homeChainKeyManageService;
  @Autowired
  private ConsumerDataService consumerDataService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    HomeChainRemoveConsumerInterface subscriptionHandler = HomeChainRemoveConsumerFactory.createSubscriptionHandler();

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

    OracleWrapRemoveConsumerBodyDTO wrapRemoveConsumerBodyDTO = new OracleWrapRemoveConsumerBodyDTO();
    wrapRemoveConsumerBodyDTO.setConsumerAddress(walletAddress);
    wrapRemoveConsumerBodyDTO.setSubId(subId);
    wrapRemoveConsumerBodyDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());
    wrapRemoveConsumerBodyDTO.setWalletPublicKey(subIdCacheDTO.getWalletPubKey());
    // wrap body
    WarpFlareRemoveConsumerBodyRespDTO warpFlareRemoveConsumerBodyRespDTO = subscriptionHandler.wrapBody(
        wrapRemoveConsumerBodyDTO
    );

    // get wallet PrivateKey
    String walletPrivateKey = subIdCacheDTO.getWalletPrivKey();

    OracleRemoveConsumerSignatureDTO removeConsumerSignatureDTO = new OracleRemoveConsumerSignatureDTO();
    removeConsumerSignatureDTO.setChainId(warpFlareRemoveConsumerBodyRespDTO.getChainId());
    removeConsumerSignatureDTO.setRawTransaction(warpFlareRemoveConsumerBodyRespDTO.getRawTransaction());
    removeConsumerSignatureDTO.setVnWalletPrivateKey(walletPrivateKey);
    // signature
    OracleRemoveConsumerSignatureResDTO signature = subscriptionHandler.signature(
        removeConsumerSignatureDTO
    );


    // Assemble the send message parameter
    OracleRemoveConsumerMetaDataDTO metaDataDTO = new OracleRemoveConsumerMetaDataDTO();
    metaDataDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());

    OracleRemoveConsumerSignatureResDTO signatureResDTO = new OracleRemoveConsumerSignatureResDTO();
    signatureResDTO.setSignature(signature.getSignature());

    OracleRemoveConsumerSendDTO sendDTO = new OracleRemoveConsumerSendDTO();
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
    subscriptionConsumer.setConsumerStatus(ConsumerStatusEnum.UNBOUND.getCode());

    consumerDataService.updateStatus(subscriptionConsumer);

    OracleSubConsumerRemoveMethodResDTO resDTO = new OracleSubConsumerRemoveMethodResDTO();
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
