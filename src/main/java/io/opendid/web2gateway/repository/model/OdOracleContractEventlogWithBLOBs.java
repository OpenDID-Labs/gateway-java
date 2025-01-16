package io.opendid.web2gateway.repository.model;

public class OdOracleContractEventlogWithBLOBs extends OdOracleContractEventlog {
    private String errorMsg;

    private String cancelErrorMsg;

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
}