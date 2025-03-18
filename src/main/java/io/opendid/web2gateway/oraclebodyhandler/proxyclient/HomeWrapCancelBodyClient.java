package io.opendid.web2gateway.oraclebodyhandler.proxyclient;

import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.model.dto.oracle.CancelWarpReqBodyRequestDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class HomeWrapCancelBodyClient {

    private Logger logger = LoggerFactory.getLogger(HomeWrapCancelBodyClient.class);

    @Autowired
    private VnGatewayClient vnGatewayClient;

    public JsonRpc2Response getCancelWarpReqBody(CancelWarpReqBodyRequestDTO requestDTO)
            throws Exception {


        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setJobId(requestDTO.getJobId());
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("publicKey",requestDTO.getPublicKey());
        linkedHashMap.put("requestId",requestDTO.getRequestId());
        JsonRpc2Request oracleWrapRequestBody = new JsonRpc2Request(1L, "request_wrap_cancel_request_body", linkedHashMap, "");
        vnClientJobIdDTO.setRequestBody(oracleWrapRequestBody);
        vnClientJobIdDTO.setVnCode(requestDTO.getVnCode());

        JsonRpc2Response request = vnGatewayClient.request(vnClientJobIdDTO);

        return request;

    }
}
