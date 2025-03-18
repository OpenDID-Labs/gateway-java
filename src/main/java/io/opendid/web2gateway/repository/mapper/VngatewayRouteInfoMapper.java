package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import java.util.List;

public interface VngatewayRouteInfoMapper {
    int deleteByPrimaryKey(Long routeId);

    int insert(VngatewayRouteInfo record);

    int insertSelective(VngatewayRouteInfo record);

    VngatewayRouteInfo selectByPrimaryKey(Long routeId);

    int updateByPrimaryKeySelective(VngatewayRouteInfo record);

    int updateByPrimaryKey(VngatewayRouteInfo record);

    List<VngatewayRouteInfo> selectByJobId(String jobId);

    int updateByVnCodeSelective(VngatewayRouteInfo record);

    VngatewayRouteInfo selectByVnCode(String vnCode);

    int deleteAll();

    List<VngatewayRouteInfo> selectByCodes(String[] vnCodes);

    List<VngatewayRouteInfo> selectAllVn();


}