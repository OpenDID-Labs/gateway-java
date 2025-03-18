package io.opendid.web2gateway.oraclebodyhandler.interfaces;


import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

public interface HomeChainRequestCancelBodyInterfaceV2 {

  String BEAN_SUFFIX="RequestProcessV2";

  OracleWrapCancelBodyResDTO wrapBody(OracleWrapCancelBodyDTO oracleWrapCancelBodyDTO) throws Exception;

  OracleCancelSignatureResDTO signature(OracleCancelSignatureDTO oracleCancelSignatureDTO) throws Exception, JsonRpc2ServerErrorException;

  JsonRpc2Response sendMessage(OracleCancelSendDTO oracleCancelSendDTO) throws Exception, JsonRpc2ServerErrorException;

}
