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
    private String data;

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

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }
  }

}