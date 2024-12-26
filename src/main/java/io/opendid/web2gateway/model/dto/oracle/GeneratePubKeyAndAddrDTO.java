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
}
