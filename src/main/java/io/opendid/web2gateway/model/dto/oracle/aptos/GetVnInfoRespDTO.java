package io.opendid.web2gateway.model.dto.oracle.aptos;

/**
*  GetVnInfoRespDTO
* @author Dong-Jianguo
* @Date: 2024/12/16
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class GetVnInfoRespDTO {


  private String vnCode;

  /**
   * for home chain
   */
  private String publicKey;


    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }
}
