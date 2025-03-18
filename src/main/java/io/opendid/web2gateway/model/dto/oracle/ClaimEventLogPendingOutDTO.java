package io.opendid.web2gateway.model.dto.oracle;

public class ClaimEventLogPendingOutDTO {

    private Long logId;

    private String requestId;

    private Integer claimExecuteCount;

    private String vnCode;

    private String jobId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getClaimExecuteCount() {
        return claimExecuteCount;
    }

    public void setClaimExecuteCount(Integer claimExecuteCount) {
        this.claimExecuteCount = claimExecuteCount;
    }
}
