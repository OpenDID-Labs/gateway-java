package io.opendid.web2gateway.model.dto.oracle.subscription;

public class OracleSubscriptionCreateRespDTO {

    private String subId;

    private String transactionHash;

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
}
