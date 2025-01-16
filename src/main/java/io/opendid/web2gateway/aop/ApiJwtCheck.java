package io.opendid.web2gateway.aop;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.micrometer.common.util.StringUtils;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.contextfilter.security.SecurityContextHolder;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2UnauthorizedRequestException;
import io.opendid.web2gateway.model.dto.admin.TenantJwCancelReqDTO;
import io.opendid.web2gateway.model.dto.jwtclaim.JWTClaimDTO;
import io.opendid.web2gateway.model.dto.jwtkey.JWTCatchDTO;
import io.opendid.web2gateway.repository.model.GatewayKeyVault;
import io.opendid.web2gateway.repository.model.TenantJwtManger;
import io.opendid.web2gateway.security.authorization.AuthorizationBearer;
import io.opendid.web2gateway.security.jwt.JWTCatch;
import io.opendid.web2gateway.security.jwt.JWTUtil;
import io.opendid.web2gateway.service.GatewayKeyVaultService;
import io.opendid.web2gateway.service.JwtTokenService;
import java.security.PublicKey;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


@Aspect
@Component
@Order(15)
public class ApiJwtCheck {

  private Logger logger = LoggerFactory.getLogger(ApiJwtCheck.class);

  @Autowired
  private JWTCatch jwtCatch;

  @Autowired
  private GatewayKeyVaultService keyVaultService;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Pointcut("@annotation(io.opendid.web2gateway.security.checkaspect.TenantJwtTokenCheck)")
  private void tenantJwtCheck() {

  }


  @Before("tenantJwtCheck()")
  public void beforeMethod(
      JoinPoint joinPoint) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

    ServerWebExchange exchange = (ServerWebExchange) joinPoint.getArgs()[0];
    HttpHeaders headers = exchange.getRequest().getHeaders();
    String headerValue = headers.getFirst("Authorization");

    if (StringUtils.isBlank(headerValue)) {
      throw new JsonRpc2UnauthorizedRequestException(traceId,
          "Authorization header is required");
    }

    String bearerJwt = AuthorizationBearer.getBearer(headerValue);

    verifyJwt(bearerJwt, traceId);

    logger.info("Header Authorization: {}", headerValue);

  }

  private void verifyJwt(String bearerJwt, String traceId) {
    JWTCatchDTO jwtCatch1 = jwtCatch.getJwtCatch();
    PublicKey publicKey = jwtCatch1.getPublicKey();
    String subId="";
    try {

      Jws<Claims> claimsJws1 = JWTUtil.parseJwtToken(bearerJwt, publicKey);
      subId = (String) claimsJws1.getPayload().get(JWTClaimDTO.SUB_ID);
      boolean admin = (boolean) claimsJws1.getPayload().get(JWTClaimDTO.ADMIN);
//      boolean admin=(boolean) claimsJws.getPayload().get(JWTClaimDTO.ADMIN);

      if (admin) {
        checkAdminInDb(bearerJwt, traceId);
      } else {

        checkTenantInDb(bearerJwt, traceId, subId);
      }
      Jws<Claims> claimsJws = JWTUtil.validateJwtToken(bearerJwt, publicKey);
      settingHolder(bearerJwt, claimsJws);

    }
    catch (SignatureException e) {
//      logger.error("Invalid JWT signature: {}", e.getMessage());
      throw new JsonRpc2UnauthorizedRequestException(traceId,
          "Invalid JWT Signature"
      );
    } catch (MalformedJwtException e) {
//      logger.error("Invalid JWT token: {}", e.getMessage());
      throw new JsonRpc2UnauthorizedRequestException(traceId,
          "Invalid JWT token"
      );

    } catch (ExpiredJwtException e) {
      cancelJwt(subId);
      throw new JsonRpc2UnauthorizedRequestException(traceId,
          "JWT token is expired");
//      logger.error("JWT token is expired: {}", e.getMessage());
//      throw e;

    } catch (UnsupportedJwtException e) {
      throw new JsonRpc2UnauthorizedRequestException(traceId,
          "JWT token is unsupported");
//      logger.error("JWT token is unsupported: {}", e.getMessage());
//      throw e;

    } catch (IllegalArgumentException e) {
      throw new JsonRpc2UnauthorizedRequestException(traceId,
          "JWT claims string is empty");
//      logger.error("JWT claims string is empty: {}", e.getMessage());
//      throw e;

    }  catch (Throwable e) {
      throw new JsonRpc2UnauthorizedRequestException(traceId, e.getMessage());

    }
  }

  private void cancelJwt(String subId) {
    TenantJwCancelReqDTO reqDTO=new TenantJwCancelReqDTO();
    reqDTO.setSubId(subId);
    int count = jwtTokenService.deleteTenantJwt(reqDTO);
    if (count > 0) {
      logger.info("Tenant JWT has been cancelled");
    }
  }

  private void checkAdminInDb(String bearerJwt, String traceId) {
    GatewayKeyVault gatewayKeyVault = keyVaultService.selectCurrentKeyVault();
    if (gatewayKeyVault != null
        && StringUtils.isNotBlank(bearerJwt)) {
      if (!bearerJwt.equals(gatewayKeyVault.getAdminJwt())) {
        throw new JsonRpc2UnauthorizedRequestException(traceId,
            "It is inconsistent with the currently generated admin-token.");
      }
    } else {
      throw new JsonRpc2UnauthorizedRequestException(traceId,
          "It is inconsistent with the currently generated admin-token.");
    }
  }

  private void checkTenantInDb(String bearerJwt,
      String traceId, String subId) {
    TenantJwtManger tenantJwt = jwtTokenService.selectTenantJwtBySubId(subId);
    if (tenantJwt != null &&
        StringUtils.isNotBlank(tenantJwt.getJwt())) {
      if (!bearerJwt.equals(tenantJwt.getJwt())) {
        throw new JsonRpc2UnauthorizedRequestException(traceId, "Invalid JWT token.");
      }
    } else {
      throw new JsonRpc2UnauthorizedRequestException(traceId, "Invalid JWT token.");
    }
  }

  private static void settingHolder(String bearerJwt, Jws<Claims> claimsJws) {
    String subject = claimsJws.getPayload().getSubject();
    boolean admin = (boolean) claimsJws.getPayload().get(JWTClaimDTO.ADMIN);
    String subjectId = (String) claimsJws.getPayload().get(JWTClaimDTO.SUB_ID);
    SecurityContextHolder.getContext().setJwt(bearerJwt);
    SecurityContextHolder.getContext().setSubjectId(subjectId);
    SecurityContextHolder.getContext().setSubject(subject);
    SecurityContextHolder.getContext().setAdmin(admin);
  }


}