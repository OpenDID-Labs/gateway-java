package io.opendid.web2gateway.model.dto.oracle;

public class OracleRequestMetaDataDTO {

  private String subId;

  private String jobId;

  private String vnCode;

  private String publicKey;

  private String callbackUrl;

  private String data;

  private Boolean generateClaim;

  private Long claimFee;

  private Long jobIdFee;

  private Boolean jobIdFree;

  private Boolean claimFree;

  public String getSubId() {
    return subId;
  }

  public void setSubId(String subId) {
    this.subId = subId;
  }

  public Boolean getJobIdFree() {
    return jobIdFree;
  }

  public void setJobIdFree(Boolean jobIdFree) {
    this.jobIdFree = jobIdFree;
  }

  public Boolean getClaimFree() {
    return claimFree;
  }

  public void setClaimFree(Boolean claimFree) {
    this.claimFree = claimFree;
  }

  public Long getJobIdFee() {
    return jobIdFee;
  }

  public void setJobIdFee(Long jobIdFee) {
    this.jobIdFee = jobIdFee;
  }

  public Long getClaimFee() {
    return claimFee;
  }

  public void setClaimFee(Long claimFee) {
    this.claimFee = claimFee;
  }

  public Boolean getGenerateClaim() {
    return generateClaim;
  }

  public void setGenerateClaim(Boolean generateClaim) {
    this.generateClaim = generateClaim;
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

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }
}
