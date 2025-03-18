package io.opendid.web2gateway.oraclebodyhandler.interfaces;


import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.config.WalletPrivateKeyConfig;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.service.VngatewayRouteInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface HomeChainRequestBodyInterface {

  String BEAN_SUFFIX="RequestProcess";

  OracleWrapRequestBodyResDTO wrapBody(OracleWrapRequestBodyDTO oracleWrapRequestBodyDTO) throws Exception;

  OracleRequestSignatureResDTO signature(OracleRequestSignatureDTO oracleRequestSignatureDTO) throws Exception, JsonRpc2ServerErrorException;

  JsonRpc2Response sendMessage(OracleRequestSendMessageDTO sendMessageDTO) throws Exception, JsonRpc2ServerErrorException;

  List<GeneratePubKeyAndAddrDTO> generatePublicKeyAndAddr() throws Exception;

}
