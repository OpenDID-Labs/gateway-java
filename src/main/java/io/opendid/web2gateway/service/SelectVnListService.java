package io.opendid.web2gateway.service;

import com.alibaba.fastjson.JSONArray;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.SelectVnListDTO;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SelectVnListService {

  private static final Logger logger = LoggerFactory.getLogger(SelectVnListService.class);


  @Autowired
  private VngatewayRouteInfoService vngatewayRouteInfoService;


  /**
   * 获取VNList逻辑：
   *    1，SelectVnListDTO中的vnList不为空，则为指定VNCode，返回指定的Vn信息
   *    2，SelectVnListDTO中的vnList为空，vnCount不为空，jobId不为空，则查询所有支持该jobId的vn，随机返回指定个数的Vn信息
   *    3，SelectVnListDTO中的vnList为空，vnCount不为空，jobId为空，则查询所有的vn，随机返回指定个数的Vn信息
   */
  public List<VngatewayRouteInfo> selectVnList(SelectVnListDTO selectVnListDTO) throws Exception {

    String jobId = selectVnListDTO.getJobId();
    String[] vnList=null;
    // Query the VnCode parameter in the input parameter
    Object vnListObj = selectVnListDTO.getVnList();
    if (vnListObj instanceof JSONArray){
      // Determine whether vnCode is correct
      JSONArray jsonArray = (JSONArray) vnListObj;
      vnList= jsonArray.toJavaList(String.class).toArray(new String[0]);
    }


    // Query the Vn quantity parameter in the input parameter
    Integer vnCount = selectVnListDTO.getVnCount();

    if (vnList!=null && vnList.length>0) {

      // Determine whether vnCode is correct
      List<VngatewayRouteInfo> vnGatewayRouteInfos = vngatewayRouteInfoService.selectVnRouteByCodes(vnList);
      if (vnGatewayRouteInfos.size() != vnList.length) {
        // The number of input parameters is inconsistent with the number of query results,
        // indicating that there is non-existent vnGateway information in the input parameters.
        logger.error("MethodOracleRequest selectVnList vnGateway number error");
        throw new JsonRpc2ServerErrorException(
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
            MDC.get(LogTraceIdConstant.TRACE_ID),
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
            "The VnCode you entered is incorrect!");
      }

      return vnGatewayRouteInfos;
    } else if (vnCount != null){
      // Randomly select vnCode based on the number of user requests
      List<VngatewayRouteInfo> vnGatewayRouteInfos = vngatewayRouteInfoService.selectVnRouteByJobId(jobId);

      // Determine whether the quantity is reasonable
      if (vnCount <= 0
          || vnCount > vnGatewayRouteInfos.size()) {
        logger.error("MethodOracleRequest selectVnList vnGateway number error");
        throw new JsonRpc2ServerErrorException(
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
            MDC.get(LogTraceIdConstant.TRACE_ID),
            JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
            "The number of VNs that support this JobId is:" + vnGatewayRouteInfos.size());
      }

      // Select vn method based on quantity, the current strategy is random
      // Shuffle the list order and return vnCount quantity
      Collections.shuffle(vnGatewayRouteInfos);

      return vnGatewayRouteInfos.subList(0, vnCount);
    }

    logger.error("MethodOracleRequest selectVnList no code and number");
    throw new JsonRpc2ServerErrorException(
        JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
        MDC.get(LogTraceIdConstant.TRACE_ID),
        JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
        "You must specify the VnCode or Vn number");
  }




}
