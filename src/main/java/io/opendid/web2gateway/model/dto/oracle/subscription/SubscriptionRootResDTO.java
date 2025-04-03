package io.opendid.web2gateway.model.dto.oracle.subscription;

public abstract class SubscriptionRootResDTO {
    private String transactionHash;

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
}
