package io.opendid.web2gateway.common.utils;

import cn.hutool.core.codec.Base64;
import java.math.BigInteger;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

/**
*  ECDSAUtils
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class ECDSAUtils {

  public static final String type = "Secp256k1";
  public static final String signFlag = "SHA256withECDSA";
  private static String data = "ecdsa security";


  public static String getPublicKey(String privateKey) {

    String ecKeyPair = null;
    try {
      ECKeyPair keyPair = ECKeyPair.create(new BigInteger(privateKey));
      ecKeyPair = String.valueOf(keyPair.getPublicKey());
    } catch (Exception e) {
      throw new RuntimeException("invalid privateKey");
    }
    return ecKeyPair;
  }

  public static String getAddress(BigInteger publicKey) {

    return Keys.getAddress(publicKey);
  }

//  public static void main(String[] args) throws Exception {
//
//    ECKeyPair keyPairOriginal = Keys.createEcKeyPair();
//    String privateKey = keyPairOriginal.getPrivateKey().toString();
//    System.out.println("--------------- privateKey = " + privateKey);
//    String publicKey = keyPairOriginal.getPublicKey().toString();
//
//    System.out.println("--------------- publicKey = " + publicKey);
//    String address = getAddress(new BigInteger(publicKey));
//    System.out.println("--------------- address = " + address);
//
//  }

  /**
   * The message generates a signature based on the private key
   *
   * @param message
   * @param privateKey
   * @param publicKey
   * @author shaopengfei
   */
  public static String sign(String message, String privateKey, String publicKey) throws Exception {
    try {
      Sign.SignatureData signatureData =
          sign(
              message.getBytes(),
              new ECKeyPair(new BigInteger(privateKey), new BigInteger(publicKey)));
      return secp256k1SigBase64Serialization(signatureData);
    } catch (Exception e) {
      // e.printStackTrace();
      throw new Exception("invalid privateKey or publicKey");
    }
  }

  /**
   * Verifying the signature of a message based on the public key
   *
   * @param message
   * @param publicKey
   * @param signValue
   * @author shaopengfei
   */
  public static boolean verify(String message, String publicKey, String signValue)
      throws Exception {
    try {
      byte[] bytes = Hash.sha3(message.getBytes());
      BigInteger bigInteger = new BigInteger(publicKey);
      Sign.SignatureData signatureData = secp256k1SigBase64Deserialization(signValue);
      return verify(bytes, bigInteger, signatureData);
    } catch (Exception e) {
      // e.printStackTrace();
      throw new Exception("invalid publicKey");
    }
  }

  private static Sign.SignatureData sign(byte[] message, ECKeyPair keyPair) {
    return Sign.signMessage(message, keyPair, true);
  }

  private static boolean verify(
      byte[] hash, BigInteger publicKey, Sign.SignatureData signatureData) {
    ECDSASignature sig =
        new ECDSASignature(
            Numeric.toBigInt(signatureData.getR()), Numeric.toBigInt(signatureData.getS()));
    byte[] v = signatureData.getV();
    byte recld = v[0];
    BigInteger k = Sign.recoverFromSignature(recld - 27, sig, hash);
    return publicKey.equals(k);
  }

  private static String secp256k1SigBase64Serialization(Sign.SignatureData sigData) {
    byte[] sigBytes = new byte[65];
    sigBytes[64] = sigData.getV()[0];
    System.arraycopy(sigData.getR(), 0, sigBytes, 0, 32);
    System.arraycopy(sigData.getS(), 0, sigBytes, 32, 32);
    return new String(Base64.encode(sigBytes));
  }

  private static Sign.SignatureData secp256k1SigBase64Deserialization(String signature) {
    byte[] sigBytes = Base64.decode(signature);
    byte[] r = new byte[32];
    byte[] s = new byte[32];
    System.arraycopy(sigBytes, 0, r, 0, 32);
    System.arraycopy(sigBytes, 32, s, 0, 32);
    return new Sign.SignatureData(sigBytes[64], r, s);
  }
}

