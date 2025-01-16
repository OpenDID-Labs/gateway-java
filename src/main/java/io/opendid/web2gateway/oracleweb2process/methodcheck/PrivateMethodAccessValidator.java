package io.opendid.web2gateway.oracleweb2process.methodcheck;

import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2UnauthorizedRequestException;
import io.opendid.web2gateway.oracleweb2process.method.Web2Method;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateMethodAccessValidator implements
    MethodAccessVerify {

  private Logger logger = LoggerFactory.getLogger(PrivateMethodAccessValidator.class);


  @Override
  public void checkAccess(String traceId, Web2Method methodProcess, String method) {
    if (methodProcess.getClass().isAnnotationPresent(MethodPrivate.class)) {
      logger.info("The current call is a valid private method");
    } else {
      logger.error("The current call is a invalid private method :{}",
          method
      );
      throw new JsonRpc2UnauthorizedRequestException(
          traceId, "invalid private method ");
    }
  }

  public static PrivateMethodAccessValidator getInstance() {
    return InstanceHolder.instance;
  }

  private static class InstanceHolder {

    private static final PrivateMethodAccessValidator instance = new PrivateMethodAccessValidator();
  }
}
