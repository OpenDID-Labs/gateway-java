package io.opendid.web2gateway.contextfilter.api;

import org.springframework.stereotype.Component;

@Component
public class SystemContext {

  /**
   * decrypt ; vn call data
   */
  private String callBackDataDecrypted;

  public String getCallBackDataDecrypted() {
    return callBackDataDecrypted;
  }

  public void setCallBackDataDecrypted(String callBackDataDecrypted) {
    this.callBackDataDecrypted = callBackDataDecrypted;
  }
}
