package io.opendid.web2gateway.model.dto.vnclient;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public class VnClientJobIdDTO {

  private String jobId;

  private JsonRpc2Request requestBody;

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public JsonRpc2Request getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(JsonRpc2Request requestBody) {
    this.requestBody = requestBody;
  }
}