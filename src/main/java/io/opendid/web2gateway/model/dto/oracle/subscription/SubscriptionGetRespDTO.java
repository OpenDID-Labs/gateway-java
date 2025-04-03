package io.opendid.web2gateway.model.dto.oracle.subscription;

import java.util.List;

public class SubscriptionGetRespDTO {

    private List<SubscriptionInfoDTO> subIds;

    public List<SubscriptionInfoDTO> getSubIds() {
        return subIds;
    }

    public void setSubIds(List<SubscriptionInfoDTO> subIds) {
        this.subIds = subIds;
    }
}
