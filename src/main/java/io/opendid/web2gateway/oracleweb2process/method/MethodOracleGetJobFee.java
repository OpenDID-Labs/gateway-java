package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.JobIdFeeResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.ORACLE_GET_JOB_FEE+ Web2Method.BEAN_SUFFIX)
@MethodPrivate
public class MethodOracleGetJobFee implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodOracleGetJobFee.class);
  @Resource
  private VnGatewayClient vnGatewayClient;

  @Override
  public Object process(JsonRpc2Request request) throws JsonRpc2ServerErrorException {

    // build params
    VnClientJobIdDTO dto = new VnClientJobIdDTO();
    dto.setJobId(request.getParams().get("jobId").toString());
    dto.setRequestBody(request);
    // request vn gateway
    JsonRpc2Response response = vnGatewayClient.request(dto);
    if (response != null) {
      JSONObject respResultJson = JSONObject.parseObject(
              JSONObject.toJSONString(response));
      return respResultJson.getObject("result", JobIdFeeResponseDTO.class);
    } else {
      logger.error("MethodOracleGetJobFee request vn gateway return null");
      throw new RuntimeException();
    }
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    Object jobId = request.getParams().get("jobId");
    if (jobId == null || StringUtils.isBlank(String.valueOf(jobId))) {
      return " jobId is empty";
    }

    return null;
  }
}
