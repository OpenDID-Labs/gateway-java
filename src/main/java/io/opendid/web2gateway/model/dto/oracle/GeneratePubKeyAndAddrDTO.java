package io.opendid.web2gateway.model.dto.oracle;

/**
*  GeneratePubKeyAndAddrDTO
* @author Dong-Jianguo
* @Date: 2024/12/16
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class GeneratePubKeyAndAddrDTO {

  private String publicKey;

  private String walletAddress;

  private String vnCode;

  private String privateKey;

  private String privateKeyCode;

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getVnCode() {
    return vnCode;
  }

  public void setVnCode(String vnCode) {
    this.vnCode = vnCode;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public void setWalletAddress(String walletAddress) {
    this.walletAddress = walletAddress;
  }

    public String getPrivateKeyCode() {
        return privateKeyCode;
    }

    public void setPrivateKeyCode(String privateKeyCode) {
        this.privateKeyCode = privateKeyCode;
    }
}
