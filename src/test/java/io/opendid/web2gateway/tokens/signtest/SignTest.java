package io.opendid.web2gateway.tokens.signtest;

import io.opendid.web2gateway.common.utils.PemToKey;
import io.opendid.web2gateway.security.jwt.JWTReadPem;
import io.vertx.core.buffer.Buffer;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

public class SignTest {

  @Test
  public void generate() throws Exception {
    Path resourcesPath = Paths.get("src", "test", "resources");
    Path absolutePath = resourcesPath.toAbsolutePath();

    String privateRSAKey=
        JWTReadPem.readPem(absolutePath+ File.separator+"RSA_private_key.pem");
    String publicRSAKey =
        JWTReadPem.readPem(absolutePath+ File.separator+"RSA_public.pem");

    //parse  privateKey
    Buffer privateKeyBuffer = Buffer.buffer(privateRSAKey, "UTF-8");
    String privateKeyString = privateKeyBuffer.toString(StandardCharsets.US_ASCII);
    PrivateKey privateKey = PemToKey.pemToPrivateKey(privateKeyString);
    System.out.println(privateKey);


    //parse  publicKey
    Buffer publicKeyBuffer = Buffer.buffer(publicRSAKey, "UTF-8");
    String publicKeyString = publicKeyBuffer.toString(StandardCharsets.US_ASCII);
    PublicKey publicKey = PemToKey.pemToPublicKey(publicKeyString);

    String publicKeyEncoded = Base64.encodeBase64String(publicKey.getEncoded());





    //generate signature to base64
    byte[] hash = hashSHA256(publicKeyEncoded);
    String hashBase64Encode = Base64.encodeBase64String(hash);//give to
    byte[] signature = sign(hash, privateKey);
    String signatureBase64Encode = Base64.encodeBase64String(signature);
    System.out.println("TokenBase64: " + signatureBase64Encode);

    //verify
    byte[] hashBase64Decode = Base64.decodeBase64(hashBase64Encode);//get from db
    byte[] signatureBase64Decode = Base64.decodeBase64(signatureBase64Encode.getBytes());

    boolean verified = verify(hashBase64Decode, signatureBase64Decode, publicKey);
    System.out.println("Token verified: " + verified);


  }

  @Test
  public void sign2(){

  }


  private static byte[] hashSHA256(String data) throws Exception {
    java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
    return digest.digest(data.getBytes());
  }

  private static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initSign(privateKey);
    signature.update(data);
    return signature.sign();
  }

  private static boolean verify(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
    Signature sig = Signature.getInstance("SHA256withRSA");
    sig.initVerify(publicKey);
    sig.update(data);
    return sig.verify(signature);
  }
}
