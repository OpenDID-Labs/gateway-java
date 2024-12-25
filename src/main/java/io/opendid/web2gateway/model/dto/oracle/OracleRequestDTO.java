package io.opendid.web2gateway.model.dto.oracle;



/**
 *  OracleRequestDTO
 * @author Dong-Jianguo
 * @Date: 2024/12/13
 * @version 1.0.0
 *
 * @history date, modifier,and description
 **/
public class OracleRequestDTO {

  private String signedTx;

  private OracleRequestMetaDataDTO metaData;

  public String getSignedTx() {
    return signedTx;
  }

  public void setSignedTx(String signedTx) {
    this.signedTx = signedTx;
  }

  public OracleRequestMetaDataDTO getMetaData() {
    return metaData;
  }

  public void setMetaData(OracleRequestMetaDataDTO metaData) {
    this.metaData = metaData;
  }
}
