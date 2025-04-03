package io.opendid.web2gateway.oracleweb2process.methodexecute;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.methodvalidator.OracleMethodAccessValidator;
import io.opendid.web2gateway.oracleweb2process.methodhandler.MethodDefaultExecuteHandler;

public class OracleMethodExecute {

  public  static JsonRpc2Response execute(String bodyStr)
      throws Exception {

    return MethodDefaultExecuteHandler.execute(bodyStr,
        OracleMethodAccessValidator.getInstance());
  }
}