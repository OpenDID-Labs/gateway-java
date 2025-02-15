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

  // Please use -32000 to -32049
  JSON_RPC2_CODE_32000(-32000, "Server error!"),
  JSON_RPC2_CODE_32001(-32001, "MAX_GAS_UNITS_BELOW_MIN_TRANSACTION_GAS_UNITS"),
  JSON_RPC2_CODE_32002(-32002, "Call VN error!"),
  JSON_RPC2_CODE_32003(-32003, "Call http error!"),
  JSON_RPC2_CODE_32004(-32004, "Data does not exist!"),
  JSON_RPC2_CODE_32005(-32005, "This JobId is currently not supported!"),


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
