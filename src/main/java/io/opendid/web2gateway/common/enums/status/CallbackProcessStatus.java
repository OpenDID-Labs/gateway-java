package io.opendid.web2gateway.common.enums.status;

public enum CallbackProcessStatus {

    SUCCESSFUL(1),
    FAILED(2);

    private int code;

    CallbackProcessStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
