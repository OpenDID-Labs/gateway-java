package io.opendid.web2gateway.repository.model;

public class OdOracleContractEventlogWithBLOBs extends OdOracleContractEventlog {
    private String errorMsg;

    private String cancelErrorMsg;

    private String requestBody;

    private String responseBody;

    private String cancelRequestBody;

    private String cancelResponseBody;

    private String claimErrorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public String getCancelErrorMsg() {
        return cancelErrorMsg;
    }

    public void setCancelErrorMsg(String cancelErrorMsg) {
        this.cancelErrorMsg = cancelErrorMsg == null ? null : cancelErrorMsg.trim();
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

    public String getCancelRequestBody() {
        return cancelRequestBody;
    }

    public void setCancelRequestBody(String cancelRequestBody) {
        this.cancelRequestBody = cancelRequestBody == null ? null : cancelRequestBody.trim();
    }

    public String getCancelResponseBody() {
        return cancelResponseBody;
    }

    public void setCancelResponseBody(String cancelResponseBody) {
        this.cancelResponseBody = cancelResponseBody == null ? null : cancelResponseBody.trim();
    }

    public String getClaimErrorMsg() {
        return claimErrorMsg;
    }

    public void setClaimErrorMsg(String claimErrorMsg) {
        this.claimErrorMsg = claimErrorMsg == null ? null : claimErrorMsg.trim();
    }
}