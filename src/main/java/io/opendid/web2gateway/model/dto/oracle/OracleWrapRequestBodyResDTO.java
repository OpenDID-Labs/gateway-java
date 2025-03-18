package io.opendid.web2gateway.model.dto.oracle;

public class OracleWrapRequestBodyResDTO {

    private Object wrapData;

    private Long jobIdFee;

    private Boolean jobIdFree;

    private Long claimFee;

    private Boolean claimFeeFree;

    public Boolean getJobIdFree() {
        return jobIdFree;
    }

    public void setJobIdFree(Boolean jobIdFree) {
        this.jobIdFree = jobIdFree;
    }

    public Boolean getClaimFeeFree() {
        return claimFeeFree;
    }

    public void setClaimFeeFree(Boolean claimFeeFree) {
        this.claimFeeFree = claimFeeFree;
    }

    public Long getJobIdFee() {
        return jobIdFee;
    }

    public void setJobIdFee(Long jobIdFee) {
        this.jobIdFee = jobIdFee;
    }

    public Long getClaimFee() {
        return claimFee;
    }

    public void setClaimFee(Long claimFee) {
        this.claimFee = claimFee;
    }



    public Object getWrapData() {
        return wrapData;
    }

    public void setWrapData(Object wrapData) {
        this.wrapData = wrapData;
    }
}
