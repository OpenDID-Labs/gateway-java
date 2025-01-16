package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class TenantJwtManger {
    private Long tenantJwtId;

    private String subjectId;

    private String subjectName;

    private String jwt;

    private Date createDate;

    private Date updateDate;

    private Date expedDate;

    public Long getTenantJwtId() {
        return tenantJwtId;
    }

    public void setTenantJwtId(Long tenantJwtId) {
        this.tenantJwtId = tenantJwtId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId == null ? null : subjectId.trim();
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName == null ? null : subjectName.trim();
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt == null ? null : jwt.trim();
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

    public Date getExpedDate() {
        return expedDate;
    }

    public void setExpedDate(Date expedDate) {
        this.expedDate = expedDate;
    }
}