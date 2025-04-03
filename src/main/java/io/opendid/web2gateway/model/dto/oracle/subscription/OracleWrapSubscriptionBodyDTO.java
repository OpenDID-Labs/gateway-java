package io.opendid.web2gateway.model.dto.oracle.subscription;

public class OracleWrapSubscriptionBodyDTO {

    private String vnCode;

    private String walletPublicKey;

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }

    public String getWalletPublicKey() {
        return walletPublicKey;
    }

    public void setWalletPublicKey(String walletPublicKey) {
        this.walletPublicKey = walletPublicKey;
    }
}
