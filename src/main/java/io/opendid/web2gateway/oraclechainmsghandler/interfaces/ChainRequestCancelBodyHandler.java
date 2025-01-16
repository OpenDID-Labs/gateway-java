package io.opendid.web2gateway.oraclechainmsghandler.interfaces;


import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.OracleCancelSendMessageDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleChainCancelMsgDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleChainMsgDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestCancelMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleRequestMetaDataDTO;
import io.opendid.web2gateway.model.dto.oracle.OracleSendMessageDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import java.util.LinkedHashMap;

public interface ChainRequestCancelBodyHandler {

  String BEAN_SUFFIX="Process";

  String signature(OracleChainCancelMsgDTO oracleChainCancelMsgDTO) throws Exception, JsonRpc2ServerErrorException;

  OracleRequestCancelMetaDataDTO assemblyMessage(OracleChainCancelMsgDTO oracleChainMsgDTO) throws Exception;

  JsonRpc2Response sendMessage(OracleCancelSendMessageDTO sendMessageDTO) throws Exception, JsonRpc2ServerErrorException;

}
