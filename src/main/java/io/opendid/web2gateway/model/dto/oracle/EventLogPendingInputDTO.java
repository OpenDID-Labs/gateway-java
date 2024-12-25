package io.opendid.web2gateway.model.dto.oracle;

public class EventLogPendingInputDTO {

  private Long nextExecuteTime;

  private Integer executeCount;

  private Integer processStatus;

  public Long getNextExecuteTime() {
    return nextExecuteTime;
  }

  public void setNextExecuteTime(Long nextExecuteTime) {
    this.nextExecuteTime = nextExecuteTime;
  }

  public Integer getExecuteCount() {
    return executeCount;
  }

  public void setExecuteCount(Integer executeCount) {
    this.executeCount = executeCount;
  }

  public Integer getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(Integer processStatus) {
    this.processStatus = processStatus;
  }
}
