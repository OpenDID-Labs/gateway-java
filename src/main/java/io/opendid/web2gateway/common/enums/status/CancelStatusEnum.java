package io.opendid.web2gateway.common.enums.status;


//status  1=pending   5=processed  10=error
public enum CancelStatusEnum {

  NOT_CANCELLED(0),
  PENDING(1),
  SUCCESSFULLY(5),
  FAIL(15),
  EXCEEDING(20),
  NOT_EXPIRED(151),
  INSUFFICIENT_BALANCE(152)
  ;

  private int code;

  CancelStatusEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
