package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.SubscriptionConsumer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubscriptionConsumerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SubscriptionConsumer record);

    int insertSelective(SubscriptionConsumer record);

    SubscriptionConsumer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SubscriptionConsumer record);

    int updateByPrimaryKey(SubscriptionConsumer record);

    SubscriptionConsumer selectBySubIdAndAddress(@Param("subId") String subId, @Param("address")String address);

    List<SubscriptionConsumer> selectAllBySubId(@Param("subId") String subId);
}