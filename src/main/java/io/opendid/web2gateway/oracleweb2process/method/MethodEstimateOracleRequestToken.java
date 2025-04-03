package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.model.jsonrpc2.method.OracleResultMethodRes;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainRequestBodyFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestBodyInterface;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapRequestBodyClient;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import io.opendid.web2gateway.service.JobidMappingDataService;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.GET_METHOD_ESTIMATE_ORACLE_REQUEST_TOKEN + Web2Method.BEAN_SUFFIX)
@MethodSubscription
public class MethodEstimateOracleRequestToken implements Web2Method {

    private Logger logger = LoggerFactory.getLogger(MethodEstimateOracleRequestToken.class);

    @Autowired
    private VnGatewayClient vnGatewayClient;
    @Autowired
    private JobidMappingDataService jobidMappingDataService;

    @Override
    public Object process(JsonRpc2Request request) throws Exception {

        HomeChainRequestBodyInterface homeChainRequestHandler =
            HomeChainRequestBodyFactory.createRequestBodyHandler();
        if (request.getParams() == null) {
            return new OracleResultMethodRes();
        }

        LinkedHashMap params = request.getParams();
        String jobId = MapUtils.getString(params, "jobId");
        Integer ovnCount = MapUtils.getInteger(params, "ovnCount");
        boolean generateClaim = MapUtils.getBoolean(params, "generateClaim");

        List<VngatewayJobidMapping> vngatewayJobidMappings = jobidMappingDataService.selectByJobId(jobId);

        if(vngatewayJobidMappings != null && vngatewayJobidMappings.size() > 0){

            VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
            vnClientJobIdDTO.setJobId(jobId);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("jobId",jobId);
            linkedHashMap.put("ovnCount",ovnCount);
            linkedHashMap.put("generateClaim",generateClaim);

            JsonRpc2Request oracleWrapRequestBody = new JsonRpc2Request(1L, "get_method_estimate_oracle_request_token", linkedHashMap, "");

            vnClientJobIdDTO.setRequestBody(oracleWrapRequestBody);
            vnClientJobIdDTO.setVnCode(vngatewayJobidMappings.get(0).getVnCode());

            JsonRpc2Response response = vnGatewayClient.requestSubscriptionSend(vnClientJobIdDTO);

            if (response == null){
                logger.error("WarpRequestBodyService request vn gateway return null");
                throw new JsonRpc2ServerErrorException(
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
                    MDC.get(LogTraceIdConstant.TRACE_ID),
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
                    "Method Estimate Oracle Token call vn result is null");
            }

            return response.getResult();

        }

        return null;
    }

    @Override
    public String checkParams(JsonRpc2Request request) {

        Object jobId = request.getParams().get("jobId");
        if (jobId == null || StringUtils.isBlank(String.valueOf(jobId))) {
            return " jobId is empty";
        }
        Object vnCount = request.getParams().get("ovnCount");
        if (vnCount == null) {
            return " ovnCount is empty";
        }
        Object generateClaim = request.getParams().get("generateClaim");
        if (generateClaim == null) {
            return " generateClaim is empty";
        }
        return null;
    }
}
