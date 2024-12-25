package io.opendid.web2gateway.common.vnclient;

import com.alibaba.fastjson.JSONObject;
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
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VnGatewayClient implements VnClient {

  private int counter = 1;

  @Autowired
  private VnGlobalMapping vnGlobalMapping;
  @Autowired
  private OkHttpClientUtil okHttpClientUtil;
  @Autowired
  private OAuth2TokenUtil keyClockTokenUtil;

  @Override
  public JsonRpc2Response request(VnClientJobIdDTO vnClientJobIdDTO)
      throws JsonRpc2ServerErrorException {

    VngatewayRouteInfo vnGatewayRouteInfo = vnGlobalMapping.getVnRouteByJobId(
        vnClientJobIdDTO.getJobId()
    );


    if (vnGatewayRouteInfo != null) {

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

      ResponseDTO responseDTO = okHttpClientUtil.postForJson(
          vnGatewayRouteInfo.getUrl() + VnRequestAddress.JOB_SEND,
          vnClientJobIdDTO.getRequestBody(),
          accessToken
      );

      if (responseDTO.getCode().equals(401) && counter > 0) {

        counter = counter - 1;

        keyClockTokenUtil.createToken(
            vnGatewayRouteInfo.getVnCode(),
            vnGatewayRouteInfo.getUrl()
        );

        request(vnClientJobIdDTO);

      } else if (responseDTO.getCode().equals(200) || responseDTO.getCode().equals(400)) {

        return responseManage(responseDTO.getBody());
      }

    }else{
      String logId = MDC.get(LogTraceIdConstant.TRACE_ID);
      throw new JsonRpc2InternalErrorException(logId,"Vn route is empty");
    }

    return null;
  }


  private JsonRpc2Response responseManage(String body) throws JsonRpc2ServerErrorException {

    JSONObject jsonBody = JSONObject.parseObject(body);

    if (jsonBody.get("error") != null) {

      JSONObject errorJson = jsonBody.getJSONObject("error");

      try {

        throw new JsonRpc2ServerErrorException(
            errorJson.getInteger("code"),
            errorJson.getString("logId"),
            errorJson.getString("message"),
            errorJson.getString("data")
        );

      } catch (Exception e) {

        throw new RuntimeException(e);
      }

    } else {
      return new JsonRpc2Response(jsonBody.getLong("id"),
          jsonBody.getObject("result", Object.class));
    }

  }

}
