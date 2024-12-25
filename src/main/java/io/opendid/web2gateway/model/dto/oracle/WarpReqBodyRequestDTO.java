package io.opendid.web2gateway.model.dto.oracle;

/**
*  WaitSignDataRequestDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class WarpReqBodyRequestDTO {

  private String jobId;

  private String publicKey;

  private String nonce;

  private String data;

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
