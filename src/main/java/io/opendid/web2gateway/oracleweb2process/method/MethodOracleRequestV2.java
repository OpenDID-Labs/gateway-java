package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestV2MetaDataDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.mapper.OracleRequestV2RecordMapper;
import io.opendid.web2gateway.repository.model.OracleRequestV2RecordWithBLOBs;
import io.opendid.web2gateway.security.checkaspect.MethodOracle;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component(Web2MethodName.ORACLE_REQUEST_V2 + Web2Method.BEAN_SUFFIX)
@MethodOracle
public class MethodOracleRequestV2 implements Web2Method {

    private Logger logger = LoggerFactory.getLogger(MethodOracleRequestV2.class);

    @Autowired
    private VnGatewayClient vnGatewayClient;

    @Autowired
    private OracleRequestV2RecordMapper oracleRequestV2RecordMapper;

    @Value("${oracle.callBack.url}")
    private String callBackUrl;

    @Override
    public Object process(JsonRpc2Request request) throws Exception {

        LinkedHashMap params = request.getParams();
        String jobId = MapUtils.getString(params, "jobId");
        String vnCode = MapUtils.getString(params, "vnCode");
        String data = MapUtils.getString(params, "data");

        OracleRequestV2MetaDataDTO metadata = new OracleRequestV2MetaDataDTO();
        metadata.setVnCode(vnCode);
        metadata.setJobId(jobId);
        metadata.setData(data);
        metadata.setCallbackUrl(callBackUrl);
        LinkedHashMap v2Params = new LinkedHashMap();
        v2Params.put("metadata", metadata);

        JsonRpc2Request v2Request = new JsonRpc2Request(
                request.getId(),
                Web2MethodName.ORACLE_REQUEST_V2,
                v2Params,
                "2.0"
        );

        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setJobId(jobId);
        vnClientJobIdDTO.setVnCode(vnCode);
        vnClientJobIdDTO.setRequestBody(v2Request);

        logger.info("MethodOracleRequestV2 call VN, vnCode={}, jobId={}", vnCode, jobId);
        JsonRpc2Response respResult = vnGatewayClient.requestJobSend(vnClientJobIdDTO);

        if (respResult == null) {
            logger.error("MethodOracleRequestV2 VN call returned null, vnCode={}, jobId={}", vnCode, jobId);
            throw new JsonRpc2ServerErrorException(
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
                    MDC.get(LogTraceIdConstant.TRACE_ID),
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
                    "VN call returned null");
        }

        Object resultObj = respResult.getResult();
        JSONObject resultJson = JSONObject.parseObject(JSONObject.toJSONString(resultObj));
        String requestId = resultJson.getString("requestId");

        OracleRequestV2RecordWithBLOBs record = new OracleRequestV2RecordWithBLOBs();
        record.setRequestId(requestId);
        record.setVnCode(vnCode);
        record.setJobId(jobId);
        record.setProcessStatus(ProcessStatusEnum.PENDING.getCode());
        record.setTraceId(MDC.get(LogTraceIdConstant.TRACE_ID));
        record.setRequestBody(JSON.toJSONString(request));
        record.setCreateDate(new Date());
        record.setUpdateDate(new Date());

        oracleRequestV2RecordMapper.insertSelective(record);
        logger.info("MethodOracleRequestV2 saved V2 record, requestId={}, vnCode={}", requestId, vnCode);

        return resultObj;
    }

    @Override
    public String checkParams(JsonRpc2Request request) {

        LinkedHashMap params = request.getParams();
        if (params == null) {
            return "params is empty";
        }

        String vnCode = MapUtils.getString(params, "vnCode");
        if (StringUtils.isBlank(vnCode)) {
            return "vnCode is empty";
        }

        Map<String, Object> data = MapUtils.getMap(params, "data");
        if (data == null) {
            return "data is empty";
        }

        return null;
    }
}
