package io.opendid.web2gateway.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2UnauthorizedRequestException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

import io.opendid.web2gateway.model.dto.admin.TenantGenerateAccessTokenResDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class JWTUtil {

  private static  Logger logger= LoggerFactory.getLogger(JWTUtil.class);

  public static TenantGenerateAccessTokenResDTO generateAccessToken(
      String userName,
      Map<String,String> claims,
      PrivateKey jwtPrivateKey,
      long accessExpirationMs)  {
    JwtBuilder jwtBuilder = Jwts.builder()
        .subject(userName)
        .claims(claims)
        .issuedAt(new Date());
    TenantGenerateAccessTokenResDTO tenantGenerateAccessTokenResDTO = new TenantGenerateAccessTokenResDTO();
    if (accessExpirationMs>0L){
      Date date = new Date((new Date()).getTime() + accessExpirationMs);
      jwtBuilder
          .expiration(date);
      tenantGenerateAccessTokenResDTO.setExpTime(date);
    }

    tenantGenerateAccessTokenResDTO.setJwtToken(jwtBuilder.signWith(jwtPrivateKey)
            .compact());
    return tenantGenerateAccessTokenResDTO;

  }

  public static Jws<Claims> validateJwtToken(String authToken, PublicKey jwtPublicKey) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

    try {
      Jws<Claims> claimsJws = Jwts.parser().verifyWith(jwtPublicKey)
          .build().parseSignedClaims(authToken);

      return claimsJws;
    } catch (SignatureException e) {
//      logger.error("Invalid JWT signature: {}", e.getMessage());
//      throw new JsonRpc2UnauthorizedRequestException(traceId,
//          "Invalid JWT Signature"
//          );
      throw  e;
    } catch (MalformedJwtException e) {
//      logger.error("Invalid JWT token: {}", e.getMessage());
//      throw new JsonRpc2UnauthorizedRequestException(traceId,
//          "Invalid JWT token"
//      );
      throw e;

    } catch (ExpiredJwtException e) {
//      throw new JsonRpc2UnauthorizedRequestException(traceId,
//          "JWT token is expired");
//      logger.error("JWT token is expired: {}", e.getMessage());
      throw e;

    } catch (UnsupportedJwtException e) {
//      throw new JsonRpc2UnauthorizedRequestException(traceId,
//          "JWT token is unsupported");
//      logger.error("JWT token is unsupported: {}", e.getMessage());
      throw e;

    } catch (IllegalArgumentException e) {
//      throw new JsonRpc2UnauthorizedRequestException(traceId,
//          "JWT claims string is empty");
//      logger.error("JWT claims string is empty: {}", e.getMessage());
      throw e;

    } /*catch (NoSuchAlgorithmException e) {
      System.out.println("no such algorithm exception");
    }*/ /*catch (InvalidKeySpecException e) {
      System.out.println("invalid key exception");
    }*/

  }

  public static  boolean isSignedJwtToken(String authToken, PublicKey jwtPublicKey) {
    boolean signed = Jwts.parser().verifyWith(jwtPublicKey)
        .build().isSigned(authToken);

    return signed;
  }

  public static  Jws<Claims> parseJwtToken(String authToken,PublicKey publicKey) {
    Jws<Claims> claimsJws = Jwts.parser().verifyWith(publicKey)
        .clock(new Clock() {
          @Override
          public Date now() {
            return new Date(0);
          }
        }).build().parseSignedClaims(authToken);
    return claimsJws;
  }
}
