package io.opendid.web2gateway.model.dto.oracle.subscription;

public class SubscriptionWrapBodyReqDTO {

    private String vnCode;

    private String publicKey;

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
