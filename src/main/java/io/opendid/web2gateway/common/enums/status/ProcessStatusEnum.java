package io.opendid.web2gateway.common.enums.status;


//status  1=pending   5=processed  10=error
public enum ProcessStatusEnum {

  PENDING(1),
  PROCESSED(5),
  ERROR(10),
  EXCEEDFEE(15),
  EXCEEDING(20)
  ;

  private int code;

  ProcessStatusEnum(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}