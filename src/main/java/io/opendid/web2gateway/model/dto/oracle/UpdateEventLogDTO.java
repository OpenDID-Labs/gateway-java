package io.opendid.web2gateway.model.dto.oracle;

public class UpdateEventLogDTO {

  private String requestId;

  private String responseBody;

  private Integer processStatus;

  private String callbackOracleHash;

  private String requestAptosVersion;

  public String getRequestAptosVersion() {
    return requestAptosVersion;
  }

  public void setRequestAptosVersion(String requestAptosVersion) {
    this.requestAptosVersion = requestAptosVersion;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

  public Integer getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(Integer processStatus) {
    this.processStatus = processStatus;
  }

  public String getCallbackOracleHash() {
    return callbackOracleHash;
  }

  public void setCallbackOracleHash(String callbackOracleHash) {
    this.callbackOracleHash = callbackOracleHash;
  }
}
