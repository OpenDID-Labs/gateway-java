package io.opendid.web2gateway.repository.model;

import java.math.BigDecimal;
import java.util.Date;

public class SubscriptionTransferTokenRecord {
    private Long transferId;

    private String subId;

    private String fromAddress;

    private String toAddress;

    private BigDecimal tokenAmounts;

    private BigDecimal latestBalance;

    private BigDecimal oldBalance;

    private String requestVnCode;

    private String signAddress;

    private String signKeyCode;

    private Date createDate;

    private Date updateDate;

    private String transactionHash;

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId == null ? null : subId.trim();
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress == null ? null : fromAddress.trim();
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress == null ? null : toAddress.trim();
    }

    public BigDecimal getTokenAmounts() {
        return tokenAmounts;
    }

    public void setTokenAmounts(BigDecimal tokenAmounts) {
        this.tokenAmounts = tokenAmounts;
    }

    public BigDecimal getLatestBalance() {
        return latestBalance;
    }

    public void setLatestBalance(BigDecimal latestBalance) {
        this.latestBalance = latestBalance;
    }

    public BigDecimal getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(BigDecimal oldBalance) {
        this.oldBalance = oldBalance;
    }

    public String getRequestVnCode() {
        return requestVnCode;
    }

    public void setRequestVnCode(String requestVnCode) {
        this.requestVnCode = requestVnCode == null ? null : requestVnCode.trim();
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress == null ? null : signAddress.trim();
    }

    public String getSignKeyCode() {
        return signKeyCode;
    }

    public void setSignKeyCode(String signKeyCode) {
        this.signKeyCode = signKeyCode == null ? null : signKeyCode.trim();
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

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash == null ? null : transactionHash.trim();
    }
}