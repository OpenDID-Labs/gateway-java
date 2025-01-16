package io.opendid.web2gateway.model.dto.oracle;

public class OracleCallbackCancelResultDTO {

    private String requestId;

    private String cancelTxHash;

    private Integer status;

    private String signData;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCancelTxHash() {
        return cancelTxHash;
    }

    public void setCancelTxHash(String cancelTxHash) {
        this.cancelTxHash = cancelTxHash;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }
}
