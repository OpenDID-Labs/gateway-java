package io.opendid.web2gateway.oraclebodyhandler.interfaces;


import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.OracleCancelSendMessageDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleChainCancelMsgDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestCancelMetaDataDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

@Deprecated
public interface HomeChainRequestCancelBodyInterface {

  String BEAN_SUFFIX="CancelProcess";

  @Deprecated
  String signature(OracleChainCancelMsgDTO oracleChainCancelMsgDTO) throws Exception, JsonRpc2ServerErrorException;

  @Deprecated
  OracleRequestCancelMetaDataDTO wrapBody(OracleChainCancelMsgDTO oracleChainMsgDTO) throws Exception;

  @Deprecated
  JsonRpc2Response sendMessage(OracleCancelSendMessageDTO sendMessageDTO) throws Exception, JsonRpc2ServerErrorException;

}
