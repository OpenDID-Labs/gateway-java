package io.opendid.web2gateway.model.dto.oracle;

public class OracleCallbackClaimDTO {

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

    private String contractParams;

    private Integer status;

    private String signData;

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getRequestTxnHash() {
        return requestTxnHash;
    }

    public void setRequestTxnHash(String requestTxnHash) {
        this.requestTxnHash = requestTxnHash;
    }

    public String getRequestDataHash() {
        return requestDataHash;
    }

    public void setRequestDataHash(String requestDataHash) {
        this.requestDataHash = requestDataHash;
    }

    public String getResponseTxnHash() {
        return responseTxnHash;
    }

    public void setResponseTxnHash(String responseTxnHash) {
        this.responseTxnHash = responseTxnHash;
    }

    public String getResponseDataHash() {
        return responseDataHash;
    }

    public void setResponseDataHash(String responseDataHash) {
        this.responseDataHash = responseDataHash;
    }

    public String getCustomizeHash() {
        return customizeHash;
    }

    public void setCustomizeHash(String customizeHash) {
        this.customizeHash = customizeHash;
    }

    public String getIdSystem() {
        return idSystem;
    }

    public void setIdSystem(String idSystem) {
        this.idSystem = idSystem;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContractParams() {
        return contractParams;
    }

    public void setContractParams(String contractParams) {
        this.contractParams = contractParams;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }

}
