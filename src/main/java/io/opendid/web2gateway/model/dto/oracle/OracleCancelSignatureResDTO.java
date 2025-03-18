package io.opendid.web2gateway.model.dto.oracle;

public class OracleCancelSignatureResDTO {

    private Object reWrapOriginWrapData;

    private Object signature;

    public Object getSignature() {
        return signature;
    }

    public void setSignature(Object signature) {
        this.signature = signature;
    }

    public Object getReWrapOriginWrapData() {
        return reWrapOriginWrapData;
    }

    public void setReWrapOriginWrapData(Object reWrapOriginWrapData) {
        this.reWrapOriginWrapData = reWrapOriginWrapData;
    }
}
