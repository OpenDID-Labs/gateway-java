package io.opendid.web2gateway.model.dto.oracle.subscription;

import java.math.BigInteger;
import java.util.List;

public class SubscriptionInfoDTO {

    private String subId;

    private String subIdOwner;

    private BigInteger latestBalance;

    private List<String> walletAddressArray;


    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubIdOwner() {
        return subIdOwner;
    }

    public void setSubIdOwner(String subIdOwner) {
        this.subIdOwner = subIdOwner;
    }

    public BigInteger getLatestBalance() {
        return latestBalance;
    }

    public void setLatestBalance(BigInteger latestBalance) {
        this.latestBalance = latestBalance;
    }

    public List<String> getWalletAddressArray() {
        return walletAddressArray;
    }

    public void setWalletAddressArray(List<String> walletAddressArray) {
        this.walletAddressArray = walletAddressArray;
    }
}
