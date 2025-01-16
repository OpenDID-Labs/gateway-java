package io.opendid.web2gateway.common.enums.status;

public enum ChainErrorEnum {

  NOT_EXPIRED("EREQUEST_NOT_EXPIRED"),
  INSUFFICIENT_BALANCE("EREQUEST_INSUFFICIENT")
  ;

  private final String code;

  ChainErrorEnum(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }


}
