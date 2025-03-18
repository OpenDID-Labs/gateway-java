package io.opendid.web2gateway.model.dto.oracle;



/**
 *  OracleRequestRespDTO
 * @author Dong-Jianguo
 * @Date: 2024/12/13
 * @version 1.0.0
 *
 * @history date, modifier,and description
 **/
public class OracleRequestRespDTO {

//  private String requestId;

  private String oracleRequestTxHash;

//  private String aptosVersion;
//
//  public String getAptosVersion() {
//    return aptosVersion;
//  }
//
//  public void setAptosVersion(String aptosVersion) {
//    this.aptosVersion = aptosVersion;
//  }
//
//  public String getRequestId() {
//    return requestId;
//  }
//
//  public void setRequestId(String requestId) {
//    this.requestId = requestId;
//  }

  public String getOracleRequestTxHash() {
    return oracleRequestTxHash;
  }

  public void setOracleRequestTxHash(String oracleRequestTxHash) {
    this.oracleRequestTxHash = oracleRequestTxHash;
  }
}
