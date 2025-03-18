package io.opendid.web2gateway.model.dto.oracle;

/**
*  GetWalletAddressRespDTO
* @author Dong-Jianguo
* @Date: 2024/12/16
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class GetWalletAddressRespDTO {

  private String walletAddress;

  private String walletPublicKey;

  private String vnCode;

  public String getWalletPublicKey() {
    return walletPublicKey;
  }

  public void setWalletPublicKey(String walletPublicKey) {
    this.walletPublicKey = walletPublicKey;
  }

  public String getVnCode() {
    return vnCode;
  }

  public void setVnCode(String vnCode) {
    this.vnCode = vnCode;
  }

  public String getWalletAddress() {
    return walletAddress;
  }

  public void setWalletAddress(String walletAddress) {
    this.walletAddress = walletAddress;
  }
}

