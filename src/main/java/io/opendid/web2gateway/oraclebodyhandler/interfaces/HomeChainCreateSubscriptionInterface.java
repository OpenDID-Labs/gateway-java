package io.opendid.web2gateway.oraclebodyhandler.interfaces;

import io.opendid.web2gateway.model.dto.oracle.subscription.*;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

public interface HomeChainCreateSubscriptionInterface {

    String BEAN_SUFFIX="SubscriptionProcess";

    OracleWrapSubscriptionBodyResDTO wrapBody(OracleWrapSubscriptionBodyDTO oracleWrapSubscriptionBodyDTO) throws Exception;

    OracleSubscriptionSignatureResDTO signature(OracleSubscriptionSignatureDTO oracleSubscriptionSignatureDTO) throws Exception;

    JsonRpc2Response sendMessage(OracleSubscriptionSendDTO oracleSubscriptionSendDTO) throws Exception;

}
