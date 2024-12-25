package io.opendid.web2gateway.common.utils;

import java.util.ArrayList;
import java.util.List;

public class HexUtils {
  public HexUtils() {
  }

  public static byte[] hexToAccountAddressBytes(String hex) {
    byte[] addressBytes = hexToByteArray(hex);
    if (addressBytes.length < 32) {
      byte[] bs = new byte[32];

      for(int i = 0; i < addressBytes.length; ++i) {
        bs[bs.length - addressBytes.length - i] = addressBytes[i];
      }

      addressBytes = bs;
    }

    return addressBytes;
  }

  public static byte[][] hexArrayToByteArrays(String[] hs) {
    List<byte[]> bytesList = new ArrayList(hs.length);
    String[] var2 = hs;
    int var3 = hs.length;

    for(int var4 = 0; var4 < var3; ++var4) {
      String h = var2[var4];
      bytesList.add(hexToByteArray(h));
    }

    return (byte[][])bytesList.toArray(new byte[0][]);
  }

  public static byte hexToByte(String h) {
    return (byte)Integer.parseInt(h, 16);
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

  public static String byteListToHexWithPrefix(List<Byte> bytes) {
    return "0x" + byteListToHex(bytes);
  }

  public static String byteListToHex(List<Byte> bytes) {
    byte[] byteArray = toPrimitive((Byte[])bytes.toArray(new Byte[0]));
    return byteArrayToHex(byteArray);
  }

  public static byte[] toPrimitive(Byte[] array) {
    if (array == null) {
      return null;
    } else if (array.length == 0) {
      return new byte[0];
    } else {
      byte[] result = new byte[array.length];

      for(int i = 0; i < array.length; ++i) {
        result[i] = array[i];
      }

      return result;
    }
  }

  public static String byteArrayToHexWithPrefix(byte[] bytes) {
    return "0x" + byteArrayToHex(bytes);
  }

  public static String[] bytesArrayToHexArray(byte[][] bytes) {
    List<String> hexArray = new ArrayList();
    byte[][] var2 = bytes;
    int var3 = bytes.length;

    for(int var4 = 0; var4 < var3; ++var4) {
      byte[] bs = var2[var4];
      String h = bs == null ? null : byteArrayToHex(bs);
      hexArray.add(h);
    }

    return (String[])hexArray.toArray(new String[0]);
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
}

