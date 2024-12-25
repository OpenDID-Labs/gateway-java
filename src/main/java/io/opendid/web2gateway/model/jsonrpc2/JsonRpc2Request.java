package io.opendid.web2gateway.model.jsonrpc2;

import io.micrometer.common.util.StringUtils;
import java.util.LinkedHashMap;

public class JsonRpc2Request extends JsonRpc2ROOT {

  private Long id;
  private String method;
  private LinkedHashMap params;


  public JsonRpc2Request(Long id, String method, LinkedHashMap params,
      String jsonrpc) {
    this.id = id;
    this.method = method;
    this.params = params;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public LinkedHashMap getParams() {
    return params;
  }

  public void setParams(LinkedHashMap params) {
    this.params = params;
  }

  public boolean checkJsonInvalid(){
    if (StringUtils.isBlank(super.getJsonrpc()) ||
        (StringUtils.isNotBlank(super.getJsonrpc()) && !"2.0".equals(super.getJsonrpc()))||
        StringUtils.isBlank(method) ||
        params == null) {
      return true;
    }
    return false;
  }
}
