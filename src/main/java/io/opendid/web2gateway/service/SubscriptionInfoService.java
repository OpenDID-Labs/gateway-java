package io.opendid.web2gateway.service;

import io.opendid.web2gateway.model.dto.oracle.subscription.SubscriptionInfoDTO;
import io.opendid.web2gateway.repository.mapper.SubscriptionInfoMapper;
import io.opendid.web2gateway.repository.model.SubscriptionInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionInfoService {


    @Resource
    private SubscriptionInfoMapper subscriptionInfoMapper;

    public void insertSubInfo(SubscriptionInfo subscriptionInfo) {

        subscriptionInfoMapper.insertSelective(subscriptionInfo);
    }

    public List<SubscriptionInfo> selectBySubIdList(List<String> subIds) {

        return subscriptionInfoMapper.selectBySubIds(subIds);
    }

    public void updateSubIdBalance(List<SubscriptionInfoDTO> subscriptionInfoDTOList) {

        for (SubscriptionInfoDTO subscriptionInfoDTO : subscriptionInfoDTOList) {
            SubscriptionInfo subscriptionInfo = new SubscriptionInfo();
            subscriptionInfo.setSubId(subscriptionInfoDTO.getSubId());
            subscriptionInfo.setBalance(subscriptionInfoDTO.getLatestBalance().toString());
            subscriptionInfo.setUpdateDate(new Date());
            subscriptionInfoMapper.updateBySubIdSelective(subscriptionInfo);
        }

    }
}
