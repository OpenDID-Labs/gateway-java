package io.opendid.web2gateway.model.dto.admin;

import java.util.Date;

public class TenantGenerateAccessTokenResDTO {

    private String jwtToken;

    private Date expTime;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Date getExpTime() {
        return expTime;
    }

    public void setExpTime(Date expTime) {
        this.expTime = expTime;
    }
}
