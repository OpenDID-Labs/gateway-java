package io.opendid.web2gateway.oraclebodyhandler.proxyclient;

import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.model.dto.oracle.subscription.ConsumerWrapBodyReqDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubTokenTransferInfoReqDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubTokenTransferWrapBodyReqDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import java.util.LinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomeWrapSubTokenTransferBodyClient {

    private static final Logger logger = LoggerFactory.getLogger(HomeWrapSubTokenTransferBodyClient.class);

    @Autowired
    private VnGatewayClient vnGatewayClient;

    public JsonRpc2Response getSubTokenTransferReqBody(SubTokenTransferWrapBodyReqDTO reqDTO)
            throws Exception {

        // Construct a set of basic request parameters
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("publicKey",reqDTO.getPublicKey());
        linkedHashMap.put("subId",reqDTO.getSubId());
        linkedHashMap.put("walletAddress",reqDTO.getWalletAddress());
        linkedHashMap.put("amount",reqDTO.getAmount());
        JsonRpc2Request oracleWrapRequestBody = new JsonRpc2Request(1L, "sub_wrap_token_transfer", linkedHashMap, "");

        // Encapsulate gateway request parameter object
        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setRequestBody(oracleWrapRequestBody);
        vnClientJobIdDTO.setVnCode(reqDTO.getVnCode());

        // Send request
        JsonRpc2Response request = vnGatewayClient.requestSubscriptionSend(vnClientJobIdDTO);

        return request;
    }

    public JsonRpc2Response getTokenTransferInfo(SubTokenTransferInfoReqDTO reqDTO)
        throws Exception {

        // Construct a set of basic request parameters
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("transactionHash",reqDTO.getTransactionHash());
        JsonRpc2Request oracleWrapRequestBody = new JsonRpc2Request(1L, "get_token_transfer_info", linkedHashMap, "");

        // Encapsulate gateway request parameter object
        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setRequestBody(oracleWrapRequestBody);
        vnClientJobIdDTO.setVnCode(reqDTO.getVnCode());

        // Send request
        JsonRpc2Response request = vnGatewayClient.requestSubscriptionSend(vnClientJobIdDTO);

        return request;
    }

}
