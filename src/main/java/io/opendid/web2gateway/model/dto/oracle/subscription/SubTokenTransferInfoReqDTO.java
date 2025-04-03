package io.opendid.web2gateway.model.dto.oracle.subscription;

public class SubTokenTransferInfoReqDTO {

    private String vnCode;

    private String transactionHash;

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
}
