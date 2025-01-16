package io.opendid.web2gateway.model.dto.oracle;

public class ContractEventLogUpdateForCancelDTO {

  private String requestId;

  private int cancelStatus;

  private String cancelOracleHash;

  private String cancelErrorMsg;

  private String requestBody;

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public int getCancelStatus() {
    return cancelStatus;
  }

  public void setCancelStatus(int cancelStatus) {
    this.cancelStatus = cancelStatus;
  }

  public String getCancelOracleHash() {
    return cancelOracleHash;
  }

  public void setCancelOracleHash(String cancelOracleHash) {
    this.cancelOracleHash = cancelOracleHash;
  }

  public String getCancelErrorMsg() {
    return cancelErrorMsg;
  }

  public void setCancelErrorMsg(String cancelErrorMsg) {
    this.cancelErrorMsg = cancelErrorMsg;
  }
}
