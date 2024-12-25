package io.opendid.web2gateway.common.enums.request;

public enum MethodEnum {

  REQUEST("oracle_request"),
  RESULT("oracle_result")
  ;

  private String code;

  MethodEnum(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
