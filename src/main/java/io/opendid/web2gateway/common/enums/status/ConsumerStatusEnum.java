package io.opendid.web2gateway.common.enums.status;

public enum ConsumerStatusEnum {

  UNBOUND(0),
  BINDING(1);

  private int code;

  ConsumerStatusEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}
