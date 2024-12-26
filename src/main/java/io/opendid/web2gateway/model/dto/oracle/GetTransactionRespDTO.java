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

  private Object responseBody;

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
