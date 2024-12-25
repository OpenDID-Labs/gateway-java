package io.opendid.web2gateway.model.jsonrpc2.method;

public class OracleRequestMethodReq {

  private String jobId;

  private Object data;

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
