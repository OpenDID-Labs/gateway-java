package io.opendid.web2gateway.repository.model;

public class OracleRequestV2RecordWithBLOBs extends OracleRequestV2Record {
    private String errorMsg;

    private String requestBody;

    private String responseBody;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody == null ? null : requestBody.trim();
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody == null ? null : responseBody.trim();
    }
}
