package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class OdOracleContractEventlog {
    private Long logId;

    private String requestId;

    private String vnCode;

    private String jobId;

    private Long jobIdFee;

    private String platformCode;

    private Integer processStatus;

    private String callbackOracleHash;

    private String traceId;

    private Date createDate;

    private Date updateDate;

    private Long nextExecuteTime;

    private Integer executeCount;

    private String requestOracleHash;

    private String errorMsg;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId == null ? null : requestId.trim();
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode == null ? null : vnCode.trim();
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    public Long getJobIdFee() {
        return jobIdFee;
    }

    public void setJobIdFee(Long jobIdFee) {
        this.jobIdFee = jobIdFee;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode == null ? null : platformCode.trim();
    }

    public Integer getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        this.processStatus = processStatus;
    }

    public String getCallbackOracleHash() {
        return callbackOracleHash;
    }

    public void setCallbackOracleHash(String callbackOracleHash) {
        this.callbackOracleHash = callbackOracleHash == null ? null : callbackOracleHash.trim();
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId == null ? null : traceId.trim();
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

    public Long getNextExecuteTime() {
        return nextExecuteTime;
    }

    public void setNextExecuteTime(Long nextExecuteTime) {
        this.nextExecuteTime = nextExecuteTime;
    }

    public Integer getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(Integer executeCount) {
        this.executeCount = executeCount;
    }

    public String getRequestOracleHash() {
        return requestOracleHash;
    }

    public void setRequestOracleHash(String requestOracleHash) {
        this.requestOracleHash = requestOracleHash == null ? null : requestOracleHash.trim();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }
}