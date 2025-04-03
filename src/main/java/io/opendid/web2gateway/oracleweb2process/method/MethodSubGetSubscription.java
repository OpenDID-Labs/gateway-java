package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubscriptionGetMethodVnResDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubscriptionGetRespDTO;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubscriptionInfoDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.repository.model.SubscriptionInfo;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.service.SubscriptionInfoService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(Web2MethodName.SUB_GET_SUBSCRIPTION + Web2Method.BEAN_SUFFIX)
@MethodSubscription
public class MethodSubGetSubscription implements Web2SubscriptionMethod {

  private static final Logger logger = LoggerFactory.getLogger(MethodSubGetSubscription.class);

  @Resource
  private VnGatewayClient vnGatewayClient;

  @Resource
  private SubscriptionInfoService subscriptionInfoService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {
    SubscriptionGetRespDTO respDTO = new SubscriptionGetRespDTO();
    List<SubscriptionInfoDTO> subIds = new ArrayList<>();

    List<String> subIdList;
    // check subId
    Object subId = request.getParams().get("subId");
    if (subId == null) {
      subId = new ArrayList<>();
    }
    if (subId instanceof List) {
      subIdList = (List<String>) subId;
    } else {
      throw new JsonRpc2ServerErrorException(JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getCode(), null, "subId format error", null);
    }

    // select db subId
    List<SubscriptionInfo> subscriptionInfos = subscriptionInfoService.selectBySubIdList(subIdList);
    if (subscriptionInfos.isEmpty()) {
      respDTO.setSubIds(subIds);
      return respDTO;
    }

    request.getParams().put("subId", subscriptionInfos.stream().map(SubscriptionInfo::getSubId).collect(Collectors.toList()));

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setRequestBody(request);
    JsonRpc2Response jsonRpc2Response = vnGatewayClient.requestSubscriptionSend(vnClientJobIdDTO);
    if (jsonRpc2Response != null) {
      JSONObject respResultJson = JSONObject.parseObject(
              JSONObject.toJSONString(jsonRpc2Response));
      SubscriptionGetMethodVnResDTO result = respResultJson.getObject("result", SubscriptionGetMethodVnResDTO.class);
      respDTO.setSubIds(result.getSubIds());

      // update db subscription balance
      subscriptionInfoService.updateSubIdBalance(result.getSubIds());
      return respDTO;
    } else {
      logger.error("MethodSubGetSubscription request vn gateway return null");
      throw new JsonRpc2ServerErrorException(
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
              MDC.get(LogTraceIdConstant.TRACE_ID),
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
              "Method MethodSubGetSubscription call vn result is null");
    }

  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    return null;
  }


}
