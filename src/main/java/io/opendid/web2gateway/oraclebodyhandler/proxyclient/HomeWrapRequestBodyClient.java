package io.opendid.web2gateway.oraclebodyhandler.proxyclient;

import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.WarpAptosReqBodyRequestDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class HomeWrapRequestBodyClient {

    private Logger logger = LoggerFactory.getLogger(HomeWrapRequestBodyClient.class);

    @Autowired
    private VnGatewayClient vnGatewayClient;

    public JsonRpc2Response getWarpReqBody(WarpAptosReqBodyRequestDTO requestDTO) throws Exception {


        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setJobId(requestDTO.getJobId());
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("jobId",requestDTO.getJobId());
        linkedHashMap.put("publicKey",requestDTO.getPublicKey());
        linkedHashMap.put("nonce",requestDTO.getNonce());
        linkedHashMap.put("data",requestDTO.getData());
        linkedHashMap.put("generateClaim",requestDTO.getGenerateClaim());
        JsonRpc2Request oracleWrapRequestBody = new JsonRpc2Request(1L, "oracle_wrap_request_body", linkedHashMap, "");
        vnClientJobIdDTO.setRequestBody(oracleWrapRequestBody);
        vnClientJobIdDTO.setVnCode(requestDTO.getVnCode());

        JsonRpc2Response request = vnGatewayClient.request(vnClientJobIdDTO);
        if (request == null){
            logger.error("WarpRequestBodyService request vn gateway return null");
            throw new JsonRpc2ServerErrorException(
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
                    MDC.get(LogTraceIdConstant.TRACE_ID),
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
                    "Method getWarpReqBody call vn result is null");
        }

        return request;

    }
}
