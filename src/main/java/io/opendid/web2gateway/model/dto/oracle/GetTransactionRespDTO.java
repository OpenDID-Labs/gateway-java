package io.opendid.web2gateway.model.dto.oracle;

/**
*  GetTransactionRespDTO
* @author Dong-Jianguo
* @Date: 2024/12/16
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class GetTransactionRespDTO {

  private String jobId;

  private String requestId;

  private String errorMessage;

  private Integer status;

  private String oracleRequestTxHash;

  private String requestDate;

  private Integer claimStatus;

  private Object claimBody;

  private Object responseBody;

  public String getOracleRequestTxHash() {
    return oracleRequestTxHash;
  }

  public void setOracleRequestTxHash(String oracleRequestTxHash) {
    this.oracleRequestTxHash = oracleRequestTxHash;
  }

  public String getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }





  public Integer getClaimStatus() {
    return claimStatus;
  }

  public void setClaimStatus(Integer claimStatus) {
    this.claimStatus = claimStatus;
  }

  public Object getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(Object responseBody) {
    this.responseBody = responseBody;
  }

    public Object getClaimBody() {
        return claimBody;
    }

    public void setClaimBody(Object claimBody) {
        this.claimBody = claimBody;
    }
}
