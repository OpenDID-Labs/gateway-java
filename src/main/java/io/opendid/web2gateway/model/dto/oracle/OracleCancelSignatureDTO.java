package io.opendid.web2gateway.model.dto.oracle;

public class OracleCancelSignatureDTO {

    private  String vnWalletPrivateKey;

    private Object wrapData;

    public Object getWrapData() {
        return wrapData;
    }

    public void setWrapData(Object wrapData) {
        this.wrapData = wrapData;
    }

    public String getVnWalletPrivateKey() {
        return vnWalletPrivateKey;
    }

    public void setVnWalletPrivateKey(String vnWalletPrivateKey) {
        this.vnWalletPrivateKey = vnWalletPrivateKey;
    }
}
