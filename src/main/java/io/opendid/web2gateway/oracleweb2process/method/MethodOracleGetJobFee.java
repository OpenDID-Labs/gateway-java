package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.ClaimFeeResponseDTO;
import io.opendid.web2gateway.model.dto.oracle.JobIdFeeResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import io.opendid.web2gateway.security.checkaspect.MethodOracle;
import io.opendid.web2gateway.service.VngatewayJobidMappingService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component(Web2MethodName.ORACLE_GET_JOB_FEE+ Web2Method.BEAN_SUFFIX)
@MethodOracle
public class MethodOracleGetJobFee implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodOracleGetJobFee.class);
  @Resource
  private VnGatewayClient vnGatewayClient;
  @Autowired
  private VngatewayJobidMappingService vngatewayJobidMappingService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    //
    ClaimFeeResponseDTO claimFeeDTO = getClaimFee(request);

    // build params
    VnClientJobIdDTO dto = new VnClientJobIdDTO();
    dto.setJobId(request.getParams().get("jobId").toString());
    dto.setRequestBody(request);
    // request vn gateway
    JsonRpc2Response response = vnGatewayClient.requestJobSend(dto);
    if (response != null) {
      JSONObject respResultJson = JSONObject.parseObject(
              JSONObject.toJSONString(response));
      JobIdFeeResponseDTO result = respResultJson.getObject("result", JobIdFeeResponseDTO.class);
      result.setClaimFee(claimFeeDTO.getGasAmount());
      return result;
    } else {
      logger.error("MethodOracleGetJobFee request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
          "Method getJobIdFee call vn result is null");
    }
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    Object jobId = request.getParams().get("jobId");
    if (jobId == null || StringUtils.isBlank(String.valueOf(jobId))) {
      return " jobId is empty";
    }

    List<VngatewayJobidMapping> vngatewayJobidMappings = vngatewayJobidMappingService.searchByJobId(jobId.toString());
    if (vngatewayJobidMappings.isEmpty()){
      return "jobId does not exist";
    }

    return null;
  }


  private ClaimFeeResponseDTO getClaimFee(JsonRpc2Request request) throws Exception {

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        request.getId(),
        "oracle_get_claim_fee",
        new LinkedHashMap(),
        ""
    );
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);
    vnClientJobIdDTO.setVnCode(vnClientJobIdDTO.getVnCode());

    JsonRpc2Response response = vnGatewayClient.requestJobSend(vnClientJobIdDTO);

    if (response != null) {
      JSONObject respResultJson = JSONObject.parseObject(
          JSONObject.toJSONString(response));
      return respResultJson.getObject("result", ClaimFeeResponseDTO.class);
    } else {
      logger.error("MethodOracleGetJobFee getClaimFee request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
          "Method getJobIdFee call vn result is null");
    }

  }

}
