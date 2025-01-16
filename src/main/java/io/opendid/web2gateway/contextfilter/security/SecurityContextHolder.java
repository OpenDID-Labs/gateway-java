package io.opendid.web2gateway.contextfilter.security;

public class SecurityContextHolder {
  private static final ThreadLocal<SecurityContext> userContext = new ThreadLocal<SecurityContext>();

  public static final SecurityContext getContext(){
    SecurityContext context = userContext.get();

    if (context == null) {
      context = new SecurityContext();
      userContext.set(context);

    }
    return userContext.get();
  }
}
