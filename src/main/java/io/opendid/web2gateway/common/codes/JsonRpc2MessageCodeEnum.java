package io.opendid.web2gateway.common.codes;

/**
*
* @author Dong-Jianguo
* @Date: 2024/12/19
* @version 1.0.0
*
* @history date, modifier,and description
**/
public enum JsonRpc2MessageCodeEnum {

  JSON_RPC2_CODE_32000(-32000, "Server error!"),


  ;


  /**
   * code
   */
  private Integer code;

  /**
   * message
   */
  private String message;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  JsonRpc2MessageCodeEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

}
