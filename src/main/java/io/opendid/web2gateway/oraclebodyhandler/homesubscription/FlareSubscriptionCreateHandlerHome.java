package io.opendid.web2gateway.oraclebodyhandler.homesubscription;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.subscription.*;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainCreateSubscriptionInterface;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapSubscriptionBodyClient;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.util.LinkedHashMap;

@Component(HomeChainName.FLARE + HomeChainCreateSubscriptionInterface.BEAN_SUFFIX)
public class FlareSubscriptionCreateHandlerHome implements HomeChainCreateSubscriptionInterface {

    private static final Logger logger = LoggerFactory.getLogger(FlareSubscriptionCreateHandlerHome.class);

    @Resource
    private HomeWrapSubscriptionBodyClient wrapSubscriptionBodyClient;

    @Resource
    private VnGatewayClient vnGatewayClient;

    @Override
    public OracleWrapSubscriptionBodyResDTO wrapBody(OracleWrapSubscriptionBodyDTO oracleWrapSubscriptionBodyDTO) throws Exception {

        String  homeChainPublicKey = oracleWrapSubscriptionBodyDTO.getWalletPublicKey();
        SubscriptionWrapBodyReqDTO reqDTO = new SubscriptionWrapBodyReqDTO();
        reqDTO.setPublicKey(homeChainPublicKey);
        reqDTO.setVnCode(oracleWrapSubscriptionBodyDTO.getVnCode());
        JsonRpc2Response request = wrapSubscriptionBodyClient.getSubscriptionWarpReqBody(reqDTO);
        WarpFlareSubCreateBodyRespDTO respDTO =
                JSONObject.parseObject(request.getResult().toString(), WarpFlareSubCreateBodyRespDTO.class);

        logger.info("Subscription FlareChainMsg process warpResponse={}", JSON.toJSONString(respDTO));

        OracleWrapSubscriptionBodyResDTO resDTO = new OracleWrapSubscriptionBodyResDTO();
        resDTO.setWrapData(respDTO);
        return resDTO;
    }

    @Override
    public OracleSubscriptionSignatureResDTO signature(OracleSubscriptionSignatureDTO oracleSubscriptionSignatureDTO) throws Exception {

        // 1,Get private key
        String walletPrivateKey = oracleSubscriptionSignatureDTO.getVnWalletPrivateKey();

        // 2,Get wait signature data
        WarpFlareSubCreateBodyRespDTO wrapData = oracleSubscriptionSignatureDTO.getWrapData();

        // 3,Create Credentials
        Credentials credentials = Credentials.create(walletPrivateKey);
        byte[] signedMessage;
        RawTransaction rawTransaction = wrapData.getRawTransaction();
        Long chainId = wrapData.getChainId();

        // 4, Sign
        if (chainId != null) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String signatureStr = Numeric.toHexString(signedMessage);

        // 5, Return signature string
        OracleSubscriptionSignatureResDTO signatureResDTO = new OracleSubscriptionSignatureResDTO();
        signatureResDTO.setSignature(signatureStr);

        return signatureResDTO;
    }

    @Override
    public JsonRpc2Response sendMessage(OracleSubscriptionSendDTO oracleSubscriptionSendDTO) throws Exception {

        OracleSubscriptionMetaDataDTO metaData = oracleSubscriptionSendDTO.getMetaData();

        JsonRpc2Request requestClientBody = oracleSubscriptionSendDTO.getRequestClientBody();

        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("signedTx", oracleSubscriptionSendDTO.getSignatureResDTO().getSignature().toString());
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
            logger.error("FlareChain CreateSubscription request vn gateway return null");
            throw new JsonRpc2ServerErrorException(
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
                    MDC.get(LogTraceIdConstant.TRACE_ID),
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
                    "Method CreateSubscription call vn result is null");
        }

        logger.info("CreateSubscription Method process send result={}",JSONObject.toJSONString(respResult.getResult()));

        return respResult;
    }
}
