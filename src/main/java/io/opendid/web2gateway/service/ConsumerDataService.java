package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.SubscriptionConsumerMapper;
import io.opendid.web2gateway.repository.model.SubscriptionConsumer;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerDataService {

  @Autowired
  private SubscriptionConsumerMapper consumerMapper;

  public int updateStatus(SubscriptionConsumer subscriptionConsumer) {

    SubscriptionConsumer consumer = consumerMapper.selectBySubIdAndAddress(
        subscriptionConsumer.getSubId(),
        subscriptionConsumer.getConsumerAddress()
    );

    SubscriptionConsumer consumerData = new SubscriptionConsumer();
    consumerData.setLastTxHash(subscriptionConsumer.getLastTxHash());
    consumerData.setConsumerStatus(subscriptionConsumer.getConsumerStatus());

    if (consumer != null) {

      consumerData.setId(consumer.getId());
      consumerData.setUpdateDate(new Date());
      return consumerMapper.updateByPrimaryKeySelective(consumerData);

    } else {

      consumerData.setSubId(subscriptionConsumer.getSubId());
      consumerData.setConsumerAddress(subscriptionConsumer.getConsumerAddress());
      consumerData.setCreateDate(new Date());
      return consumerMapper.insertSelective(consumerData);
    }
  }


  public List<SubscriptionConsumer> selectAllBySubId(String subId) {
    return consumerMapper.selectAllBySubId(subId);
  }

}
