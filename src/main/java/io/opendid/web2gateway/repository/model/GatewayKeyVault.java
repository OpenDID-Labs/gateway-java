package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class GatewayKeyVault {
    private Integer keyId;

    private String servicePublicKey;

    private Date updateDate;

    private String adminJwt;

    public Integer getKeyId() {
        return keyId;
    }

    public void setKeyId(Integer keyId) {
        this.keyId = keyId;
    }

    public String getServicePublicKey() {
        return servicePublicKey;
    }

    public void setServicePublicKey(String servicePublicKey) {
        this.servicePublicKey = servicePublicKey == null ? null : servicePublicKey.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getAdminJwt() {
        return adminJwt;
    }

    public void setAdminJwt(String adminJwt) {
        this.adminJwt = adminJwt == null ? null : adminJwt.trim();
    }
}