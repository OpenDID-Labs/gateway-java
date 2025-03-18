package io.opendid.web2gateway.common.enums.status;

public enum ClaimStatusEnum {

  NOT_CREATE(0),
  PENDING(1),
  CREATE_SUCCESS(5),
  CREATE_FAIL(10)
  ;

  private int code;

  ClaimStatusEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
