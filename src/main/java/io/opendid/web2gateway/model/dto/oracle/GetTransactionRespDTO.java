package io.opendid.web2gateway.model.dto.oracle;

import java.util.Date;
import javax.xml.crypto.Data;

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

  private String oracleRequestHash;

  private String requestDate;

  private Object responseBody;

  public String getOracleRequestHash() {
    return oracleRequestHash;
  }

  public void setOracleRequestHash(String oracleRequestHash) {
    this.oracleRequestHash = oracleRequestHash;
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

  public Object getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(Object responseBody) {
    this.responseBody = responseBody;
  }
}
