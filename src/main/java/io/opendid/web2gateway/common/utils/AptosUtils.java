package io.opendid.web2gateway.common.utils;

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AptosUtils {


  public static String generateAddressFromPublicKey(String publicKey) throws Exception {

    byte[] publicKeyByte = HexUtils.hexToByteArray(publicKey);

    ByteBuffer buffer = ByteBuffer.allocate(publicKeyByte.length + 1);
    buffer.put(publicKeyByte);
    buffer.put((byte) 0x00);

    MessageDigest digest = MessageDigest.getInstance("SHA3-256");
    byte[] authKeyBytes = digest.digest(buffer.array());
    StringBuilder hexString = new StringBuilder();
    byte[] var4 = authKeyBytes;
    int var5 = authKeyBytes.length;

    for(int var6 = 0; var6 < var5; ++var6) {
      byte b = var4[var6];
      String hex = Integer.toHexString(255 & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }

      hexString.append(hex);
    }

    return "0x" + hexString;
  }

  public static String generatePublicKeyFromPrivateKey(String privateKeyHex) {

    byte[] privateKeyBytes = hexStringToByteArray(privateKeyHex.replace("0x", ""));

    Ed25519PrivateKeyParameters ed25519PrivateKeyParameters = new Ed25519PrivateKeyParameters(privateKeyBytes, 0);

    Ed25519PublicKeyParameters publicKey = ed25519PrivateKeyParameters.generatePublicKey();

    byte[] publicKeyEncoded = publicKey.getEncoded();

    String publicKeyHex = bytesToHex(publicKeyEncoded);

    return "0x" + publicKeyHex;
  }

  public static byte[] ed25519Sign(byte[] privateKey, byte[] data) {
    Ed25519PrivateKeyParameters key = new Ed25519PrivateKeyParameters(privateKey, 0);
    Ed25519Signer signer = new Ed25519Signer();
    signer.init(true, key);
    signer.update(data, 0, data.length);
    byte[] rst = signer.generateSignature();
    return rst;
  }


  public static byte[] hexToByteArray(String h) {
    String tmp = h.substring(0, 2);
    if ("0x".equals(tmp)) {
      h = h.substring(2);
    }

    int hexlen = h.length();
    byte[] result;
    if (hexlen % 2 == 1) {
      ++hexlen;
      result = new byte[hexlen / 2];
      h = "0" + h;
    } else {
      result = new byte[hexlen / 2];
    }

    int j = 0;

    for(int i = 0; i < hexlen; i += 2) {
      result[j] = hexToByte(h.substring(i, i + 2));
      ++j;
    }

    return result;
  }


  public static byte hexToByte(String h) {
    return (byte)Integer.parseInt(h, 16);
  }


  public static String byteArrayToHexWithPrefix(byte[] bytes) {
    return "0x" + byteArrayToHex(bytes);
  }


  public static String byteArrayToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    int index = 0;

    for(int len = bytes.length; index <= len - 1; ++index) {
      String hex1 = Integer.toHexString(bytes[index] >> 4 & 15);
      String hex2 = Integer.toHexString(bytes[index] & 15);
      result.append(hex1);
      result.append(hex2);
    }

    return result.toString();
  }


  public static String genRequestId(String sender, long nonce) throws NoSuchAlgorithmException {
    // Convert sender's hexadecimal string to a byte array
    // Remove "0x"
    byte[] senderBytes = hexStringToByteArray(sender.substring(2));

    // Convert nonce to a little-endian byte array
    byte[] nonceBytes = ByteBuffer.allocate(Long.BYTES)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putLong(nonce)
        .array();

    // Merge senderBytes and nonceBytes
    byte[] combined = new byte[senderBytes.length + nonceBytes.length];
    System.arraycopy(senderBytes, 0, combined, 0, senderBytes.length);
    System.arraycopy(nonceBytes, 0, combined, senderBytes.length, nonceBytes.length);

    // Calculate hash value using SHA3-256
    MessageDigest digest = MessageDigest.getInstance("SHA3-256");
    byte[] hash = digest.digest(combined);

    // Convert hash value to hexadecimal string
    return "0x" + bytesToHex(hash);
  }

  private static byte[] hexStringToByteArray(String hex) {
    int length = hex.length();
    byte[] data = new byte[length / 2];
    for (int i = 0; i < length; i += 2) {
      data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
          + Character.digit(hex.charAt(i + 1), 16));
    }
    return data;
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        // Ensure that each byte is two bits
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }


}
