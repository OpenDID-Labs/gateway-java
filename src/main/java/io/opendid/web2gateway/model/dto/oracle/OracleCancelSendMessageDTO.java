package io.opendid.web2gateway.model.dto.oracle;

import java.util.LinkedHashMap;

public class OracleCancelSendMessageDTO {

  private LinkedHashMap<String, Object> linkedHashMap;
  private Long id;
  private String method;
  private String jobId;

  private String vnCode;

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

  public LinkedHashMap<String, Object> getLinkedHashMap() {
    return linkedHashMap;
  }

  public void setLinkedHashMap(LinkedHashMap<String, Object> linkedHashMap) {
    this.linkedHashMap = linkedHashMap;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}
