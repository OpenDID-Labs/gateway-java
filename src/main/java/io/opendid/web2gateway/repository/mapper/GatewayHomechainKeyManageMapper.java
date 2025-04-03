package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;

import java.util.List;

public interface GatewayHomechainKeyManageMapper {
    int deleteByPrimaryKey(Integer keyId);

    int insert(GatewayHomechainKeyManage record);

    int insertSelective(GatewayHomechainKeyManage record);

    GatewayHomechainKeyManage selectByPrimaryKey(Integer keyId);

    int updateByPrimaryKeySelective(GatewayHomechainKeyManage record);

    int updateByPrimaryKey(GatewayHomechainKeyManage record);

    int deleteAll();

    GatewayHomechainKeyManage selectByVnCode(String vnCode);

    List<GatewayHomechainKeyManage> selectAll();

    GatewayHomechainKeyManage selectByWalletAddress(String walletAddress);
}