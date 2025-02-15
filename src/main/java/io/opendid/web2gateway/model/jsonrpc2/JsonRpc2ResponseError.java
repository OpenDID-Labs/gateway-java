package io.opendid.web2gateway.model.jsonrpc2;

public class JsonRpc2ResponseError extends JsonRpc2ROOT {

  private ErrorEntity error;

  public ErrorEntity getError() {
    return error;
  }

  public void setError(ErrorEntity error) {
    this.error = error;
  }


  public static class  ErrorEntity{
    private int code;
    private String message;
    private Object data;
    private String logId;

    public String getLogId() {
      return logId;
    }

    public void setLogId(String logId) {
      this.logId = logId;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public Object getData() {
      return data;
    }

    public void setData(Object data) {
      this.data = data;
    }
  }

}
