package io.opendid.web2gateway.security.jwt;

import io.opendid.web2gateway.common.utils.PemToKey;
import io.opendid.web2gateway.common.utils.UuidUtil;
import io.opendid.web2gateway.model.dto.jwtkey.JWTCatchDTO;
import io.opendid.web2gateway.service.JwtTokenService;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTKeyLoader {

  public static boolean JWTRootGenerated=false;

  @Autowired
  JwtTokenService jwtTokenService;

  @Autowired
  private JWTCatch jwtCatch;

  private Logger logger= LoggerFactory.getLogger(JWTKeyLoader.class);
  @Value("${jwt-key.private-pem-filepath}")
  private String privatePemPath;

  @Value("${jwt-key.public-pem-filepath}")
  private String publicPemPath;

  @Autowired
  private  JwtCreate jwtCreate;

  public void load(){
    try {
      String adminJwt = getJwt();
      String privatePem = JWTReadPem.readPem(privatePemPath);
      String publicPem = JWTReadPem.readPem(publicPemPath);
      PrivateKey privateKey = PemToKey.pemToPrivateKey(privatePem);
      PublicKey publicKey = PemToKey.pemToPublicKey(publicPem);
      jwtLoadToCatch(privateKey, publicKey);

      if(StringUtils.isBlank(adminJwt)){
        String createAdminJwt = jwtCreate.generateAdminJwt();
        jwtTokenService.updateAdminJwt(createAdminJwt);
        logger.info("JWT ROOT-Token generated successfully");
      }else{
        logger.info("JWT ROOT-token already exists");
      }
      adminJwt = getJwt();
      if(StringUtils.isNotBlank(adminJwt)){
        JWTRootGenerated=true;
        logger.info("JWTRootGenerated to true successfully");
      }

    } catch (IOException | CertificateException | InvalidKeySpecException e) {
      logger.error("Failed to load pem file", e);
    }

  }

  private String getJwt() {
    String adminJwt = jwtTokenService.selectAdminJwt();
    return adminJwt;
  }

  private void jwtLoadToCatch(PrivateKey privateKey, PublicKey publicKey) {
    JWTCatchDTO catchDTO=new JWTCatchDTO();
    catchDTO.setID(UuidUtil.generateUuid32());
    catchDTO.setPrivateKey(privateKey);
    catchDTO.setPublicKey(publicKey);
    jwtCatch.cacheJwtKey(catchDTO);
  }

}
