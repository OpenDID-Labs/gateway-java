package io.opendid.web2gateway.oracleweb2process.methodexecute;

import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.methodcheck.AdminMethodAccessValidator;
import io.opendid.web2gateway.oracleweb2process.methodhandler.MethodExecuteHandler;

public  class AdminMethodExecute {

  public static JsonRpc2Response execute(String bodyStr)
      throws JsonRpc2ServerErrorException, Exception {
    return MethodExecuteHandler.execute(bodyStr,
        AdminMethodAccessValidator.getInstance());
  }
}