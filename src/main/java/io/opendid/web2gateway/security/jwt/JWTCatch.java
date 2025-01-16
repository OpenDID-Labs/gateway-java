package io.opendid.web2gateway.security.jwt;

import io.opendid.web2gateway.config.JWTCacheConfig;
import io.opendid.web2gateway.model.dto.jwtkey.JWTCatchDTO;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class JWTCatch {


  @Cacheable(cacheNames = JWTCacheConfig.CATCH_JWT_RSA_KEY,
      key ="'jwtKeyCatch'",
      cacheManager = JWTCacheConfig.MANAGER_JWT_CATCH,unless = "#result==null")
  public JWTCatchDTO getJwtCatch() {
    return null;
  }

  @CachePut(cacheNames = JWTCacheConfig.CATCH_JWT_RSA_KEY,
      key ="'jwtKeyCatch'",
      cacheManager = JWTCacheConfig.MANAGER_JWT_CATCH)
  public JWTCatchDTO cacheJwtKey(JWTCatchDTO jwtCatchDTO) {
    return jwtCatchDTO;
  }
}
