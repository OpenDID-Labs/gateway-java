package io.opendid.web2gateway.oracleweb2process.method;

import io.opendid.web2gateway.model.dto.vnclient.VnCallbackSignPayloadDTO;

import java.util.LinkedHashMap;
import java.util.Map;

public interface Web2CallbackMethod extends Web2Method {

    VnCallbackSignPayloadDTO buildSignPayload(String decryptStr);

}
