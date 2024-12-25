package io.opendid.web2gateway.common.vnclient;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.VnRequestAddress;
import io.opendid.web2gateway.model.dto.OAuth2.GetTokenReqDTO;
import io.opendid.web2gateway.model.dto.OAuth2.GetTokenResDTO;
import io.opendid.web2gateway.common.utils.OkHttpClientUtil;
import io.opendid.web2gateway.model.dto.okhttp.ResponseDTO;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Token {

  private Logger logger = LoggerFactory.getLogger(OAuth2Token.class);

  @Autowired
  private OkHttpClientUtil okHttpClientUtil;

  @Autowired
  private VnGlobalMapping vnGlobalMapping;

  public GetTokenResDTO getAccessToken(GetTokenReqDTO getTokenReqDTO) {

    Map<String, String> reqData = new HashMap<>();

    reqData.put("client_id", getTokenReqDTO.getClientId());
    reqData.put("client_secret", getTokenReqDTO.getClientSecret());
    reqData.put("grant_type", getTokenReqDTO.getGrantType());

    if (getTokenReqDTO.getScope() != null && !"".equals(getTokenReqDTO.getScope())) {
      reqData.put("scope", getTokenReqDTO.getScope());
    }

    ResponseDTO responseDTO = okHttpClientUtil.postForForm(
        getTokenReqDTO.getVnRouteUrl()+ VnRequestAddress.GET_TOKEN,
        reqData);

    logger.info("KeyClock getAccessToken response data:{}", JSONObject.toJSONString(responseDTO));

    if (responseDTO != null && responseDTO.getCode().equals(200)) {

      JSONObject resJsonData = JSONObject.parseObject(responseDTO.getBody());

      if (resJsonData != null) {

        GetTokenResDTO getTokenResDTO = new GetTokenResDTO();

        if (resJsonData.getString("access_token") != null) {
          getTokenResDTO.setAccessToken(resJsonData.getString("access_token"));
        }
        if (resJsonData.getInteger("expires_in") != null) {
          getTokenResDTO.setExpiresIn(resJsonData.getInteger("expires_in"));
        }
        if (resJsonData.getInteger("refresh_expires_in") != null) {
          getTokenResDTO.setRefreshExpiresIn(resJsonData.getInteger("refresh_expires_in"));
        }
        if (resJsonData.getString("token_type") != null) {
          getTokenResDTO.setTokenType(resJsonData.getString("token_type"));
        }
        if (resJsonData.getString("id_token") != null) {
          getTokenResDTO.setIdToken(resJsonData.getString("id_token"));
        }
        if (resJsonData.getInteger("not-before-policy") != null) {
          getTokenResDTO.setNotBeforePolicy(resJsonData.getInteger("not-before-policy"));
        }
        if (resJsonData.getString("scope") != null) {
          getTokenResDTO.setScope(resJsonData.getString("scope"));
        }

        return getTokenResDTO;
      }

    }

    return null;
  }

}