package io.opendid.web2gateway.model.dto.oracle;

public class OracleChainMsgDTO {

  private String jobId;
  private String data;
  private Long nonce;

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Long getNonce() {
    return nonce;
  }

  public void setNonce(Long nonce) {
    this.nonce = nonce;
  }
}
