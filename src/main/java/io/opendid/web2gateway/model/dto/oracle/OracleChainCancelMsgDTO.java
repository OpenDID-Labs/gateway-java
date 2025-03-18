package io.opendid.web2gateway.model.dto.oracle;

public class OracleChainCancelMsgDTO {

  private String jobId;
  private String requestId;
  private Long nonce;

  private String vnCode;

  private String keyCode;

  public String getVnCode() {
    return vnCode;
  }

  public void setVnCode(String vnCode) {
    this.vnCode = vnCode;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public Long getNonce() {
    return nonce;
  }

  public void setNonce(Long nonce) {
    this.nonce = nonce;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }
}
