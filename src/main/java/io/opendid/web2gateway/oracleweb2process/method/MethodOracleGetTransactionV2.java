package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.OracleResultV2ResDTO;
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
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;

@Component(Web2MethodName.ORACLE_GET_TRANSACTION_V2 + Web2Method.BEAN_SUFFIX)
@MethodOracle
public class MethodOracleGetTransactionV2 implements Web2Method {

    private Logger logger = LoggerFactory.getLogger(MethodOracleGetTransactionV2.class);

    private static final String ORACLE_RESULT_V2_METHOD = "oracle_result_v2";

    @Autowired
    private VnGatewayClient vnGatewayClient;

    @Autowired
    private OracleRequestV2RecordMapper oracleRequestV2RecordMapper;

    @Override
    public Object process(JsonRpc2Request request) throws Exception {

        String requestId = MapUtils.getString(request.getParams(), "requestId");

        OracleRequestV2RecordWithBLOBs existingRecord =
                oracleRequestV2RecordMapper.selectByRequestId(requestId);

        if (existingRecord == null) {
            logger.error("MethodOracleGetTransactionV2 no pending request found, requestId={}", requestId);
            throw new JsonRpc2ServerErrorException(
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getCode(),
                    MDC.get(LogTraceIdConstant.TRACE_ID),
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getMessage(),
                    "No pending oracle request found for requestId: " + requestId);
        }

        if (existingRecord.getProcessStatus() != null
                && existingRecord.getProcessStatus().equals(ProcessStatusEnum.PROCESSED.getCode())) {
            logger.info("MethodOracleGetTransactionV2 cache hit, requestId={}", requestId);
            OracleResultV2ResDTO cached = new OracleResultV2ResDTO();
            cached.setRequestId(existingRecord.getRequestId());
            cached.setJobId(existingRecord.getJobId());
            cached.setStatus(ProcessStatusEnum.PROCESSED.getCode());
            cached.setErrorMessage(existingRecord.getErrorMsg());
            if (existingRecord.getResponseBody() != null) {
                cached.setResponseBody(JSON.parse(existingRecord.getResponseBody()));
            }
            return cached;
        }

        LinkedHashMap v2Params = new LinkedHashMap();
        v2Params.put("requestId", requestId);

        JsonRpc2Request v2Request = new JsonRpc2Request(
                request.getId(),
                ORACLE_RESULT_V2_METHOD,
                v2Params,
                "2.0"
        );

        VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
        vnClientJobIdDTO.setJobId(existingRecord.getJobId());
        vnClientJobIdDTO.setVnCode(existingRecord.getVnCode());
        vnClientJobIdDTO.setRequestBody(v2Request);

        logger.info("MethodOracleGetTransactionV2 call VN, requestId={}, vnCode={}",
                requestId, existingRecord.getVnCode());
        JsonRpc2Response respResult = vnGatewayClient.requestJobSend(vnClientJobIdDTO);

        if (respResult == null) {
            logger.error("MethodOracleGetTransactionV2 VN call returned null, requestId={}", requestId);
            throw new JsonRpc2ServerErrorException(
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
                    MDC.get(LogTraceIdConstant.TRACE_ID),
                    JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
                    "VN result query returned null for requestId: " + requestId);
        }

        Object resultObj = respResult.getResult();
        OracleResultV2ResDTO resultDTO = JSONObject.parseObject(
                JSONObject.toJSONString(resultObj), OracleResultV2ResDTO.class);

        if (resultDTO != null
                && Integer.valueOf(ProcessStatusEnum.PROCESSED.getCode()).equals(resultDTO.getStatus())) {
            OracleRequestV2RecordWithBLOBs updateRecord = new OracleRequestV2RecordWithBLOBs();
            updateRecord.setRequestId(existingRecord.getRequestId());
            updateRecord.setProcessStatus(ProcessStatusEnum.PROCESSED.getCode());
            updateRecord.setResponseBody(JSONObject.toJSONString(resultDTO.getResponseBody()));
            if (resultDTO.getErrorMessage() != null) {
                updateRecord.setErrorMsg(resultDTO.getErrorMessage());
            }
            updateRecord.setUpdateDate(new Date());

            oracleRequestV2RecordMapper.updateByRequestIdSelective(updateRecord);
            logger.info("MethodOracleGetTransactionV2 updated to PROCESSED, requestId={}", requestId);
        }

        return resultDTO;
    }

    @Override
    public String checkParams(JsonRpc2Request request) {

        LinkedHashMap params = request.getParams();
        if (params == null) {
            return "params is empty";
        }

        String requestId = MapUtils.getString(params, "requestId");
        if (StringUtils.isBlank(requestId)) {
            return "requestId is empty";
        }

        return null;
    }
}
