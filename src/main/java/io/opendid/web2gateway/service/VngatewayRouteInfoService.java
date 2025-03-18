package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.VngatewayRouteInfoMapper;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
* VngatewayRouteInfoService
* @author Dong-Jianguo
* @Date: 2025/1/8
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Service
public class VngatewayRouteInfoService {


  @Autowired
  private VngatewayRouteInfoMapper vngatewayRouteInfoMapper;

  public void deleteAll(){
    vngatewayRouteInfoMapper.deleteAll();
  }


  public void insertRouteInfo(VngatewayRouteInfo vngatewayRouteInfo){
    vngatewayRouteInfoMapper.insertSelective(vngatewayRouteInfo);
  }

  public List<VngatewayRouteInfo> selectVnRouteByCodes(String[] codes){

    List<VngatewayRouteInfo> vnGatewayRouteInfos =
        vngatewayRouteInfoMapper.selectByCodes(codes);

    return vnGatewayRouteInfos;
  }


  public List<VngatewayRouteInfo> selectVnRouteByJobId(String jobId){
    List<VngatewayRouteInfo> vnGatewayRouteInfos = vngatewayRouteInfoMapper.selectByJobId(jobId);

    return vnGatewayRouteInfos;
  }


  public List<VngatewayRouteInfo> selectAllVn(){

    List<VngatewayRouteInfo> vngatewayRouteInfos = vngatewayRouteInfoMapper.selectAllVn();

    return vngatewayRouteInfos;
  }


}
