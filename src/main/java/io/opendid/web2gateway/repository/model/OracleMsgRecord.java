package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class OracleMsgRecord {
    private Long msgId;

    private String vnCode;

    private String requestId;

    private Date createDate;

    private Date updateDate;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode == null ? null : vnCode.trim();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId == null ? null : requestId.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}