package io.opendid.web2gateway.model.dto;

public class SelectVnListDTO {

  private String jobId;

  private Object vnList;

  private Integer vnCount;

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public Object getVnList() {
    return vnList;
  }

  public void setVnList(Object vnList) {
    this.vnList = vnList;
  }

  public Integer getVnCount() {
    return vnCount;
  }

  public void setVnCount(Integer vnCount) {
    this.vnCount = vnCount;
  }
}
