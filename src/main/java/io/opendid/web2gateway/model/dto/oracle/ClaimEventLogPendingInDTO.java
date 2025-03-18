package io.opendid.web2gateway.model.dto.oracle;

public class ClaimEventLogPendingInDTO {

    private Long claimExecuteTime;

    private Integer claimExecuteCount;

    public Long getClaimExecuteTime() {
        return claimExecuteTime;
    }

    public void setClaimExecuteTime(Long claimExecuteTime) {
        this.claimExecuteTime = claimExecuteTime;
    }

    public Integer getClaimExecuteCount() {
        return claimExecuteCount;
    }

    public void setClaimExecuteCount(Integer claimExecuteCount) {
        this.claimExecuteCount = claimExecuteCount;
    }
}
