package io.opendid.web2gateway.oracleweb2process.methodexecute;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.methodhandler.MethodSubExecuteHandler;
import io.opendid.web2gateway.oracleweb2process.methodvalidator.SubscriptionMethodAccessValidator;
import io.opendid.web2gateway.oracleweb2process.methodhandler.MethodDefaultExecuteHandler;

public  class SubscriptionMethodExecute {

  public static JsonRpc2Response execute(String bodyStr)
      throws Exception {
    return MethodSubExecuteHandler.execute(bodyStr,
            SubscriptionMethodAccessValidator.getInstance());
  }
}