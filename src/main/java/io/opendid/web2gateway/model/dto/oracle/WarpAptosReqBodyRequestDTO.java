package io.opendid.web2gateway.model.dto.oracle;

/**
*  WaitSignDataRequestDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class WarpAptosReqBodyRequestDTO {

  private String jobId;

  private String publicKey;

  private String nonce;

  private String data;

  private String vnCode;

  private Boolean generateClaim;

  public Boolean getGenerateClaim() {
    return generateClaim;
  }

  public void setGenerateClaim(Boolean generateClaim) {
    this.generateClaim = generateClaim;
  }

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
