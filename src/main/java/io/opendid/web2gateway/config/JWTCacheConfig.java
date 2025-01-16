package io.opendid.web2gateway.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class JWTCacheConfig {
  public static final String MANAGER_JWT_CATCH = "jwtCatch";
  public static final String CATCH_JWT_RSA_KEY = "catchJwtRsaKey";


  @Bean(JWTCacheConfig.MANAGER_JWT_CATCH)
  @Primary
  public CacheManager caffeineCacheManager() {
    Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
        .initialCapacity(5)
        .maximumSize(10);
//        .expireAfterWrite(10, TimeUnit.MINUTES);
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(caffeine);
    return cacheManager;
  }
}
