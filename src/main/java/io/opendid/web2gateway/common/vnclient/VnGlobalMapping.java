package io.opendid.web2gateway.common.vnclient;

import com.alibaba.fastjson.JSON;
import io.opendid.web2gateway.repository.mapper.VngatewayRouteInfoMapper;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VnGlobalMapping {

  private Logger logger = LoggerFactory.getLogger(VnGlobalMapping.class);

  @Autowired
  private VngatewayRouteInfoMapper vnGatewayRouteInfoMapper;

  public VngatewayRouteInfo getVnRouteByJobId(String jobId) {

    List<VngatewayRouteInfo> vnGatewayRouteInfos = vnGatewayRouteInfoMapper.selectByJobId(jobId);

    logger.info("VnGlobalMapping getVnRouteByJobId size={}",vnGatewayRouteInfos.size());

    if (!vnGatewayRouteInfos.isEmpty()) {
      logger.info("VnGlobalMapping getVnRouteByJobId result={}", JSON.toJSONString(vnGatewayRouteInfos.get(0)));
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
