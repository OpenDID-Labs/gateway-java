package io.opendid.web2gateway.model.dto.oracle;

public class UpdateCancelEventLogDTO {

    private String requestId;


    private Integer cancelStatus;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(Integer cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

}
