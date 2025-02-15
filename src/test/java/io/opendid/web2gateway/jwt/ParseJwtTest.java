package io.opendid.web2gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.opendid.web2gateway.Web2GatewayApplication;
import io.opendid.web2gateway.config.JWTCacheConfig;
import io.opendid.web2gateway.model.dto.jwtkey.JWTCatchDTO;
import io.opendid.web2gateway.security.jwt.JWTCatch;
import io.opendid.web2gateway.tokens.jwt.keyparse.ParsePemKeyTest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


public class ParseJwtTest {


  private static final String SECRET_KEY = "your-secret-key";

  @Test
  public void test()
      throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    String jwt="eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInN1YklkIjoidG50ZTAzMjcyNDY3ZWQwNGNlMGFkMjk2MTU3NjkzODI3YmIiLCJhZG1pbiI6ZmFsc2UsImlhdCI6MTczNjc0OTAxMywiZXhwIjoxNzM2NzQ5MDEzfQ.C0Usbgb_gDKLkOsJ-thpYG0YwoieSuDiVAUL5o-j9nHQRxL2m9OrlcMNrExSX9jxLswOUJRgaSk-HujVwCMgOcSpTSPBSMSIYWpWwPijhbTKnKLl1dfW1P87RBuFHynU7weQ6fLspnUOZn4cyEwAltrBHZOyeZ22xjgz6B0I8rGsy_9jA-a_BnmVotijGlfCS_tJ3IsciNX87Wf5NGKOGfH8zsTOhi4HxC9rPcBSicGUmWsZxyPSwIMdMVKnED8uauMTB_8wxWYIXxm0Uf0VFpP5SUvqcXf50o5ggdrROWcp_LJjxuSjWq58jy120VQVl9jRtAdJcTjshFZJ2Kvslw";

    ParsePemKeyTest parsePemKeyTest=new ParsePemKeyTest();
    parsePemKeyTest.parsePubKey();
    PublicKey publicKey = parsePemKeyTest.publicKey;
    PrivateKey privateKey = parsePemKeyTest.privateKey;

    boolean signed = Jwts.parser().verifyWith(publicKey).build().isSigned(jwt);


    Jws<Claims> claimsJws = Jwts.parser().verifyWith(publicKey)
        .clock(new Clock() {
          @Override
          public Date now() {
            return new Date(0);
          }
        }).build().parseSignedClaims(jwt);

    System.out.println(claimsJws);

  }
}
