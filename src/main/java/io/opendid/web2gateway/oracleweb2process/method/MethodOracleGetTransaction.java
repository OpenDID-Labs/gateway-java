package io.opendid.web2gateway.oracleweb2process.method;

import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.traceid.TraceIdPutUtil;
import io.opendid.web2gateway.common.utils.TraceIdeUtil;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.contextfilter.api.SystemContextHolder;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidParamsException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.service.OracleMsgRecordService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.ORACLE_GET_TRANSACTION + Web2Method.BEAN_SUFFIX)
@MethodPrivate
public class MethodOracleGetTransaction implements Web2Method {


  @Autowired
  private OracleMsgRecordService oracleMsgRecordService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    return oracleMsgRecordService.selectMsgRecordByRequestId(
            String.valueOf(request.getParams().get("oracleRequestTxHash")));
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

    if (request.getParams().get("oracleRequestTxHash") == null) {
      throw  new JsonRpc2InvalidParamsException(traceId,"oracleRequestTxHash is required");
    }

    return null;


  }
}
