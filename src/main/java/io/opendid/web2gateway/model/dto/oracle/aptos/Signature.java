package io.opendid.web2gateway.model.dto.oracle.aptos;

public class Signature {

  private Object publicKey;

  private Object sender;

  private Object signature;

  private String type;

  public Object getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(Object publicKey) {
    this.publicKey = publicKey;
  }

  public Object getSender() {
    return sender;
  }

  public void setSender(Object sender) {
    this.sender = sender;
  }

  public Object getSignature() {
    return signature;
  }

  public void setSignature(Object signature) {
    this.signature = signature;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
