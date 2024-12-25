package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class VngatewayJobidMapping {
    private Long vnJobidMappingid;

    private String jobId;

    private String platformCode;

    private String vnCode;

    private Date updateDate;

    public Long getVnJobidMappingid() {
        return vnJobidMappingid;
    }

    public void setVnJobidMappingid(Long vnJobidMappingid) {
        this.vnJobidMappingid = vnJobidMappingid;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode == null ? null : platformCode.trim();
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode == null ? null : vnCode.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}