package io.opendid.web2gateway.jwt;

import com.alibaba.fastjson.JSON;
import io.opendid.web2gateway.config.JWTCacheConfig;
import io.opendid.web2gateway.model.dto.jwtkey.JWTCatchDTO;
import io.opendid.web2gateway.security.jwt.JWTCatch;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {JWTCatch.class, JWTCacheConfig.class})
public class JWTCatchTest {


  @Autowired
  JWTCatch jwtCatch;

  @Test
  public void testCatch(){
    String uuid = UUID.randomUUID().toString();
    JWTCatchDTO dto=new JWTCatchDTO();
    dto.setPrivateKey(null);
    dto.setPublicKey(null);
    dto.setID(uuid);
    jwtCatch.cacheJwtKey(dto);

    JWTCatchDTO catchDTO = jwtCatch.getJwtCatch();
    System.out.println(JSON.toJSONString(catchDTO));
  }
}