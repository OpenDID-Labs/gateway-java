package io.opendid.web2gateway.oraclebodyhandler.interfaces;

import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapRemoveConsumerBodyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareRemoveConsumerBodyRespDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRemoveConsumerSendDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

public interface HomeChainRemoveConsumerInterface {

    String BEAN_SUFFIX="RemoveConsumerProcess";

    WarpFlareRemoveConsumerBodyRespDTO wrapBody(OracleWrapRemoveConsumerBodyDTO oracleWrapRemoveConsumerBodyDTO) throws Exception;

    OracleRemoveConsumerSignatureResDTO signature(OracleRemoveConsumerSignatureDTO oracleRemoveConsumerSignatureDTO) throws Exception;

    JsonRpc2Response sendMessage(OracleRemoveConsumerSendDTO oracleRemoveConsumerSendDTO) throws Exception;

}
