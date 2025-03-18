package io.opendid.web2gateway.model.dto.oracle;

public class EventLogPendingOutputDTO {

  private Long logId;

  private String requestId;

  private String vnCode;

  private Long nextExecuteTime;

  private Integer executeCount;

  private String jobId;

  private String requestAptosVersion;

  private String requestOracleHash;

  public String getRequestAptosVersion() {
    return requestAptosVersion;
  }

  public void setRequestAptosVersion(String requestAptosVersion) {
    this.requestAptosVersion = requestAptosVersion;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
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

  public String getVnCode() {
    return vnCode;
  }

  public void setVnCode(String vnCode) {
    this.vnCode = vnCode;
  }

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

    public String getRequestOracleHash() {
        return requestOracleHash;
    }

    public void setRequestOracleHash(String requestOracleHash) {
        this.requestOracleHash = requestOracleHash;
    }
}
