package io.opendid.web2gateway.common.enums.oauth2;

public enum GrantTypeEnum {

  CLIENT_CREDENTIALS("client_credentials");

  private String code;

  GrantTypeEnum(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

}
