/*
 * Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
 * (https://www.mozilla.org/en-US/MPL/2.0/)
 */

package io.opendid.web2gateway.exception.throwentity.jsonrpc2;


/**
 * method description
 *
 * @author wb
 * @version 1.0.0
 * @Date: 2023/9/28
 * @history date, modifier,and description
 **/
public class JsonRpc2ParseErrorException extends JsonRpc2ExceptionObject implements  JsonRpcException{

  private Integer code=-32700;
  private String message;
  private String logId;
  private String data;


  public JsonRpc2ParseErrorException(){

  }

  public JsonRpc2ParseErrorException( String logId,String message,String data) {
    super(message);
    this.logId = logId;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }


  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String getLogId() {
    return logId;
  }

  public void setLogId(String logId) {
    this.logId = logId;
  }

  @Override
  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
