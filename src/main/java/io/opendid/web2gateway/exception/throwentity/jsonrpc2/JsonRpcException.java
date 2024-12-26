package io.opendid.web2gateway.exception.throwentity.jsonrpc2;

public interface JsonRpcException {

  int getCode() ;

  String getMessage() ;

  String getLogId() ;

  Object getData() ;

}
