package io.opendid.web2gateway.model.dto.oracle;

public class OracleNonceUpdateDTO {

  private String publicKey;

  private String address;


  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
