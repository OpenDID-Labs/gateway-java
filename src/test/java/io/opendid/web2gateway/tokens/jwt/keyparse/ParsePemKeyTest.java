package io.opendid.web2gateway.tokens.jwt.keyparse;


import io.opendid.web2gateway.common.utils.PemToKey;
import io.opendid.web2gateway.security.jwt.JWTReadPem;
import io.vertx.core.buffer.Buffer;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import org.junit.jupiter.api.Test;

public class ParsePemKeyTest {

  private static final Base64.Decoder BASE64MIME_DECODER = Base64.getMimeDecoder();

  public PublicKey publicKey;
  public PrivateKey privateKey;
  @Test
  public void parsePubKey()
      throws IOException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException {
    executeParsePem();

  }

  public void executeParsePem()
      throws IOException, NoSuchAlgorithmException, CertificateException, InvalidKeySpecException {
    Path resourcesPath = Paths.get("src", "test", "resources");
    Path absolutePath = resourcesPath.toAbsolutePath();

    String privateRSAKey=
        JWTReadPem.readPem(absolutePath+ File.separator+"RSA_private_key.pem");
    String publicRSAKey =
        JWTReadPem.readPem(absolutePath+ File.separator+"RSA_public.pem");


    //parse  publicKey
    Buffer publicKeyBuffer = Buffer.buffer(publicRSAKey, "UTF-8");
    String publicKeyString = publicKeyBuffer.toString(StandardCharsets.US_ASCII);
    publicKey = PemToKey.pemToPublicKey(publicKeyString);
    System.out.println(publicKey);

    //parse  privateKey
    Buffer privateKeyBuffer = Buffer.buffer(privateRSAKey, "UTF-8");
    String privateKeyString = privateKeyBuffer.toString(StandardCharsets.US_ASCII);
    privateKey = PemToKey.pemToPrivateKey(privateKeyString);
    System.out.println(privateKey);
  }



}
