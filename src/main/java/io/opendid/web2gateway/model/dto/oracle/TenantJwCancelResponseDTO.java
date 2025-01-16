package io.opendid.web2gateway.model.dto.oracle;

public class TenantJwCancelResponseDTO {

    private String canceledJwtToken;

    public String getCanceledJwtToken() {
        return canceledJwtToken;
    }

    public void setCanceledJwtToken(String canceledJwtToken) {
        this.canceledJwtToken = canceledJwtToken;
    }
}
