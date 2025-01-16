package io.opendid.web2gateway.model.dto.oracle;



/**
*  OracleCallBackRespDTO
* @author Dong-Jianguo
* @Date: 2024/12/17
* @version 1.0.0
*
* @history date, modifier,and description
**/

public class OracleCallBackResultDTO {

  private String requestId;

  private String oracleFulfillTxHash;

  private String data;

  private String signData;

  private Integer status;

  public String getSignData() {
    return signData;
  }

  public void setSignData(String signData) {
    this.signData = signData;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getOracleFulfillTxHash() {
    return oracleFulfillTxHash;
  }

  public void setOracleFulfillTxHash(String oracleFulfillTxHash) {
    this.oracleFulfillTxHash = oracleFulfillTxHash;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
