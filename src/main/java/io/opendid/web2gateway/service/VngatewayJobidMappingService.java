package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.VngatewayJobidMappingMapper;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*  VngatewayJobidMappingService
* @author Dong-Jianguo
* @Date: 2025/1/8
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Service
public class VngatewayJobidMappingService {

  @Autowired
  private VngatewayJobidMappingMapper vngatewayJobidMappingMapper;


  public void deleteAll(){
    vngatewayJobidMappingMapper.deleteAll();
  }


  public void insertJobIdMapping(VngatewayJobidMapping vngatewayJobidMapping){
    vngatewayJobidMappingMapper.insertSelective(vngatewayJobidMapping);

  }


  public List<VngatewayJobidMapping> searchByJobId(String jobId){

    List<VngatewayJobidMapping> results = vngatewayJobidMappingMapper.selectByJobId(jobId);

    return results;
  }

}
