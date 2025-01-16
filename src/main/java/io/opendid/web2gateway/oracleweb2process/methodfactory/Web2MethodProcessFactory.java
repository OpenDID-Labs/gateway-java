package io.opendid.web2gateway.oracleweb2process.methodfactory;

import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2MethodNotFoundException;
import io.opendid.web2gateway.oracleweb2process.method.Web2Method;
import org.slf4j.MDC;

public class Web2MethodProcessFactory {

  public static Web2Method getMethodProcess(String method) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

    Web2Method bean = SpringContextUtil.getBean(method + Web2Method.BEAN_SUFFIX,
        Web2Method.class);

    if (bean == null) {
      throw new JsonRpc2MethodNotFoundException(
          traceId, "method not found", ""
      );
    }

    return bean;

  }
}