package io.opendid.web2gateway.model.dto.OAuth2;

public class GetTokenReqDTO {

  private String vnRouteUrl;

  private String clientId;

  private String clientSecret;

  private String grantType;

  private String scope;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  public String getGrantType() {
    return grantType;
  }

  public void setGrantType(String grantType) {
    this.grantType = grantType;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getVnRouteUrl() {
    return vnRouteUrl;
  }

  public void setVnRouteUrl(String vnRouteUrl) {
    this.vnRouteUrl = vnRouteUrl;
  }
}
