package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class GatewayKeyVault {
    private Integer keyId;

    private String servicePublicKey;

    private String walletPublicKey;

    private String walletAddress;

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

    public String getWalletPublicKey() {
        return walletPublicKey;
    }

    public void setWalletPublicKey(String walletPublicKey) {
        this.walletPublicKey = walletPublicKey == null ? null : walletPublicKey.trim();
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress == null ? null : walletAddress.trim();
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