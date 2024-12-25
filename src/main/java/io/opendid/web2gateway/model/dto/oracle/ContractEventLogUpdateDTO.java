package io.opendid.web2gateway.model.dto.oracle;

/**
*  ContractEventLogUpdateDTO
* @author Dong-Jianguo
* @Date: 2024/12/18
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class ContractEventLogUpdateDTO {


  private String requestId;

  private String callbackOracleHash;

  private Integer processStatus;


  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getCallbackOracleHash() {
    return callbackOracleHash;
  }

  public void setCallbackOracleHash(String callbackOracleHash) {
    this.callbackOracleHash = callbackOracleHash;
  }

  public Integer getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(Integer processStatus) {
    this.processStatus = processStatus;
  }
}
