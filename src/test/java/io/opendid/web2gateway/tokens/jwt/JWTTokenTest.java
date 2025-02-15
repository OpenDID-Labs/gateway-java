package io.opendid.web2gateway.tokens.jwt;

import io.opendid.web2gateway.model.dto.admin.TenantGenerateAccessTokenResDTO;
import io.opendid.web2gateway.security.jwt.JWTUtil;
import io.opendid.web2gateway.tokens.jwt.keyparse.ParsePemKeyTest;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import org.junit.jupiter.api.Test;


public class JWTTokenTest {

  int accessExpirationMs = 9600000;


  /*
    openssl genrsa -out RSA_private.pem 2048
    openssl pkcs8 -topk8 -inform PEM -in RSA_private.pem -out RSA_private_key.pem -nocrypt
    openssl rsa -in RSA_private.pem -outform PEM -pubout -out RSA_public.pem
    */
  @Test
  public void generateJWT()
      throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    ParsePemKeyTest parsePemKeyTest = new ParsePemKeyTest();
    parsePemKeyTest.executeParsePem();

    //generate  jwt
    PrivateKey privateKey = parsePemKeyTest.privateKey;
    String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());


    Map claims = new HashMap();
    claims.put("admin", true);

    TenantGenerateAccessTokenResDTO tenantGenerateAccessTokenResDTO = JWTUtil.generateAccessToken(
            "solomon",
            claims, privateKey, accessExpirationMs);

    System.out.println("JWT Token========  " + tenantGenerateAccessTokenResDTO.getJwtToken());
    //jwt end

//    verify
    PublicKey publicKey = parsePemKeyTest.publicKey;
    String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
    System.out.println(JWTUtil.validateJwtToken(tenantGenerateAccessTokenResDTO.getJwtToken(), publicKey));
//      verify end
  }

//  public PublicKey generateJwtKeyDecryption(String jwtPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//    byte[] keyBytes = Base64.decodeBase64(jwtPublicKey);
//    X509EncodedKeySpec x509EncodedKeySpec=new X509EncodedKeySpec(keyBytes);
//    return keyFactory.generatePublic(x509EncodedKeySpec);
//  }
//
//  public PrivateKey generateJwtKeyEncryption(String jwtPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
//    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//    byte[] keyBytes = Base64.decodeBase64(jwtPrivateKey);
//    PKCS8EncodedKeySpec pkcs8EncodedKeySpec=new PKCS8EncodedKeySpec(keyBytes);
//    return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
//  }

}
