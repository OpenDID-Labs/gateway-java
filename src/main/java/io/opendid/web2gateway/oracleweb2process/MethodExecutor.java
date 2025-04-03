package io.opendid.web2gateway.oracleweb2process;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.methodexecute.AdminMethodExecute;
import io.opendid.web2gateway.oracleweb2process.methodexecute.OracleMethodExecute;
import io.opendid.web2gateway.oracleweb2process.methodexecute.PublicMethodExecute;
import io.opendid.web2gateway.oracleweb2process.methodexecute.SubscriptionMethodExecute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodExecutor {

  private static Logger logger = LoggerFactory.getLogger(MethodExecutor.class);


  private MethodExecutor() {
  }

  public static JsonRpc2Response oracleMethod(String bodyStr)
      throws Exception {
    return OracleMethodExecute.execute(bodyStr);
  }

  public static  JsonRpc2Response publicMethod(String bodyStr)
      throws Exception {
    return PublicMethodExecute.execute(bodyStr);
  }
  public static  JsonRpc2Response adminMethod(String bodyStr)
      throws Exception {
    return AdminMethodExecute.execute(bodyStr);
  }


  public static  JsonRpc2Response subscriptionMethod(String bodyStr)
          throws Exception {
    return SubscriptionMethodExecute.execute(bodyStr);
  }

}
