package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.catches.ChainSignKeyVaultCache;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubSubTokenTransferMethodResResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapSubTokenTransferBodyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareSubTokenTransferBodyRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainSubTokenTransferFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainSubTokenTransferInterface;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.repository.model.SubscriptionTransferTokenRecord;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import io.opendid.web2gateway.service.HomeChainKeyManageService;
import io.opendid.web2gateway.service.SubTransferTokenDataService;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.SUB_TOKEN_TRANSFER + Web2Method.BEAN_SUFFIX)
@MethodSubscription
public class MethodSubTokenTransfer implements Web2SubscriptionMethod {

  private static final Logger logger = LoggerFactory.getLogger(MethodSubTokenTransfer.class);

  @Resource
  private HomeChainKeyManageService homeChainKeyManageService;
  @Autowired
  private SubTransferTokenDataService subscriptionDataService;;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    HomeChainSubTokenTransferInterface subscriptionHandler = HomeChainSubTokenTransferFactory.createSubscriptionHandler();

    // select key manage
    String walletAddress = request.getParams().get("walletAddress").toString();
    String subId = request.getParams().get("subId").toString();
    BigInteger amount = new BigInteger(request.getParams().get("amount").toString());

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

    OracleWrapSubTokenTransferBodyDTO subTokenTransferBodyDTO = new OracleWrapSubTokenTransferBodyDTO();
    subTokenTransferBodyDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());
    subTokenTransferBodyDTO.setWalletPublicKey(gatewayHomechainKeyManage.getWalletPublicKey());
    subTokenTransferBodyDTO.setSubId(subId);
    subTokenTransferBodyDTO.setWalletAddress(walletAddress);
    subTokenTransferBodyDTO.setAmount(amount);

    // wrap body
    WarpFlareSubTokenTransferBodyRespDTO warpFlareSubTokenTransferBodyRespDTO = subscriptionHandler.wrapBody(
        subTokenTransferBodyDTO
    );

    // get wallet PrivateKey
    ChainKeyDTO chainKeyByKeyCode = ChainSignKeyVaultCache.getChainKeyByKeyCode(
        gatewayHomechainKeyManage.getKeyCode()
    );
    String walletPrivateKey = chainKeyByKeyCode.getPrivateKey();

    OracleSubTokenTransferSignatureDTO oracleSubTokenTransferSignatureDTO = new OracleSubTokenTransferSignatureDTO();
    oracleSubTokenTransferSignatureDTO.setChainId(warpFlareSubTokenTransferBodyRespDTO.getChainId());
    oracleSubTokenTransferSignatureDTO.setRawTransaction(warpFlareSubTokenTransferBodyRespDTO.getRawTransaction());
    oracleSubTokenTransferSignatureDTO.setVnWalletPrivateKey(walletPrivateKey);

    OracleSubTokenTransferSignatureResDTO signature = subscriptionHandler.signature(
        oracleSubTokenTransferSignatureDTO
    );

    // Assemble the send message parameter
    OracleSubTokenTransferMetaDataDTO metaDataDTO = new OracleSubTokenTransferMetaDataDTO();
    metaDataDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());

    OracleSubTokenTransferSignatureResDTO signatureResDTO = new OracleSubTokenTransferSignatureResDTO();
    signatureResDTO.setSignature(signature.getSignature());

    OracleSubTokenTransferSendDTO sendDTO = new OracleSubTokenTransferSendDTO();
    sendDTO.setMetaData(metaDataDTO);
    sendDTO.setSignatureResDTO(signatureResDTO);
    sendDTO.setRequestClientBody(request);

    // send message
    JsonRpc2Response respResult = subscriptionHandler.sendMessage(sendDTO);

    JSONObject respResultJson = JSONObject.parseObject(
        JSONObject.toJSONString(respResult.getResult())
    );

    if (respResultJson.get("failReason") != null
        && !"".equals(respResultJson.get("failReason"))) {

      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getCode(),
          "VN not return a hash",
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getMessage(),
          null);
    }

    String transactionHash = respResultJson.getString("transactionHash");

    SubscriptionTransferTokenRecord transferTokenRecord = new SubscriptionTransferTokenRecord();
    transferTokenRecord.setSubId(subId);
    transferTokenRecord.setRequestVnCode(gatewayHomechainKeyManage.getVnCode());
    transferTokenRecord.setSignAddress(walletAddress);
    transferTokenRecord.setSignKeyCode(gatewayHomechainKeyManage.getKeyCode());
    transferTokenRecord.setCreateDate(new Date());
    transferTokenRecord.setTransactionHash(transactionHash);
    transferTokenRecord.setFromAddress(walletAddress);
    transferTokenRecord.setTokenAmounts(new BigDecimal(amount));
    transferTokenRecord.setToAddress(warpFlareSubTokenTransferBodyRespDTO.getContractAddress());

    subscriptionDataService.insertSelective(transferTokenRecord);

    OracleSubSubTokenTransferMethodResResDTO resDTO = new OracleSubSubTokenTransferMethodResResDTO();
    resDTO.setTransactionHash(transactionHash);

    return resDTO;
  }


  @Override
  public String checkParams(JsonRpc2Request request) {

    return null;
  }

}
