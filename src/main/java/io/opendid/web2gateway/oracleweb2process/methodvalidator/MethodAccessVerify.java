package io.opendid.web2gateway.oracleweb2process.methodvalidator;

import io.opendid.web2gateway.oracleweb2process.method.Web2Method;

public interface MethodAccessVerify {

  void checkAccess(String traceId,
      Web2Method methodProcess,
      String method);
}
