package io.opendid.web2gateway.model.dto.oracle;

public class OracleWrapRequestBodyDTO {

    private String subId;

    private String jobId;

    private String publicKey;

    private String data;

    private String vnCode;

    private Boolean generateClaim;

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }

    public Boolean getGenerateClaim() {
        return generateClaim;
    }

    public void setGenerateClaim(Boolean generateClaim) {
        this.generateClaim = generateClaim;
    }
}
