package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.ClaimRecordMapper;
import io.opendid.web2gateway.repository.model.ClaimRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimRecordDataService {

  @Autowired
  private ClaimRecordMapper claimRecordMapper;

  public int insertSelective(ClaimRecord record){
    return claimRecordMapper.insertSelective(record);
  }

}
