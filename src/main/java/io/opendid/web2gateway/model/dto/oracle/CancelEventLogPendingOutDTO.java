package io.opendid.web2gateway.model.dto.oracle;

public class CancelEventLogPendingOutDTO {

    private Long logId;

    private String requestId;

    private Integer cancelExecuteCount;

    private String jobId;

    private String vnCode;

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

    public Integer getCancelExecuteCount() {
        return cancelExecuteCount;
    }

    public void setCancelExecuteCount(Integer cancelExecuteCount) {
        this.cancelExecuteCount = cancelExecuteCount;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
