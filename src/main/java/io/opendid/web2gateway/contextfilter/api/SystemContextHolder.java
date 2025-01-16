package io.opendid.web2gateway.contextfilter.api;

public class SystemContextHolder {
  private static final ThreadLocal<SystemContext> userContext = new ThreadLocal<SystemContext>();

  public static final SystemContext getContext(){
    SystemContext context = userContext.get();

    if (context == null) {
      context = new SystemContext();
      userContext.set(context);

    }
    return userContext.get();
  }
}
