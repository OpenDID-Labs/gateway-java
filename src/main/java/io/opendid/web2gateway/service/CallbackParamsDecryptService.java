package io.opendid.web2gateway.service;

import io.opendid.web2gateway.common.utils.ECIESUtil;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class CallbackParamsDecryptService {

    @Value("${service-key.privatekey}")
    private String localPrivateKey;

    public String decryptRequestCallbackParams(String encryptData) {

        // decrypt params
        String resultData = ECIESUtil.hexDecrypt(localPrivateKey, encryptData);
        return resultData;
    }

    public JsonRpc2Request decryptCancelCallbackParams(JsonRpc2Request request) {

        return request;
    }
}
