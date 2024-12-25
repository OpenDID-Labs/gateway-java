package io.opendid.web2gateway.oracleweb2process;

import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2MethodNotFoundException;
import org.slf4j.MDC;

public class Web2MethodProcessFactory {

  public static Web2MethodProcess getMethodProcess(String method) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

    Web2MethodProcess bean = SpringContextUtil.getBean(method + Web2MethodProcess.BEAN_SUFFIX,
        Web2MethodProcess.class);

    if (bean == null) {
      throw  new JsonRpc2MethodNotFoundException(
          traceId,"method not found",""
      );
    }

    return bean;

  }
}
