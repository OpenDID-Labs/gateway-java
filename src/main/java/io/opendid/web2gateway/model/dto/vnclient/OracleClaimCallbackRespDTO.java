package io.opendid.web2gateway.model.dto.vnclient;

public class OracleClaimCallbackRespDTO {
    
    private Integer successfully;

    private String callbackMessage;

    public Integer getSuccessfully() {
        return successfully;
    }

    public void setSuccessfully(Integer successfully) {
        this.successfully = successfully;
    }

    public String getCallbackMessage() {
        return callbackMessage;
    }

    public void setCallbackMessage(String callbackMessage) {
        this.callbackMessage = callbackMessage;
    }
}
