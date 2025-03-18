package io.opendid.web2gateway.model.dto.oracle;

/**
*
* @author Dong-Jianguo
* @Date: 2025/2/19
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class SelectJobIdMappingDTO {

  private String jobId;

  private String vnCode;

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public String getVnCode() {
    return vnCode;
  }

  public void setVnCode(String vnCode) {
    this.vnCode = vnCode;
  }
}
