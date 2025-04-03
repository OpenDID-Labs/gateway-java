package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.SubscriptionInfo;

import java.util.List;

public interface SubscriptionInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SubscriptionInfo record);

    int insertSelective(SubscriptionInfo record);

    SubscriptionInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SubscriptionInfo record);

    int updateByPrimaryKey(SubscriptionInfo record);

    List<SubscriptionInfo> selectBySubIds(List<String> subIds);

    SubscriptionInfo selectBySubId(String subId);
    List<SubscriptionInfo> selectAllSubIds();



    int updateBySubIdSelective(SubscriptionInfo record);


}