package io.opendid.web2gateway.tokens.jwt;

import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

public class RootToken {


  @Test
  public  void generateRootToken() throws Exception {
    byte[] bytes = hashSHA256(UUID.randomUUID().toString());
    String hashBase64Encode = Base64.encodeBase64String(bytes);//give to
    System.out.println("token: "+hashBase64Encode);

  }

  private static byte[] hashSHA256(String data) throws Exception {
    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
    return digest.digest(data.getBytes());
  }

}
