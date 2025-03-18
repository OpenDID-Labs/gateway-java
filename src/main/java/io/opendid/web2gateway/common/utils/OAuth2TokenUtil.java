package io.opendid.web2gateway.common.utils;

import io.opendid.web2gateway.common.enums.oauth2.GrantTypeEnum;
import io.opendid.web2gateway.common.vnclient.OAuth2Register;
import io.opendid.web2gateway.common.vnclient.OAuth2Token;
import io.opendid.web2gateway.common.vnclient.VnGlobalMapping;
import io.opendid.web2gateway.model.dto.OAuth2.GetTokenReqDTO;
import io.opendid.web2gateway.model.dto.OAuth2.GetTokenResDTO;
import io.opendid.web2gateway.model.dto.OAuth2.OAuth2ClientDTO;
import io.opendid.web2gateway.model.dto.OAuth2.OAuth2RegisterDTO;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuth2TokenUtil {

  private Logger logger = LoggerFactory.getLogger(OAuth2TokenUtil.class);

  private static Map<String, String> tokens = new ConcurrentHashMap<>();

  @Value("${service-key.privatekey}")
  private String privateKey;

  @Autowired
  private OAuth2Register keyClockRegister;
  @Autowired
  private OAuth2Token keyClockToken;
  @Autowired
  private VnGlobalMapping vnGlobalMapping;


  public String initToken(String vnCode, String vnRouteUrl) {

    try {
      logger.info("KeyClockTokenUtil initToken start");

      String publicKey = GatewayKeyVaultUtil.getServiceKey(GatewayKeyVaultUtil.servicePublicKey);

      String sign = ECDSAUtils.sign(
          publicKey,
          new BigInteger(privateKey.replace("0x", ""), 16).toString(),
          new BigInteger(publicKey.replace("0x", ""), 16).toString()
      );

      OAuth2RegisterDTO keyClockRegisterDTO = new OAuth2RegisterDTO();

      keyClockRegisterDTO.setVnRouteUrl(vnRouteUrl);
      keyClockRegisterDTO.setPublicKey(publicKey);
      keyClockRegisterDTO.setSignature(sign);

      OAuth2ClientDTO register = keyClockRegister.register(keyClockRegisterDTO);

      if (register == null) {
        logger.error("KeyClockTokenUtil initToken Error: Registration of KeyClock failed");
        return null;
      }

      GetTokenReqDTO getTokenReqDTO = new GetTokenReqDTO();
      getTokenReqDTO.setVnRouteUrl(vnRouteUrl);
      getTokenReqDTO.setClientId(register.getClientId());
      getTokenReqDTO.setClientSecret(register.getClientSecret());
      getTokenReqDTO.setGrantType(GrantTypeEnum.CLIENT_CREDENTIALS.getCode());

      GetTokenResDTO GetTokenResDTO = keyClockToken.getAccessToken(getTokenReqDTO);

      if (GetTokenResDTO == null) {
        logger.error("KeyClockTokenUtil initToken Error: Generate AccessToken failed");
        return null;
      }

      VngatewayRouteInfo vngatewayRouteInfo = new VngatewayRouteInfo();
      vngatewayRouteInfo.setVnCode(vnCode);
      vngatewayRouteInfo.setClientId(register.getClientId());
      vngatewayRouteInfo.setClientSecret(register.getClientSecret());
      vngatewayRouteInfo.setVnPublicKey(register.getVnPublicKey());
      // Update keyClock client information
      vnGlobalMapping.updateForVnCode(vngatewayRouteInfo);

      // setup accessToken
      tokens.put(vnCode, GetTokenResDTO.getAccessToken());

      return GetTokenResDTO.getAccessToken();

    } catch (Exception e) {
      logger.error("KeyClockTokenUtil initToken Error: {}", e.getMessage());
    }

    return null;
  }


  public String createToken(String vnCode, String vnRouteUrl) throws Exception {

    VngatewayRouteInfo vngatewayRouteInfo = vnGlobalMapping.getRouteInfoForVnCode(vnCode);

    GetTokenReqDTO getTokenReqDTO = new GetTokenReqDTO();

    getTokenReqDTO.setVnRouteUrl(vnRouteUrl);
    getTokenReqDTO.setClientId(vngatewayRouteInfo.getClientId());
    getTokenReqDTO.setClientSecret(vngatewayRouteInfo.getClientSecret());
    getTokenReqDTO.setGrantType(GrantTypeEnum.CLIENT_CREDENTIALS.getCode());

    GetTokenResDTO GetTokenResDTO = keyClockToken.getAccessToken(getTokenReqDTO);

    if (GetTokenResDTO == null) {
      logger.error("KeyClockTokenUtil refreshToken Error: Generate AccessToken failed");
      return null;
    }

    // setup accessToken
    tokens.put(vnCode, GetTokenResDTO.getAccessToken());

    return GetTokenResDTO.getAccessToken();
  }


  public String getToken(String vnCode, String vnRouteUrl) throws Exception {

    logger.info("KeyClockTokenUtil getToken start");

    String token = null;

    if (tokens.get(vnCode) != null) {

      token = tokens.get(vnCode);

    } else {

      if(vnRouteUrl != null){
        token = createToken(vnCode, vnRouteUrl);
      }
    }

    return token;

  }


}
