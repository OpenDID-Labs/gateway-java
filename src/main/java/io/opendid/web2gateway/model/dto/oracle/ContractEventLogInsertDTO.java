package io.opendid.web2gateway.model.dto.oracle;

/**
*  ContractEventLogInsertDTO
* @author Dong-Jianguo
* @Date: 2024/12/18
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class ContractEventLogInsertDTO {

  private String jobId;

  private String requestId;

  private String keyCode;

  private String vnCode;

  private String platformCode;

  private Long jobIdFee;

  private Integer processStatus;

  private String traceId;

  private String requestOracleHash;

  private String requestAptosVersion;

  private String errorMsg;

  private String requestBody;

  private String transactionBatchCode;

  private Long claimFee;

  private Integer claimStatus;

  private String subId;

  private String consumerAddress;

  public String getConsumerAddress() {
    return consumerAddress;
  }

  public void setConsumerAddress(String consumerAddress) {
    this.consumerAddress = consumerAddress;
  }

  public String getSubId() {
    return subId;
  }

  public void setSubId(String subId) {
    this.subId = subId;
  }

  public Integer getClaimStatus() {
    return claimStatus;
  }

  public void setClaimStatus(Integer claimStatus) {
    this.claimStatus = claimStatus;
  }

  public Long getClaimFee() {
    return claimFee;
  }

  public void setClaimFee(Long claimFee) {
    this.claimFee = claimFee;
  }

  public String getTransactionBatchCode() {
    return transactionBatchCode;
  }

  public void setTransactionBatchCode(String transactionBatchCode) {
    this.transactionBatchCode = transactionBatchCode;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }

  public String getRequestAptosVersion() {
    return requestAptosVersion;
  }

  public void setRequestAptosVersion(String requestAptosVersion) {
    this.requestAptosVersion = requestAptosVersion;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getRequestOracleHash() {
    return requestOracleHash;
  }

  public void setRequestOracleHash(String requestOracleHash) {
    this.requestOracleHash = requestOracleHash;
  }

  public String getPlatformCode() {
    return platformCode;
  }

  public void setPlatformCode(String platformCode) {
    this.platformCode = platformCode;
  }

  public String getTraceId() {
    return traceId;
  }

  public void setTraceId(String traceId) {
    this.traceId = traceId;
  }

  public Integer getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(Integer processStatus) {
    this.processStatus = processStatus;
  }

  public Long getJobIdFee() {
    return jobIdFee;
  }

  public void setJobIdFee(Long jobIdFee) {
    this.jobIdFee = jobIdFee;
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

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }
}
