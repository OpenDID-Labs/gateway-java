package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.VngatewayJobidMappingMapper;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobidMappingDataService {

  @Autowired
  private VngatewayJobidMappingMapper JobidMappingMapper;

  public List<VngatewayJobidMapping> selectByJobId(String jobId) {
    return JobidMappingMapper.selectByJobId(jobId);
  }


}
