package io.opendid.web2gateway.oraclebodyhandler.proxyclient;

import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubscriptionWrapBodyReqDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class HomeWrapSubscriptionBodyClient {

    private static final Logger logger = LoggerFactory.getLogger(HomeWrapSubscriptionBodyClient.class);

    @Autowired
    private VnGatewayClient vnGatewayClient;

    public JsonRpc2Response getSubscriptionWarpReqBody(SubscriptionWrapBodyReqDTO reqDTO)
            throws Exception {

        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("publicKey",reqDTO.getPublicKey());
        JsonRpc2Request oracleWrapRequestBody = new JsonRpc2Request(1L, "sub_wrap_create_body", linkedHashMap, "");
        vnClientJobIdDTO.setRequestBody(oracleWrapRequestBody);
        vnClientJobIdDTO.setVnCode(reqDTO.getVnCode());
        JsonRpc2Response request = vnGatewayClient.requestSubscriptionSend(vnClientJobIdDTO);

        return request;

    }
}
