package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.GatewayHomechainKeyManageMapper;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
*  HomeChainKeyManageService
* @author Dong-Jianguo
* @Date: 2025/2/18
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Service
public class HomeChainKeyManageService {

  @Autowired
  private GatewayHomechainKeyManageMapper gatewayHomechainKeyManageMapper;


  public void insertHomeChainKey(GatewayHomechainKeyManage gatewayHomechainKeyManage){
    gatewayHomechainKeyManageMapper.insertSelective(gatewayHomechainKeyManage);
  }


  public void deleteAll(){
    gatewayHomechainKeyManageMapper.deleteAll();
  }

  public GatewayHomechainKeyManage selectByVnCode(String vnCode){
    return gatewayHomechainKeyManageMapper.selectByVnCode(vnCode);
  }


  public List<GatewayHomechainKeyManage> selectAll(){
    return gatewayHomechainKeyManageMapper.selectAll();
  }

  public GatewayHomechainKeyManage selectByWalletAddress(String walletAddress) {
    return gatewayHomechainKeyManageMapper.selectByWalletAddress(walletAddress);
  }

}
