package io.opendid.web2gateway.common.vnclient;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.VnRequestAddress;
import io.opendid.web2gateway.model.dto.OAuth2.OAuth2ClientDTO;
import io.opendid.web2gateway.model.dto.OAuth2.OAuth2RegisterDTO;
import io.opendid.web2gateway.common.utils.OkHttpClientUtil;
import io.opendid.web2gateway.model.dto.okhttp.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuth2Register {

  private Logger logger = LoggerFactory.getLogger(OAuth2Register.class);

  @Autowired
  private OkHttpClientUtil okHttpClientUtil;


  public OAuth2ClientDTO register(OAuth2RegisterDTO keyClockRegisterDTO) {

    ResponseDTO responseData = okHttpClientUtil.postForJson(
          keyClockRegisterDTO.getVnRouteUrl()+ VnRequestAddress.KEY_CLOCK_REGISTER,
          keyClockRegisterDTO,
          null);

      logger.info("KeyClock register response data:{}", JSONObject.toJSONString(responseData));

      if (responseData != null && responseData.getCode().equals(200)) {

        JSONObject responseJsonData = JSONObject.parseObject(responseData.getBody());

        if (responseJsonData.getInteger("code") == 0 && responseJsonData.get("data") != null) {

          JSONObject keyClockData = responseJsonData.getJSONObject("data");

          OAuth2ClientDTO keyClockClientDTO = new OAuth2ClientDTO();

          keyClockClientDTO.setClientId(keyClockData.getString("clientId"));
          keyClockClientDTO.setClientSecret(keyClockData.getString("clientSecret"));

          return keyClockClientDTO;
        }

      }

    return null;

  }

}
