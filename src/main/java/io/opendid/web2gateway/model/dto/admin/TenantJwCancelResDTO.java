package io.opendid.web2gateway.model.dto.admin;

public class TenantJwCancelResDTO {

  private String canceledJwtToken;

  public String getCanceledJwtToken() {
    return canceledJwtToken;
  }

  public void setCanceledJwtToken(String canceledJwtToken) {
    this.canceledJwtToken = canceledJwtToken;
  }
}
