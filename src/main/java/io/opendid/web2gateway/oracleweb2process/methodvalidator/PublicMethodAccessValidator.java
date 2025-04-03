package io.opendid.web2gateway.oracleweb2process.methodvalidator;

import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2UnauthorizedRequestException;
import io.opendid.web2gateway.oracleweb2process.method.Web2Method;
import io.opendid.web2gateway.security.checkaspect.MethodPublic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublicMethodAccessValidator implements
    MethodAccessVerify {

  private Logger logger = LoggerFactory.getLogger(PublicMethodAccessValidator.class);


  @Override
  public void checkAccess(String traceId, Web2Method methodProcess, String method) {
    if (methodProcess.getClass().isAnnotationPresent(MethodPublic.class)) {
      logger.info("The current call is a valid private method");
    } else {
      logger.error("The current call is a invalid private method :{}",
          method
      );
      throw new JsonRpc2UnauthorizedRequestException(
          traceId, "invalid private method ");
    }
  }

  public static PublicMethodAccessValidator getInstance() {
    return InstanceHolder.instance;
  }

  private static class InstanceHolder {
    private static final PublicMethodAccessValidator instance = new PublicMethodAccessValidator();
  }
}
