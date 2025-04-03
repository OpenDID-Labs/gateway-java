package io.opendid.web2gateway.oracleweb2process.methodvalidator;

import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2UnauthorizedRequestException;
import io.opendid.web2gateway.oracleweb2process.method.Web2Method;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionMethodAccessValidator implements
    MethodAccessVerify {

  private Logger logger = LoggerFactory.getLogger(SubscriptionMethodAccessValidator.class);


  @Override
  public void checkAccess(String traceId, Web2Method methodProcess, String method) {
    if (methodProcess.getClass().isAnnotationPresent(MethodSubscription.class)) {
      logger.info("The current call is a valid subscription method");
    } else {
      logger.error("The current call is a invalid subscription method :{}",
          method
      );
      throw new JsonRpc2UnauthorizedRequestException(
          traceId, "Invalid subscription permissions ");
    }
  }



  public static SubscriptionMethodAccessValidator getInstance() {
    return InstanceHolder.instance;
  }

  private static class InstanceHolder {
    private static final SubscriptionMethodAccessValidator instance = new SubscriptionMethodAccessValidator();
  }
}
