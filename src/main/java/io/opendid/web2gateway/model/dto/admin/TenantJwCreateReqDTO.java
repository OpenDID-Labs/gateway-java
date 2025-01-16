package io.opendid.web2gateway.model.dto.admin;

import java.util.Date;

public class TenantJwCreateReqDTO {

  private String subject;
  private Long exp;

  private String subjectId;

  private String jwt;

  private Date expTime;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public Long getExp() {
    return exp;
  }

  public void setExp(Long exp) {
    this.exp = exp;
  }

  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public Date getExpTime() {
    return expTime;
  }

  public void setExpTime(Date expTime) {
    this.expTime = expTime;
  }
}
