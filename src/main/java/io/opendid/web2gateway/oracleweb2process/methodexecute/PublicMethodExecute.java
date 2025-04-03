package io.opendid.web2gateway.oracleweb2process.methodexecute;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.methodvalidator.PublicMethodAccessValidator;
import io.opendid.web2gateway.oracleweb2process.methodhandler.MethodDefaultExecuteHandler;

public  class PublicMethodExecute {

  public static JsonRpc2Response execute(String bodyStr)
      throws Exception {
    return MethodDefaultExecuteHandler.execute(bodyStr,
        PublicMethodAccessValidator.getInstance());
  }
}