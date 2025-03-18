package io.opendid.web2gateway.common.enums.request;

public enum MethodEnum {

  REQUEST("oracle_request"),
  RESULT("oracle_result"),
  CANCEL_RESULT("oracle_result_cancel"),
  GET_VN_INFO("get_vn_info"),
  REQUEST_CLAIM("request_claim"),
  ;

  private String code;

  MethodEnum(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
