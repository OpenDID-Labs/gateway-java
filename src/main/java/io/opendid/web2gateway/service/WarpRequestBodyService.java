package io.opendid.web2gateway.service;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.WarpReqBodyRequestDTO;
import io.opendid.web2gateway.model.dto.oracle.WarpReqBodyResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;


/**
*  WarpRequestBodyService
* @author Dong-Jianguo
* @Date: 2024/12/17
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Service
public class WarpRequestBodyService {

  @Autowired
  private VnGatewayClient vnGatewayClient;

  public WarpReqBodyResponseDTO getWarpReqBody(WarpReqBodyRequestDTO requestDTO) throws JsonRpc2ServerErrorException {


    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setJobId(requestDTO.getJobId());
    LinkedHashMap linkedHashMap = new LinkedHashMap();
    linkedHashMap.put("jobId",requestDTO.getJobId());
    linkedHashMap.put("publicKey",requestDTO.getPublicKey());
    linkedHashMap.put("nonce",requestDTO.getNonce());
    linkedHashMap.put("data",requestDTO.getData());
    JsonRpc2Request oracleWrapRequestBody = new JsonRpc2Request(1L, "oracle_wrap_request_body", linkedHashMap, "");
    vnClientJobIdDTO.setRequestBody(oracleWrapRequestBody);
    JsonRpc2Response request = vnGatewayClient.request(vnClientJobIdDTO);
    WarpReqBodyResponseDTO responseDTO =
        JSONObject.parseObject(request.getResult().toString(),WarpReqBodyResponseDTO.class);
    return responseDTO;

  }


}
