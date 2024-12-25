package io.opendid.web2gateway.common.vnclient;

import io.opendid.web2gateway.repository.mapper.VngatewayRouteInfoMapper;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VnGlobalMapping {

  @Autowired
  private VngatewayRouteInfoMapper vnGatewayRouteInfoMapper;

  public VngatewayRouteInfo getVnRouteByJobId(String jobId) {

    List<VngatewayRouteInfo> vnGatewayRouteInfos = vnGatewayRouteInfoMapper.selectByJobId(jobId);

    if (!vnGatewayRouteInfos.isEmpty()) {

      return vnGatewayRouteInfos.get(0);
    }

    return null;
  }

  public Integer updateForVnCode(VngatewayRouteInfo vngatewayRouteInfo){
    return vnGatewayRouteInfoMapper.updateByVnCodeSelective(vngatewayRouteInfo);
  }

  public VngatewayRouteInfo getRouteInfoForVnCode(String vnCode){
    return vnGatewayRouteInfoMapper.selectByVnCode(vnCode);
  }

}
