package io.opendid.web2gateway.security.jwt;

import io.opendid.web2gateway.common.utils.UuidUtil;
import io.opendid.web2gateway.model.dto.admin.TenantGenerateAccessTokenResDTO;
import io.opendid.web2gateway.model.dto.admin.TenantJwtGenerateResDTO;
import io.opendid.web2gateway.model.dto.jwtclaim.JWTClaimDTO;
import io.opendid.web2gateway.model.dto.jwtkey.JWTCatchDTO;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtCreate {


  private static Logger logger = LoggerFactory.getLogger(JwtCreate.class);

  @Autowired
  JWTCatch jwtCatch;

  public String generateAdminJwt() {
    JWTCatchDTO jwtCatch1 = jwtCatch.getJwtCatch();
    Map mapClaims = settingRootClaims();
    TenantGenerateAccessTokenResDTO tenantGenerateAccessTokenResDTO = JWTUtil.generateAccessToken(
            JWTClaimDTO.ADMIN, mapClaims, jwtCatch1.getPrivateKey(),
            0L);

    logger.info(
        "\n============================\n "
            + "RootJWT Token: {}"
            + "\n============================\n",
            tenantGenerateAccessTokenResDTO.getJwtToken());

    return tenantGenerateAccessTokenResDTO.getJwtToken();
  }


  public TenantJwtGenerateResDTO generateTenantJwt() {
    TenantJwtGenerateResDTO tenantJwtGenerateResDTO= generateTenantJwt(0L);
    return tenantJwtGenerateResDTO;
  }

  public TenantJwtGenerateResDTO generateTenantJwt(Long exp) {
    JWTCatchDTO jwtCatch1 = jwtCatch.getJwtCatch();
    Map mapClaims = settingTenantClaims();
    TenantGenerateAccessTokenResDTO tenantGenerateAccessTokenResDTO = JWTUtil.generateAccessToken(
            JWTClaimDTO.ADMIN, mapClaims, jwtCatch1.getPrivateKey(),
            exp);
    TenantJwtGenerateResDTO tenantJwtGenerateResDTO = new TenantJwtGenerateResDTO();
    tenantJwtGenerateResDTO.setJwtToken(tenantGenerateAccessTokenResDTO.getJwtToken());
    tenantJwtGenerateResDTO.setExpTime(tenantGenerateAccessTokenResDTO.getExpTime());
    tenantJwtGenerateResDTO.setSubId((String) mapClaims.get(JWTClaimDTO.SUB_ID));
    return tenantJwtGenerateResDTO;
  }


  private static @NotNull Map settingRootClaims() {
    Map mapClaims = generateClaims(true);
    return mapClaims;
  }


  private static @NotNull Map settingTenantClaims() {
    Map mapClaims = generateClaims(false);
    return mapClaims;
  }

  private static Map generateClaims(boolean admin){
    JWTClaimDTO claimDTO = new JWTClaimDTO();
    claimDTO.setAdmin(admin);
    claimDTO.setSubjectId(UuidUtil.generateUuid32("tnt"));
    Map mapClaims = JWTClaims.putClaims(claimDTO);
    return mapClaims;
  }
}
