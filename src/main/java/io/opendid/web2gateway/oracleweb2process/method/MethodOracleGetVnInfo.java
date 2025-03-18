package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.aptos.GetVnInfoRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.service.VngatewayRouteInfoService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component(Web2MethodName.ORACLE_GET_VN_INFO + Web2Method.BEAN_SUFFIX)
@MethodPrivate
public class MethodOracleGetVnInfo implements Web2Method {

  @Autowired
  private VngatewayRouteInfoService vngatewayRouteInfoService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    LinkedHashMap params = request.getParams();
    String jobId = MapUtils.getString(params, "jobId");

    List<VngatewayRouteInfo> vngatewayRouteInfos = vngatewayRouteInfoService.selectVnRouteByJobId(jobId);

    List<GetVnInfoRespDTO> resultList = vngatewayRouteInfos.stream().map(get -> {
      GetVnInfoRespDTO dto = new GetVnInfoRespDTO();
      dto.setPublicKey(get.getVnPublicKey());
      dto.setVnCode(get.getVnCode());
      return dto;
    }).toList();


    return resultList;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {


    return null;
  }
}
