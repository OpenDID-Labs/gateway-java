package io.opendid.web2gateway.model.dto.subscription;

public class SubIdCacheDTO {

    private String subId;

    private String walletAddress;

    private String walletPrivKey;

    private String walletPubKey;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getWalletPrivKey() {
        return walletPrivKey;
    }

    public void setWalletPrivKey(String walletPrivKey) {
        this.walletPrivKey = walletPrivKey;
    }

    public String getWalletPubKey() {
        return walletPubKey;
    }

    public void setWalletPubKey(String walletPubKey) {
        this.walletPubKey = walletPubKey;
    }
}
