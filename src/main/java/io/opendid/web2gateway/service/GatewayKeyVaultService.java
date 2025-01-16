package io.opendid.web2gateway.service;


import io.opendid.web2gateway.repository.mapper.GatewayKeyVaultMapper;
import io.opendid.web2gateway.repository.model.GatewayKeyVault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
*
* @author Dong-Jianguo
* @Date: 2025/1/8
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Service
public class GatewayKeyVaultService {

  @Autowired
  private GatewayKeyVaultMapper gatewayKeyVaultMapper;



  public GatewayKeyVault selectCurrentKeyVault(){

    GatewayKeyVault gatewayKeyVaultOld = gatewayKeyVaultMapper.selectKeyInfo();

    return gatewayKeyVaultOld;
  }

  public void deleteByKey(Integer keyId){
    gatewayKeyVaultMapper.deleteByPrimaryKey(keyId);
  }

  public void insertKeyVault(GatewayKeyVault gatewayKeyVault){
    gatewayKeyVaultMapper.insertSelective(gatewayKeyVault);
  }


}
