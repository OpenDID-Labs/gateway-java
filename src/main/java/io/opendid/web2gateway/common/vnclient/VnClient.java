package io.opendid.web2gateway.common.vnclient;

import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;

public interface VnClient {
//  requestSubscriptionSend
  JsonRpc2Response requestJobSend(VnClientJobIdDTO vnClientJobIdDTO) throws Exception;

  JsonRpc2Response requestSubscriptionSend(VnClientJobIdDTO vnClientJobIdDTO) throws Exception;

}
