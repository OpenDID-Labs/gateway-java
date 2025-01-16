package io.opendid.web2gateway.model.dto.vnclient;

import java.util.LinkedHashMap;

public class VnCallbackSignPayloadDTO {

    private String signStr;

    private String requestId;

    private String vnSignData;

    private LinkedHashMap<Object, Object> methodJsonRpc2Params;

    public String getSignStr() {
        return signStr;
    }

    public void setSignStr(String signStr) {
        this.signStr = signStr;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getVnSignData() {
        return vnSignData;
    }

    public void setVnSignData(String vnSignData) {
        this.vnSignData = vnSignData;
    }

    public LinkedHashMap<Object, Object> getMethodJsonRpc2Params() {
        return methodJsonRpc2Params;
    }

    public void setMethodJsonRpc2Params(LinkedHashMap<Object, Object> methodJsonRpc2Params) {
        this.methodJsonRpc2Params = methodJsonRpc2Params;
    }
}
