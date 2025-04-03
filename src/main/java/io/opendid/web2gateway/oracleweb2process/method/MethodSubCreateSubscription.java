package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.catches.ChainSignKeyVaultCache;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.dto.oracle.subscription.*;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainCreateSubscriptionFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainCreateSubscriptionInterface;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.repository.model.SubscriptionInfo;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import io.opendid.web2gateway.service.HomeChainKeyManageService;
import io.opendid.web2gateway.service.SelectVnListService;
import io.opendid.web2gateway.service.SubIdCacheService;
import io.opendid.web2gateway.service.SubscriptionInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;

@Component(Web2MethodName.SUB_CREATE_SUBSCRIPTION + Web2Method.BEAN_SUFFIX)
@MethodSubscription
public class MethodSubCreateSubscription implements Web2SubscriptionMethod {

  private static final Logger logger = LoggerFactory.getLogger(MethodSubCreateSubscription.class);

  @Resource
  private SelectVnListService vnListService;

  @Resource
  private HomeChainKeyManageService homeChainKeyManageService;

  @Resource
  private SubscriptionInfoService subscriptionInfoService;

  @Resource
  private SubIdCacheService subIdCacheService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    if (request.getParams() == null) {
       return new OracleRequestCancelRespDTO();
    }

    HomeChainCreateSubscriptionInterface subscriptionHandler = HomeChainCreateSubscriptionFactory.createSubscriptionHandler();

    // select key manage
    String walletAddress = request.getParams().get("walletAddress").toString();
    GatewayHomechainKeyManage gatewayHomechainKeyManage =
            homeChainKeyManageService.selectByWalletAddress(walletAddress);

    // check walletAddress
    if (gatewayHomechainKeyManage == null) {
      logger.info("The walletAddress {} is not adapted", walletAddress);
      throw new JsonRpc2ServerErrorException(JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getCode(), null, "The walletAddress is not adapted", null);
    }

    // wrap body
    OracleWrapSubscriptionBodyDTO oracleWrapSubscriptionBodyDTO = new OracleWrapSubscriptionBodyDTO();
    oracleWrapSubscriptionBodyDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());
    oracleWrapSubscriptionBodyDTO.setWalletPublicKey(gatewayHomechainKeyManage.getWalletPublicKey());

    OracleWrapSubscriptionBodyResDTO resDTO = subscriptionHandler.wrapBody(oracleWrapSubscriptionBodyDTO);

    // signature
    ChainKeyDTO chainKeyByKeyCode = ChainSignKeyVaultCache.getChainKeyByKeyCode(gatewayHomechainKeyManage.getKeyCode());
    String walletPrivateKey=chainKeyByKeyCode.getPrivateKey();

    OracleSubscriptionSignatureDTO oracleSubscriptionSignatureDTO = new OracleSubscriptionSignatureDTO();
    oracleSubscriptionSignatureDTO.setWrapData(resDTO.getWrapData());
    oracleSubscriptionSignatureDTO.setVnWalletPrivateKey(walletPrivateKey);
    OracleSubscriptionSignatureResDTO signature = subscriptionHandler.signature(oracleSubscriptionSignatureDTO);

    // send message
    OracleSubscriptionMetaDataDTO metaDataDTO = new OracleSubscriptionMetaDataDTO();
    metaDataDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());

    OracleSubscriptionSignatureResDTO signatureResDTO = new OracleSubscriptionSignatureResDTO();
    signatureResDTO.setSignature(signature.getSignature());

    OracleSubscriptionSendDTO sendDTO = new OracleSubscriptionSendDTO();
    sendDTO.setMetaData(metaDataDTO);
    sendDTO.setSignatureResDTO(signatureResDTO);
    sendDTO.setRequestClientBody(request);
    JsonRpc2Response jsonRpc2Response = subscriptionHandler.sendMessage(sendDTO);
    OracleSubscriptionCreateRespDTO oracleSubscriptionCreateRespDTO = JSONObject.parseObject(JSONObject.toJSONString(jsonRpc2Response.getResult()),
            OracleSubscriptionCreateRespDTO.class);
    logger.info("MethodSubCreateSubscription request vn response: {}",JSONObject.toJSONString(oracleSubscriptionCreateRespDTO));
    // insert db
    SubscriptionInfo subscriptionInfo = new SubscriptionInfo();
    subscriptionInfo.setSubId(oracleSubscriptionCreateRespDTO.getSubId());
    subscriptionInfo.setOwnerAddress(walletAddress);
    subscriptionInfo.setBalance("0");
    subscriptionInfo.setCreateDate(new Date());
    subscriptionInfoService.insertSubInfo(subscriptionInfo);

    // cache subId
    subIdCacheService.subIdToCache(oracleSubscriptionCreateRespDTO.getSubId());

    OracleSubscriptionCreateMethodResDTO methodResDTO = new OracleSubscriptionCreateMethodResDTO();
    methodResDTO.setSubId(oracleSubscriptionCreateRespDTO.getSubId());
    methodResDTO.setTransactionHash(oracleSubscriptionCreateRespDTO.getTransactionHash());
    return methodResDTO;
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
