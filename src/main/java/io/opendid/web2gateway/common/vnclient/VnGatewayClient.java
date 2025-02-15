package io.opendid.web2gateway.common.vnclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.VnRequestAddress;
import io.opendid.web2gateway.common.utils.OAuth2TokenUtil;
import io.opendid.web2gateway.common.utils.OkHttpClientUtil;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InternalErrorException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.okhttp.ResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VnGatewayClient implements VnClient {

  private Logger logger = LoggerFactory.getLogger(VnGatewayClient.class);

  @Autowired
  private VnGlobalMapping vnGlobalMapping;
  @Autowired
  private OkHttpClientUtil okHttpClientUtil;
  @Autowired
  private OAuth2TokenUtil keyClockTokenUtil;

  @Override
  public JsonRpc2Response request(VnClientJobIdDTO vnClientJobIdDTO)
      throws Exception {

    Integer counter = vnClientJobIdDTO.getCounter();
    if (counter == null){
      counter = 1;
    }

    logger.info("VnGatewayClient request start parmas={}", JSON.toJSONString(vnClientJobIdDTO));

    VngatewayRouteInfo vnGatewayRouteInfo = vnGlobalMapping.getVnRouteByJobId(
        vnClientJobIdDTO.getJobId()
    );
    logger.info("VnGatewayClient request vnGatewayRouteInfo={}", JSON.toJSONString(vnGatewayRouteInfo));


    if (vnGatewayRouteInfo != null) {

      logger.info("VnGatewayClient request vnGatewayRouteInfo != null,url={}",
          vnGatewayRouteInfo.getUrl());

      String accessToken;

      if (vnGatewayRouteInfo.getClientId() != null &&
          !"".equals(vnGatewayRouteInfo.getClientId())) {

        accessToken = keyClockTokenUtil.getToken(vnGatewayRouteInfo.getVnCode(),
            vnGatewayRouteInfo.getUrl());

      } else {

        accessToken = keyClockTokenUtil.initToken(
            vnGatewayRouteInfo.getVnCode(),
            vnGatewayRouteInfo.getUrl()
        );

      }

      logger.info("VnGatewayClient request accessToken = {}",accessToken);

      ResponseDTO responseDTO = okHttpClientUtil.postForJson(
          vnGatewayRouteInfo.getUrl() + VnRequestAddress.JOB_SEND,
          vnClientJobIdDTO.getRequestBody(),
          accessToken
      );

      logger.info("VnGatewayClient request result={}",JSON.toJSONString(responseDTO));

      if (responseDTO.getCode().equals(401) && counter > 0) {

        logger.info("VnGatewayClient request code = 401,counter={}",counter);

        counter = counter - 1;

        keyClockTokenUtil.createToken(
            vnGatewayRouteInfo.getVnCode(),
            vnGatewayRouteInfo.getUrl()
        );

        vnClientJobIdDTO.setCounter(counter);
        return request(vnClientJobIdDTO);

      } else if (responseDTO.getCode().equals(200) || responseDTO.getCode().equals(400)) {
        logger.info("VnGatewayClient request code = 200");
        return responseManage(responseDTO.getBody());
      }

    }else{
      logger.info("VnGatewayClient request vnGatewayRouteInfo = null");

      String logId = MDC.get(LogTraceIdConstant.TRACE_ID);
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32005.getCode(),
          logId,
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32005.getMessage(),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32005.getMessage());
    }

    logger.info("VnGatewayClient request return null");
    return null;
  }


  private JsonRpc2Response responseManage(String body) throws Exception {

    logger.info("VnGatewayClient responseManage = {}",body);

    JSONObject jsonBody = JSONObject.parseObject(body);

    if (jsonBody.get("error") != null) {

      JSONObject errorJson = jsonBody.getJSONObject("error");

      throw new JsonRpc2ServerErrorException(
          errorJson.getInteger("code"),
          errorJson.getString("logId"),
          errorJson.getString("message"),
          errorJson.getString("data")
      );

    } else {
      return new JsonRpc2Response(jsonBody.getLong("id"),
          jsonBody.getObject("result", Object.class));
    }

  }

}
