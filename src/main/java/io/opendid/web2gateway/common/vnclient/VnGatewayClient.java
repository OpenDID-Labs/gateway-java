package io.opendid.web2gateway.common.vnclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.VnRequestAddress;
import io.opendid.web2gateway.common.utils.OAuth2TokenUtil;
import io.opendid.web2gateway.common.utils.OkHttpClientUtil;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.okhttp.ResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import io.opendid.web2gateway.service.VngatewayRouteInfoService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class VnGatewayClient implements VnClient {

    private Logger logger = LoggerFactory.getLogger(VnGatewayClient.class);

    @Autowired
    private VnGlobalMapping vnGlobalMapping;
    @Autowired
    private OkHttpClientUtil okHttpClientUtil;
    @Autowired
    private OAuth2TokenUtil keyClockTokenUtil;
    @Autowired
    private VngatewayRouteInfoService vngatewayRouteInfoService;

      /**
   * 选择VnCode说明：
   *    VnClientJobIdDTO中vnCode字段如果有值，则为指定VnCode，会调用该VnCode的gateway。
   *    VnClientJobIdDTO中vnCode字段如果为空，且JobId不为空，会查询所有支持该JobId的VnCode，然后随机选择一个。
   *    VnClientJobIdDTO中vnCode字段如果为空，且JobId也为空，会查询所有的VnCode，然后随机选择一个。
   */
    @Override
    public JsonRpc2Response requestSubscriptionSend(VnClientJobIdDTO vnClientJobIdDTO) throws Exception {
        return callVn(vnClientJobIdDTO, VnRequestAddress.SUB_SEND);

    }


    /**
     * 选择VnCode说明：
     * VnClientJobIdDTO中vnCode字段如果有值，则为指定VnCode，会调用该VnCode的gateway。
     * VnClientJobIdDTO中vnCode字段如果为空，且JobId不为空，会查询所有支持该JobId的VnCode，然后随机选择一个。
     * VnClientJobIdDTO中vnCode字段如果为空，且JobId也为空，会查询所有的VnCode，然后随机选择一个。
     */
    @Override
    public JsonRpc2Response requestJobSend(VnClientJobIdDTO vnClientJobIdDTO)
            throws Exception {

        return callVn(vnClientJobIdDTO, VnRequestAddress.JOB_SEND);
    }


    @Nullable
    private JsonRpc2Response callVn(VnClientJobIdDTO vnClientJobIdDTO,
                                    String vnApiPath
    ) throws Exception {
        Integer counter = vnClientJobIdDTO.getCounter();
        if (counter == null) {
            counter = 1;
        }

        logger.info("VnGatewayClient request start parmas={}", JSON.toJSONString(vnClientJobIdDTO));

        String vnCode = "";
        if (StringUtils.isBlank(vnClientJobIdDTO.getVnCode())) {
            vnCode = getVnCode(vnClientJobIdDTO);
        } else {
            vnCode = vnClientJobIdDTO.getVnCode();
        }


        VngatewayRouteInfo vnGatewayRouteInfo = vnGlobalMapping.getRouteInfoForVnCode(vnCode);
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

            logger.info("VnGatewayClient request accessToken = {}", accessToken);

            ResponseDTO responseDTO = okHttpClientUtil.postForJson(
                    vnGatewayRouteInfo.getUrl() + vnApiPath,
                    vnClientJobIdDTO.getRequestBody(),
                    accessToken
            );

            logger.info("VnGatewayClient request result={}", JSON.toJSONString(responseDTO));

            if (responseDTO.getCode().equals(401) && counter > 0) {

                logger.info("VnGatewayClient request code = 401,counter={}", counter);

                counter = counter - 1;

                keyClockTokenUtil.createToken(
                        vnGatewayRouteInfo.getVnCode(),
                        vnGatewayRouteInfo.getUrl()
                );

                vnClientJobIdDTO.setCounter(counter);
                return requestJobSend(vnClientJobIdDTO);

            } else if (responseDTO.getCode().equals(200) || responseDTO.getCode().equals(400)) {
                logger.info("VnGatewayClient request code = 200");
                return responseManage(responseDTO.getBody());
            }

        } else {
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

        logger.info("VnGatewayClient responseManage = {}", body);

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


    private String getVnCode(VnClientJobIdDTO vnClientJobIdDTO) throws Exception {

        // If there is a VnCode in the parameter, it will be returned.
        // If not, a supported VnCode will be randomly selected.
        if (StringUtils.isBlank(vnClientJobIdDTO.getVnCode())) {
            logger.info("VnGatewayClient request getVnCode is blank");

            List<VngatewayRouteInfo> vngatewayRouteInfos =
                    vngatewayRouteInfoService.selectVnRouteByJobId(vnClientJobIdDTO.getJobId());

            if (vngatewayRouteInfos.isEmpty()) {
                logger.info("VnGatewayClient request getVnCode vnGatewayRouteInfo = null");
                throw new JsonRpc2ServerErrorException(
                        JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getCode(),
                        MDC.get(LogTraceIdConstant.TRACE_ID),
                        JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32002.getMessage(),
                        "The Vn info is not exist!");
            }

            Collections.shuffle(vngatewayRouteInfos);

            return vngatewayRouteInfos.get(0).getVnCode();
        } else {
            logger.info("VnGatewayClient request getVnCode is not blank={}", vnClientJobIdDTO.getVnCode());
            return vnClientJobIdDTO.getVnCode();
        }

    }
}
