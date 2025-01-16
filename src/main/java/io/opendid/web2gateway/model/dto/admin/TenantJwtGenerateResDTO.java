package io.opendid.web2gateway.model.dto.admin;

import java.util.Date;

public class TenantJwtGenerateResDTO {

  private String jwtToken;
  private String subId;
  private Date expTime;

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getSubId() {
    return subId;
  }

  public void setSubId(String subId) {
    this.subId = subId;
  }

  public Date getExpTime() {
    return expTime;
  }

  public void setExpTime(Date expTime) {
    this.expTime = expTime;
  }
}
