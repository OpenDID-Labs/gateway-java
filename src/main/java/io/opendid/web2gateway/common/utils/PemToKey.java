package io.opendid.web2gateway.common.utils;

import io.vertx.core.buffer.Buffer;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PemToKey {

  private static final Logger logger = LoggerFactory.getLogger(PemToKey.class);

  private static final Base64.Decoder BASE64MIME_DECODER = Base64.getMimeDecoder();
  private  static KeyFactory keyFactory;

  static {
    try {
      keyFactory = KeyFactory.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static PrivateKey pemToPrivateKey(
      String privatePemContent
  )throws  CertificateException, InvalidKeySpecException {


    //parse  privateKey
    Buffer privateKeyBuffer = Buffer.buffer(privatePemContent, "UTF-8");
    String privateKeyString = privateKeyBuffer.toString(StandardCharsets.US_ASCII);
    PrivateKey privateKey=(PrivateKey) parsePEM(keyFactory, privateKeyString);
    return privateKey;
  }

  public static PublicKey pemToPublicKey(
      String publicPemContent
  )  throws  CertificateException, InvalidKeySpecException {

    //parse  publicKey
    Buffer publicKeyBuffer = Buffer.buffer(publicPemContent, "UTF-8");
    String publicKeyString = publicKeyBuffer.toString(StandardCharsets.US_ASCII);
    PublicKey publicKey=(PublicKey) parsePEM(keyFactory, publicKeyString);
    return publicKey;
  }


  private static Object parsePEM(KeyFactory kf, String pem)
      throws CertificateException, InvalidKeySpecException {

    PublicKey publicKey=null;
    PrivateKey privateKey=null;

    // extract the information from the pem
    String[] lines = pem.split("\r?\n");
    // A PEM PKCS#8 formatted string shall contain on the first line the kind of content
    if (lines.length <= 2) {
      throw new IllegalArgumentException("PEM contains not enough lines");
    }
    // there must be more than 2 lines
    Pattern begin = Pattern.compile("-----BEGIN (.+?)-----");
    Pattern end = Pattern.compile("-----END (.+?)-----");

    Matcher beginMatcher = begin.matcher(lines[0]);
    if (!beginMatcher.matches()) {
      throw new IllegalArgumentException("PEM first line does not match a BEGIN line");
    }
    String kind = beginMatcher.group(1);
    Buffer buffer = Buffer.buffer();
    boolean endSeen = false;
    for (int i = 1; i < lines.length; i++) {
      if ("".equals(lines[i])) {
        continue;
      }
      Matcher endMatcher = end.matcher(lines[i]);
      if (endMatcher.matches()) {
        endSeen = true;
        if (!kind.equals(endMatcher.group(1))) {
          throw new IllegalArgumentException("PEM END line does not match start");
        }
        break;
      }
      buffer.appendString(lines[i]);
    }

    if (!endSeen) {
      throw new IllegalArgumentException("PEM END line not found");
    }

    switch (kind) {
      case "CERTIFICATE":
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        publicKey = cf.generateCertificate(
            new ByteArrayInputStream(pem.getBytes(StandardCharsets.US_ASCII))).getPublicKey();
        return publicKey;
      case "PUBLIC KEY":
      case "PUBLIC RSA KEY":
      case "RSA PUBLIC KEY":
        publicKey = kf.generatePublic(new X509EncodedKeySpec(base64MimeDecode(buffer.getBytes())));
        return publicKey;
      case "PRIVATE KEY":
      case "PRIVATE RSA KEY":
      case "RSA PRIVATE KEY":
        privateKey = kf.generatePrivate(
            new PKCS8EncodedKeySpec(base64MimeDecode(buffer.getBytes())));
        return privateKey;
      default:
        throw new IllegalStateException("Invalid PEM content: " + kind);
    }
  }

  public static byte[] base64MimeDecode(byte[] base64) {
    return BASE64MIME_DECODER.decode(base64);
  }

}
