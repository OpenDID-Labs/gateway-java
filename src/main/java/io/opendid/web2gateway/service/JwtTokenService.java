package io.opendid.web2gateway.service;

import io.opendid.web2gateway.model.dto.admin.TenantJwCancelReqDTO;
import io.opendid.web2gateway.model.dto.admin.TenantJwCancelResDTO;
import io.opendid.web2gateway.model.dto.admin.TenantJwCreateReqDTO;
import io.opendid.web2gateway.repository.mapper.GatewayKeyVaultMapper;
import io.opendid.web2gateway.repository.mapper.TenantJwtMangerMapper;
import io.opendid.web2gateway.repository.model.GatewayKeyVault;
import io.opendid.web2gateway.repository.model.TenantJwtManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenService {

  @Autowired
  private GatewayKeyVaultMapper gatewayKeyVaultMapper;

  @Autowired
  private TenantJwtMangerMapper tenantJwtMangerMapper;


  public TenantJwtManger selectTenantJwtBySubId(String subId){
    TenantJwtManger tenantJwtManger=tenantJwtMangerMapper.selectBySubjectId(subId);
    return tenantJwtManger;
  }

  public String selectAdminJwt(){
    GatewayKeyVault gatewayKeyVault = gatewayKeyVaultMapper.selectKeyInfo();
    return gatewayKeyVault.getAdminJwt();
  }


  public void updateAdminJwt(String adminJwt){
    GatewayKeyVault gatewayKeyVault = gatewayKeyVaultMapper.selectKeyInfo();

    gatewayKeyVault.setAdminJwt(adminJwt);
    gatewayKeyVaultMapper.updateByPrimaryKey(gatewayKeyVault);
  }

  public void insertTenantJwt(TenantJwCreateReqDTO createReqDTO){
    Date now = new Date();
    TenantJwtManger tenantJwtManger = new TenantJwtManger();
    tenantJwtManger.setJwt(createReqDTO.getJwt());
    tenantJwtManger.setSubjectName(createReqDTO.getSubject());
    tenantJwtManger.setSubjectId(createReqDTO.getSubjectId());
    tenantJwtManger.setCreateDate(now);
    tenantJwtManger.setUpdateDate(now);
    tenantJwtManger.setExpedDate(createReqDTO.getExpTime());
    tenantJwtMangerMapper.insertSelective(tenantJwtManger);
  }

  public int deleteTenantJwt(TenantJwCancelReqDTO cancelReqDTO){

    int count=tenantJwtMangerMapper.deleteBySubId(cancelReqDTO.getSubId());
    return count;
  }

}
