package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.status.CallbackProcessStatus;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.OracleCallBackResultDTO;
import io.opendid.web2gateway.model.dto.vnclient.OracleCallBackRespDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnCallbackSignPayloadDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.repository.mapper.OracleRequestV2RecordMapper;
import io.opendid.web2gateway.repository.model.OracleRequestV2RecordWithBLOBs;
import io.opendid.web2gateway.security.checkaspect.MethodPublic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;

@Component(Web2MethodName.ORACLE_CALL_BACK_V2 + Web2Method.BEAN_SUFFIX)
@MethodPublic
public class MethodOracleRequestV2Callback implements Web2CallbackMethod {

    private Logger logger = LoggerFactory.getLogger(MethodOracleRequestV2Callback.class);

    @Autowired
    private OracleRequestV2RecordMapper oracleRequestV2RecordMapper;

    @Override
    public Object process(JsonRpc2Request request) {

        logger.info("MethodOracleRequestV2Callback receive params: {}", JSONObject.toJSONString(request));

        OracleCallBackRespDTO callBackRespDTO = new OracleCallBackRespDTO();
        try {
            LinkedHashMap params = request.getParams();
            String requestId = params.get("requestId").toString();
            String responseBody = params.get("data") != null ? params.get("data").toString() : null;

            OracleRequestV2RecordWithBLOBs updateRecord = new OracleRequestV2RecordWithBLOBs();
            updateRecord.setRequestId(requestId);
            updateRecord.setProcessStatus(ProcessStatusEnum.PROCESSED.getCode());
            updateRecord.setResponseBody(responseBody);
            updateRecord.setUpdateDate(new Date());

            oracleRequestV2RecordMapper.updateByRequestIdSelective(updateRecord);
            callBackRespDTO.setSuccessfully(CallbackProcessStatus.SUCCESSFUL.getCode());
        } catch (Exception e) {
            logger.error("MethodOracleRequestV2Callback process error:", e);
            callBackRespDTO.setSuccessfully(CallbackProcessStatus.FAILED.getCode());
            callBackRespDTO.setCallbackMessage(e.getMessage());
        }
        return JSONObject.toJSONString(callBackRespDTO);
    }

    @Override
    public String checkParams(JsonRpc2Request request) {

        return null;
    }

    @Override
    public VnCallbackSignPayloadDTO buildSignPayload(String decryptStr) {

        OracleCallBackResultDTO oracleCallBackResultDTO =
                JSONObject.parseObject(decryptStr, OracleCallBackResultDTO.class);

        VnCallbackSignPayloadDTO vnCallbackSignPayloadDTO = new VnCallbackSignPayloadDTO();
        vnCallbackSignPayloadDTO.setRequestId(oracleCallBackResultDTO.getRequestId());
        vnCallbackSignPayloadDTO.setVnSignData(oracleCallBackResultDTO.getSignData());

        StringBuilder signStrBuilder = new StringBuilder();
        signStrBuilder.append(oracleCallBackResultDTO.getRequestId() != null
                ? oracleCallBackResultDTO.getRequestId() : "");
        signStrBuilder.append(oracleCallBackResultDTO.getData() != null
                ? oracleCallBackResultDTO.getData() : "");
        signStrBuilder.append(oracleCallBackResultDTO.getStatus() != null
                ? oracleCallBackResultDTO.getStatus() : "");


        vnCallbackSignPayloadDTO.setSignStr(signStrBuilder.toString());

        LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("requestId", oracleCallBackResultDTO.getRequestId());
        linkedHashMap.put("data", oracleCallBackResultDTO.getData());
        vnCallbackSignPayloadDTO.setMethodJsonRpc2Params(linkedHashMap);
        return vnCallbackSignPayloadDTO;
    }
}
