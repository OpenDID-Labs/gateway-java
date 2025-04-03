package io.opendid.web2gateway.model.dto.oracle.subscription;

public class OracleSubscriptionSignatureDTO {

    private  String vnWalletPrivateKey;

    private WarpFlareSubCreateBodyRespDTO wrapData;

    public WarpFlareSubCreateBodyRespDTO getWrapData() {
        return wrapData;
    }

    public void setWrapData(WarpFlareSubCreateBodyRespDTO wrapData) {
        this.wrapData = wrapData;
    }

    public String getVnWalletPrivateKey() {
        return vnWalletPrivateKey;
    }

    public void setVnWalletPrivateKey(String vnWalletPrivateKey) {
        this.vnWalletPrivateKey = vnWalletPrivateKey;
    }
}
