package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.SubscriptionTransferTokenRecord;
import java.util.List;

public interface SubscriptionTransferTokenRecordMapper {
    int deleteByPrimaryKey(Long transferId);

    int insert(SubscriptionTransferTokenRecord record);

    int insertSelective(SubscriptionTransferTokenRecord record);

    SubscriptionTransferTokenRecord selectByPrimaryKey(Long transferId);

    int updateByPrimaryKeySelective(SubscriptionTransferTokenRecord record);

    int updateByPrimaryKey(SubscriptionTransferTokenRecord record);

    List<SubscriptionTransferTokenRecord> selectWaitingResultData();

}