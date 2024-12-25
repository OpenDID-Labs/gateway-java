package io.opendid.web2gateway.model.dto.OAuth2;

public class OAuth2RegisterDTO {

  private String vnRouteUrl;

  private String publicKey;

  private String signature;

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getVnRouteUrl() {
    return vnRouteUrl;
  }

  public void setVnRouteUrl(String vnRouteUrl) {
    this.vnRouteUrl = vnRouteUrl;
  }
}
