package io.opendid.web2gateway;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.oauth2.GrantTypeEnum;
import io.opendid.web2gateway.common.utils.OkHttpClientUtil;
import io.opendid.web2gateway.common.vnclient.VnClient;
import io.opendid.web2gateway.config.vnmapping.VnMappingConfig;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.OAuth2.GetTokenReqDTO;
import io.opendid.web2gateway.model.dto.OAuth2.GetTokenResDTO;
import io.opendid.web2gateway.model.dto.OAuth2.OAuth2ClientDTO;
import io.opendid.web2gateway.model.dto.OAuth2.OAuth2RegisterDTO;
import io.opendid.web2gateway.common.vnclient.OAuth2Register;
import io.opendid.web2gateway.common.vnclient.OAuth2Token;
import io.opendid.web2gateway.common.utils.ECDSAUtils;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import java.math.BigInteger;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KeyClockTest {

  @Autowired
  private OAuth2Register keyClockRegisterService;
  @Autowired
  private OAuth2Token keyClockTokenService;
  @Autowired
  private VnClient vnClient;
  @Autowired
  private OkHttpClientUtil okHttpClientUtil;
  @Autowired
  private VnMappingConfig vnMappingConfig;

  @Test
  void register() throws Exception {

    String publicKey = "0xdd0cc7746829366f1b572fe83ce2dad957233d82af1d13a255b0372928fad69d2a5af7c20bfb1d4bde536d69a72d4d204fc5a906198ed7c0b62bcaa23501f515";
    String privateKey = "0xcf7b00d3bfd000d913c7a6bed2e8593b99aa0f00977b4c9743a141f54285e01e";
    String address = "dc624ed695c3fe02d354ddf9aea7b37d2cde85f7";

    String sign = ECDSAUtils.sign(
        publicKey,
        new BigInteger(privateKey.replace("0x", ""), 16).toString(),
        new BigInteger(publicKey.replace("0x", ""), 16).toString()
    );

    OAuth2RegisterDTO keyClockRegisterDTO = new OAuth2RegisterDTO();

    keyClockRegisterDTO.setPublicKey(publicKey);
    keyClockRegisterDTO.setSignature(sign);

    OAuth2ClientDTO register = keyClockRegisterService.register(keyClockRegisterDTO);

    GetTokenReqDTO getTokenReqDTO = new GetTokenReqDTO();
    getTokenReqDTO.setClientId(register.getClientId());
    getTokenReqDTO.setClientSecret(register.getClientSecret());
    getTokenReqDTO.setGrantType(GrantTypeEnum.CLIENT_CREDENTIALS.getCode());

    GetTokenResDTO accessToken = keyClockTokenService.getAccessToken(getTokenReqDTO);

    System.out.println(JSONObject.toJSONString(accessToken));


  }

  @Test
  void requestTest() {

    String jobId = "1028e09602ca4bdd89a6bba67212001f";

    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
    data.put("authApplyRetainData",
        "0446fb7fe1a26b8503af5a291e80f533da3a74cf39ccca041697175f6282ebd0a0f1213ba1f52fc0d8625decaee18d2c557aa40ea49edf7807b8682e1e572e1cf32542773b5964e21ae3d4941422c85a241e3c993e5a44bc1ffea8f46e7c01057ce42db05703f9d7be276d646bf3f4ecb7f69199cdb30c5c6288ebdd1eabd8877551659cf00963d3fe627e96a4f50b81b188945d8b5e3989f7cf8968bc484dee19723255ff0c0324");
    data.put("authHash", "D9B1E71175D6AB1FC166F4883BCFFFC1C9BB9456E3E4CCDA5F68C7A75B850974");
    data.put("publicKeyIndex", 1);

    LinkedHashMap<String, Object> params = new LinkedHashMap<>();
    params.put("jobId", jobId);
    params.put("data", data);

    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        1L,
        "oracle_request",
        params,
        null
    );

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setJobId(jobId);
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);

    try {

      JsonRpc2Response responseRoot = vnClient.requestJobSend(vnClientJobIdDTO);

      System.out.println(JSONObject.toJSONString(responseRoot));

    } catch (JsonRpc2ServerErrorException e) {


      System.out.println(JSONObject.toJSONString(e));
    } catch (Exception exception) {
      exception.printStackTrace();
    }


    System.out.println("------");


  }

}
