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

    private Integer cancelStatus;

    private String cancelOracleHash;

    private Long cancelNextExecuteTime;

    private Integer cancelExecuteCount;

    private String requestAptosVersion;

    private String cancelAptosVersion;

    private Date cancelCreateDate;

    private String transactionBatchCode;

    private Integer claimStatus;

    private Long claimExecuteTime;

    private Integer claimExecuteCount;

    private String keyCode;

    private Long claimFee;

    private String subId;

    private String consumerAddress;

    private Integer coinType;

    private Integer paymentType;

    private String userPayFee;

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

    public Integer getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(Integer cancelStatus) {
        this.cancelStatus = cancelStatus;
    }

    public String getCancelOracleHash() {
        return cancelOracleHash;
    }

    public void setCancelOracleHash(String cancelOracleHash) {
        this.cancelOracleHash = cancelOracleHash == null ? null : cancelOracleHash.trim();
    }

    public Long getCancelNextExecuteTime() {
        return cancelNextExecuteTime;
    }

    public void setCancelNextExecuteTime(Long cancelNextExecuteTime) {
        this.cancelNextExecuteTime = cancelNextExecuteTime;
    }

    public Integer getCancelExecuteCount() {
        return cancelExecuteCount;
    }

    public void setCancelExecuteCount(Integer cancelExecuteCount) {
        this.cancelExecuteCount = cancelExecuteCount;
    }

    public String getRequestAptosVersion() {
        return requestAptosVersion;
    }

    public void setRequestAptosVersion(String requestAptosVersion) {
        this.requestAptosVersion = requestAptosVersion == null ? null : requestAptosVersion.trim();
    }

    public String getCancelAptosVersion() {
        return cancelAptosVersion;
    }

    public void setCancelAptosVersion(String cancelAptosVersion) {
        this.cancelAptosVersion = cancelAptosVersion == null ? null : cancelAptosVersion.trim();
    }

    public Date getCancelCreateDate() {
        return cancelCreateDate;
    }

    public void setCancelCreateDate(Date cancelCreateDate) {
        this.cancelCreateDate = cancelCreateDate;
    }

    public String getTransactionBatchCode() {
        return transactionBatchCode;
    }

    public void setTransactionBatchCode(String transactionBatchCode) {
        this.transactionBatchCode = transactionBatchCode == null ? null : transactionBatchCode.trim();
    }

    public Integer getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(Integer claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Long getClaimExecuteTime() {
        return claimExecuteTime;
    }

    public void setClaimExecuteTime(Long claimExecuteTime) {
        this.claimExecuteTime = claimExecuteTime;
    }

    public Integer getClaimExecuteCount() {
        return claimExecuteCount;
    }

    public void setClaimExecuteCount(Integer claimExecuteCount) {
        this.claimExecuteCount = claimExecuteCount;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode == null ? null : keyCode.trim();
    }

    public Long getClaimFee() {
        return claimFee;
    }

    public void setClaimFee(Long claimFee) {
        this.claimFee = claimFee;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId == null ? null : subId.trim();
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress == null ? null : consumerAddress.trim();
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getUserPayFee() {
        return userPayFee;
    }

    public void setUserPayFee(String userPayFee) {
        this.userPayFee = userPayFee == null ? null : userPayFee.trim();
    }
}