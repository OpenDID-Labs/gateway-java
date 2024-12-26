package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.GatewayKeyVault;

public interface GatewayKeyVaultMapper {
    int deleteByPrimaryKey(Integer keyId);

    int insert(GatewayKeyVault record);

    int insertSelective(GatewayKeyVault record);

    GatewayKeyVault selectByPrimaryKey(Integer keyId);

    int updateByPrimaryKeySelective(GatewayKeyVault record);

    int updateByPrimaryKey(GatewayKeyVault record);

    GatewayKeyVault selectKeyInfo();
}