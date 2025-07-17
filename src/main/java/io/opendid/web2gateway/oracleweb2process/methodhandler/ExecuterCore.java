package io.opendid.web2gateway.oracleweb2process.methodhandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidParamsException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidRequestException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ParseErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.method.Web2Method;
import io.opendid.web2gateway.oracleweb2process.methodfactory.Web2MethodProcessFactory;
import io.opendid.web2gateway.oracleweb2process.methodvalidator.MethodAccessVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

class ExecuterCore {

    private static Logger logger= LoggerFactory.getLogger(ExecuterCore.class);


    static Web2Method doExecute(
                                JsonRpc2Request body,
                            MethodAccessVerify methodAccessVerify) throws Exception {
        String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

//        JsonRpc2Request body = ExecuterCore.checkJsonString(bodyStr);
        Web2Method methodProcess = Web2MethodProcessFactory.getMethodProcess(body.getMethod());

        methodAccessVerify.checkAccess(traceId,
                methodProcess, body.getMethod()
        );

        ExecuterCore.checkParams(body, methodProcess, traceId);

        return methodProcess;
    }

    static JsonRpc2Request parseJsonObject(String bodystr) throws JsonRpc2InvalidRequestException {
        String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);
        try {
            JsonRpc2Request jsonRpc2Request = JSON.parseObject(bodystr, JsonRpc2Request.class);
            if (jsonRpc2Request.checkJsonInvalid()) {
                throw new JsonRpc2InvalidRequestException(traceId, "invalid jsonrpc2.0 body");
            }
            return jsonRpc2Request;
        } catch (JSONException e) {
            logger.error("error", e);
            throw new JsonRpc2ParseErrorException(traceId,
                    e.getMessage(), "");
        }

    }

   private static void checkParams(JsonRpc2Request body, Web2Method methodProcess,
                            String traceId) throws Exception {
        String checkParams = methodProcess.checkParams(body);
        if (checkParams != null) {
            throw new JsonRpc2InvalidParamsException(
                    traceId, "invalid params", checkParams
            );
        }
    }
}
