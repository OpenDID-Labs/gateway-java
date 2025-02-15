package io.opendid.web2gateway.model.dto.oracle;

import java.util.Date;

/**
*  GetTransactionRespDTO
* @author Dong-Jianguo
* @Date: 2024/12/16
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class GetCancelTransactionRespDTO {

  private String jobId;

  private String requestId;

  private Integer status;

  private String cancelOracleHash;

  private String cancelDate;

  private String errorMessage;

  public String getCancelOracleHash() {
    return cancelOracleHash;
  }

  public void setCancelOracleHash(String cancelOracleHash) {
    this.cancelOracleHash = cancelOracleHash;
  }

  public String getCancelDate() {
    return cancelDate;
  }

  public void setCancelDate(String cancelDate) {
    this.cancelDate = cancelDate;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
