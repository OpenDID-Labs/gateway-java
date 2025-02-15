package io.opendid.web2gateway.model.dto.OAuth2;

public class OAuth2ClientDTO {

  private String clientId;

  private String clientSecret;

  private String vnPublicKey;

  public String getVnPublicKey() {
    return vnPublicKey;
  }

  public void setVnPublicKey(String vnPublicKey) {
    this.vnPublicKey = vnPublicKey;
  }

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
}
