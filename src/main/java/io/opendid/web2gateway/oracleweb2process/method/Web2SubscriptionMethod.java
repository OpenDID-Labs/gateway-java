package io.opendid.web2gateway.oracleweb2process.method;


import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public interface Web2SubscriptionMethod extends  Web2Method {

    @Override
    Object process(JsonRpc2Request request) throws Exception;
}
