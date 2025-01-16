package io.opendid.web2gateway.model.dto.oracle;

public class CancelEventLogPendingInDTO {

    private Long cancelNextExecuteTime;

    private Integer cancelExecuteCount;

    private Integer cancelStatus;

    public Long getCancelNextExecuteTime() {
        return cancelNextExecuteTime;
    }

    public void setCancelNextExecuteTime(Long cancelNextExecuteTime) {
        this.cancelNextExecuteTime = cancelNextExecuteTime;
    }

    public Integer getCancelExecuteCount() {
        return cancelExecuteCount;
    }

    public void setCancelExecuteCount(Integer cancelExecuteCount) {
        this.cancelExecuteCount = cancelExecuteCount;
    }

    public Integer getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(Integer cancelStatus) {
        this.cancelStatus = cancelStatus;
    }
}
