package io.opendid.web2gateway.model.dto.oracle;

public class UpdateCancelEventLogDTO {

    private String requestId;


    private Integer cancelStatus;

    private String cancelOracleHash;

    public String getCancelOracleHash() {
        return cancelOracleHash;
    }

    public void setCancelOracleHash(String cancelOracleHash) {
        this.cancelOracleHash = cancelOracleHash;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(Integer cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

}
