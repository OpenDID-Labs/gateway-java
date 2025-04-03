package io.opendid.web2gateway.common.enums.status;


/**
*  支付模式：1-实时付费；2-预付费
* @author Dong-Jianguo
* @Date: 2025/3/20
* @version 1.0.0
*
* @history date, modifier,and description
**/
public enum PaymentTypeEnum {

  PAYMENT_TYPE_REAL_TIME(1,"PayRealTime"),
  PAYMENT_TYPE_PRE(2,"PayPre"),
  ;

  private int code;

  private String name;

  PaymentTypeEnum(int code, String name) {
    this.code = code;
    this.name = name;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
