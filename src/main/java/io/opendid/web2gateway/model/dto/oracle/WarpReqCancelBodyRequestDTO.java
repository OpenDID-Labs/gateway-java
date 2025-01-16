package io.opendid.web2gateway.model.dto.oracle;

/**
*  WaitSignDataRequestDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class WarpReqCancelBodyRequestDTO {

  private String requestId;

  private String publicKey;

  private String nonce;

  private String data;


  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
