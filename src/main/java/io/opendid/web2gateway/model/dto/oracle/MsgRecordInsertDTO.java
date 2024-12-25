package io.opendid.web2gateway.model.dto.oracle;

/**
*  MsgRecordInsertDTO
* @author Dong-Jianguo
* @Date: 2024/12/18
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class MsgRecordInsertDTO {

  private String vnCode;

  private String requestId;

  private String requestBody;


  public String getVnCode() {
    return vnCode;
  }

  public void setVnCode(String vnCode) {
    this.vnCode = vnCode;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getRequestBody() {
    return requestBody;
  }

  public void setRequestBody(String requestBody) {
    this.requestBody = requestBody;
  }
}
