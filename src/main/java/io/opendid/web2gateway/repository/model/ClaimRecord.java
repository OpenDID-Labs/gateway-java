package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class ClaimRecord {
    private Long claimRecordId;

    private String chainName;

    private String requestId;

    private String claimId;

    private String issuer;

    private String requestTxnHash;

    private String requestDataHash;

    private String responseTxnHash;

    private String responseDataHash;

    private String customizeHash;

    private String idSystem;

    private String issuanceDate;

    private String expirationDate;

    private String signature;

    private String transactionHash;

    private String version;

    private Date createDate;

    private Date updateDate;

    private String contractParams;

    public Long getClaimRecordId() {
        return claimRecordId;
    }

    public void setClaimRecordId(Long claimRecordId) {
        this.claimRecordId = claimRecordId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName == null ? null : chainName.trim();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId == null ? null : requestId.trim();
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId == null ? null : claimId.trim();
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer == null ? null : issuer.trim();
    }

    public String getRequestTxnHash() {
        return requestTxnHash;
    }

    public void setRequestTxnHash(String requestTxnHash) {
        this.requestTxnHash = requestTxnHash == null ? null : requestTxnHash.trim();
    }

    public String getRequestDataHash() {
        return requestDataHash;
    }

    public void setRequestDataHash(String requestDataHash) {
        this.requestDataHash = requestDataHash == null ? null : requestDataHash.trim();
    }

    public String getResponseTxnHash() {
        return responseTxnHash;
    }

    public void setResponseTxnHash(String responseTxnHash) {
        this.responseTxnHash = responseTxnHash == null ? null : responseTxnHash.trim();
    }

    public String getResponseDataHash() {
        return responseDataHash;
    }

    public void setResponseDataHash(String responseDataHash) {
        this.responseDataHash = responseDataHash == null ? null : responseDataHash.trim();
    }

    public String getCustomizeHash() {
        return customizeHash;
    }

    public void setCustomizeHash(String customizeHash) {
        this.customizeHash = customizeHash == null ? null : customizeHash.trim();
    }

    public String getIdSystem() {
        return idSystem;
    }

    public void setIdSystem(String idSystem) {
        this.idSystem = idSystem == null ? null : idSystem.trim();
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate == null ? null : issuanceDate.trim();
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate == null ? null : expirationDate.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash == null ? null : transactionHash.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
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

    public String getContractParams() {
        return contractParams;
    }

    public void setContractParams(String contractParams) {
        this.contractParams = contractParams == null ? null : contractParams.trim();
    }
}