package io.opendid.web2gateway.model.jsonrpc2;

public class JsonRpc2Response extends JsonRpc2ROOT {

  private Long id;
  private Object result;

  public JsonRpc2Response(Long id, Object result, String error) {
    this.id = id;
    this.result = result;
  }

  public JsonRpc2Response(Long id, Object result) {
    this.id = id;
    this.result = result;
  }

  public Long getId() {
    return id;
  }

  public Object getResult() {
    return result;
  }


}
