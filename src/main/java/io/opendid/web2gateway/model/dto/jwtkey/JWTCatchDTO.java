package io.opendid.web2gateway.model.dto.jwtkey;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JWTCatchDTO {

  private String ID;
  private PrivateKey privateKey;
  private PublicKey publicKey;

  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }

  public PublicKey getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }
}
