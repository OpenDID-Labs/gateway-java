package io.opendid.web2gateway.repository.model;

import java.util.Date;

public class VngatewayRouteInfo {
    private Long routeId;

    private String vnCode;

    private String url;

    private Integer sslEnabled;

    private Integer gatewayDisable;

    private String clientId;

    private String clientSecret;

    private Date updateDate;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode == null ? null : vnCode.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(Integer sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public Integer getGatewayDisable() {
        return gatewayDisable;
    }

    public void setGatewayDisable(Integer gatewayDisable) {
        this.gatewayDisable = gatewayDisable;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret == null ? null : clientSecret.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}