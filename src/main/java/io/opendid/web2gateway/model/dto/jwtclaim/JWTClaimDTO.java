package io.opendid.web2gateway.model.dto.jwtclaim;

public class JWTClaimDTO {

  public static final String SUB_ID ="subId";
  public static final String ADMIN = "admin";

  public static final String FIELD1 = "field1";
  public static final String FIELD2 = "field2";
  public static final String FIELD3 = "field3";


  private String subjectId;
  private boolean admin;
  private String field1;
  private String field2;
  private String field3;

  public String getField1() {
    return field1;
  }

  public void setField1(String field1) {
    this.field1 = field1;
  }

  public String getField2() {
    return field2;
  }

  public void setField2(String field2) {
    this.field2 = field2;
  }

  public String getField3() {
    return field3;
  }

  public void setField3(String field3) {
    this.field3 = field3;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }
}
