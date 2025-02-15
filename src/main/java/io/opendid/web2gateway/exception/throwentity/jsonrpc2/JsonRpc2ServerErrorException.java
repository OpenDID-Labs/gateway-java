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
public class JsonRpc2ServerErrorException extends JsonRpc2ExceptionObject implements  JsonRpcException{

  private int code;
  private String message;
  private String logId;
  private Object data;


  public JsonRpc2ServerErrorException(){

  }

  public JsonRpc2ServerErrorException(int code, String logId,String message,Object data) throws Exception {
    super(message);
    if (code == 0){
      throw new Exception("code is not 0");
    }
    this.code = code;
    this.logId = logId;
    this.message = message;
    this.data = data;
  }

  @Override
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
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
  public Object getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
