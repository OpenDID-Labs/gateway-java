package io.opendid.web2gateway.repository.model;

public class OracleMsgRecordWithBLOBs extends OracleMsgRecord {
    private String requestBody;

    private String responseBody;

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