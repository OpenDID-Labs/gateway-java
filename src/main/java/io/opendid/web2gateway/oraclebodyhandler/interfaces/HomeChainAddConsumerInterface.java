package io.opendid.web2gateway.oraclebodyhandler.interfaces;

import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleAddConsumerSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapAddConsumerBodyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareAddConsumerBodyRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

public interface HomeChainAddConsumerInterface {

    String BEAN_SUFFIX="AddConsumerProcess";

    WarpFlareAddConsumerBodyRespDTO wrapBody(OracleWrapAddConsumerBodyDTO oracleWrapAddConsumerBodyDTO) throws Exception;

    OracleAddConsumerSignatureResDTO signature(OracleAddConsumerSignatureDTO oracleAddConsumerSignatureDTO) throws Exception;

    JsonRpc2Response sendMessage(OracleAddConsumerSendDTO oracleAddConsumerSendDTO) throws Exception;

}
