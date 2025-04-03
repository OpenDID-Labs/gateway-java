package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.SubscriptionTransferTokenRecordMapper;
import io.opendid.web2gateway.repository.model.SubscriptionTransferTokenRecord;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubTransferTokenDataService {

  @Autowired
  private SubscriptionTransferTokenRecordMapper subscriptionTransferTokenRecordMapper;


  public int insertSelective(SubscriptionTransferTokenRecord record) {
    return subscriptionTransferTokenRecordMapper.insertSelective(record);
  }

  public int updateByPrimaryKeySelective(SubscriptionTransferTokenRecord record){
    return subscriptionTransferTokenRecordMapper.updateByPrimaryKeySelective(record);
  }

  public List<SubscriptionTransferTokenRecord> selectWaitingResultData(){
    return subscriptionTransferTokenRecordMapper.selectWaitingResultData();
  }



}
