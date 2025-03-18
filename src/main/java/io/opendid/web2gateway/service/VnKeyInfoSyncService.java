package io.opendid.web2gateway.service;

import io.opendid.web2gateway.repository.mapper.VngatewayRouteInfoMapper;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VnKeyInfoSyncService {
    @Autowired
    private VngatewayRouteInfoMapper vngatewayRouteInfoMapper;
    public List<VngatewayRouteInfo> selectAllVns(){
        List<VngatewayRouteInfo> vngatewayRouteInfos = vngatewayRouteInfoMapper.selectAllVn();
        return vngatewayRouteInfos;
    }

    public int updateKeyInfo(VngatewayRouteInfo vngatewayRouteInfo){
        return vngatewayRouteInfoMapper.updateByPrimaryKeySelective(vngatewayRouteInfo);
    }
}
