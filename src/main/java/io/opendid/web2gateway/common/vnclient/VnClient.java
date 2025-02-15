package io.opendid.web2gateway.common.vnclient;

import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

public interface VnClient {

  JsonRpc2Response request(VnClientJobIdDTO vnClientJobIdDTO) throws Exception;

}
