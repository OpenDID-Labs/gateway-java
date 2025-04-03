package io.opendid.web2gateway.oracleweb2process.methodhandler;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.method.Web2Method;
import io.opendid.web2gateway.oracleweb2process.methodvalidator.MethodAccessVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodDefaultExecuteHandler {

  public static Logger logger= LoggerFactory.getLogger(MethodDefaultExecuteHandler.class);

  public static JsonRpc2Response execute(String bodyStr,
      MethodAccessVerify methodAccessVerify)
      throws Exception {

    JsonRpc2Request body = ExecuterCore.parseJsonObject(bodyStr);
    Web2Method methodProcess = ExecuterCore.doExecute(body, methodAccessVerify);

    Object processResult = methodProcess.process(body);

    return new
        JsonRpc2Response(body.getId(),
        processResult
    );

  }


}
