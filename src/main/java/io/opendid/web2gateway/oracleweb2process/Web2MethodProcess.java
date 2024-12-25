package io.opendid.web2gateway.oracleweb2process;


import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public interface Web2MethodProcess {
  String BEAN_SUFFIX="Process";
  Object process(JsonRpc2Request request) throws Exception, JsonRpc2ServerErrorException;

  String checkParams(JsonRpc2Request request);
}
