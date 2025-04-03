package io.opendid.web2gateway.oraclebodyhandler.interfaces;

import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSendDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSignatureDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleSubTokenTransferSignatureResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleWrapSubTokenTransferBodyDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.WarpFlareSubTokenTransferBodyRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

public interface HomeChainSubTokenTransferInterface {

    String BEAN_SUFFIX="SubTokenTransfer";

    WarpFlareSubTokenTransferBodyRespDTO wrapBody(OracleWrapSubTokenTransferBodyDTO oracleWrapSubTokenTransferBodyDTO) throws Exception;

    OracleSubTokenTransferSignatureResDTO signature(OracleSubTokenTransferSignatureDTO oracleSubTokenTransferSignatureDTO) throws Exception;

    JsonRpc2Response sendMessage(OracleSubTokenTransferSendDTO oracleSubTokenTransferSendDTO) throws Exception;

}
