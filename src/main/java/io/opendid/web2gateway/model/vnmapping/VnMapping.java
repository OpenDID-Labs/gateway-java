package io.opendid.web2gateway.model.vnmapping;

import java.util.List;

public class VnMapping {

  private String vncode;

  private String vnurl;

  private List<JobidMappin> jobidmapping;

  public List<JobidMappin> getJobidmapping() {
    return jobidmapping;
  }

  public void setJobidmapping(
      List<JobidMappin> jobidmapping) {
    this.jobidmapping = jobidmapping;
  }

  public String getVncode() {
    return vncode;
  }

  public void setVncode(String vncode) {
    this.vncode = vncode;
  }

  public String getVnurl() {
    return vnurl;
  }

  public void setVnurl(String vnurl) {
    this.vnurl = vnurl;
  }
}
