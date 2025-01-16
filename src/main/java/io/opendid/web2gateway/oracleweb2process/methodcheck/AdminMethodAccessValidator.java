package io.opendid.web2gateway.oracleweb2process.methodcheck;

import io.opendid.web2gateway.contextfilter.security.SecurityContext;
import io.opendid.web2gateway.contextfilter.security.SecurityContextHolder;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2UnauthorizedRequestException;
import io.opendid.web2gateway.oracleweb2process.method.Web2Method;
import io.opendid.web2gateway.security.checkaspect.MethodAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminMethodAccessValidator implements
    MethodAccessVerify {

  private Logger logger = LoggerFactory.getLogger(AdminMethodAccessValidator.class);


  @Override
  public void checkAccess(String traceId, Web2Method methodProcess, String method) {
    if (methodProcess.getClass().isAnnotationPresent(MethodAdmin.class)) {
      //check admin claims
      verifyMethodAccess(traceId);
      logger.info("The current call is a valid admin method");
    } else {
      logger.error("The current call is a invalid admin method :{}",
          method
      );
      throw new JsonRpc2UnauthorizedRequestException(
          traceId, "Invalid admin permissions ");
    }
  }

  private void verifyMethodAccess(String traceId) {
    SecurityContext context = SecurityContextHolder.getContext();
    String jwt = context.getJwt();
    String subject = context.getSubject();
    boolean admin = context.isAdmin();

    if (!admin){
      throw new JsonRpc2UnauthorizedRequestException(
          traceId, "Invalid admin permissions"
      );
    }



  }

  public static AdminMethodAccessValidator getInstance() {
    return InstanceHolder.instance;
  }

  private static class InstanceHolder {
    private static final AdminMethodAccessValidator instance = new AdminMethodAccessValidator();
  }
}
