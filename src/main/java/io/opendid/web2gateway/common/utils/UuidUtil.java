// Copyright 2021 Red Date Technology Ltd.  Licensed under MPLv2
// (https://www.mozilla.org/en-US/MPL/2.0/)

package io.opendid.web2gateway.common.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * class description UUID tools
 *
 * @author Dong-Jianguo
 * @version 1.0.0
 * @Date: 2021/9/16
 * @history date, modifier,and description
 **/
public class UuidUtil {

  private static final AtomicInteger atomicInteger = new AtomicInteger();

  private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
      "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
      "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
      "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
      "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
      "W", "X", "Y", "Z"};

  /**
   * method description generate 32-bit UUID
   *
   * @param
   * @return java.lang.String
   * @date 2021/9/16
   */
  public static String generateUuid32() {
    synchronized (atomicInteger) {
      return UUID.randomUUID().toString().replace("-", "");
    }
  }

  public static String generateUuid32(String prefix) {
    synchronized (atomicInteger) {
      return prefix+generateUuid32();
    }
  }


  /**
   * method description generate 32-bit UUID
   *
   * @param
   * @return java.lang.String
   * @date 2021/9/16
   */

  public static String generateRandom8() {
    synchronized (atomicInteger) {

      //Invoke the object provided by Java to generate random strings: 32-bit, hexadecimal
      StringBuffer shortBuffer = new StringBuffer();
      String uuid = UuidUtil.generateUuid32();

      //divide into 8 groups
      for (int i = 0; i < 8; i++) {
        //4 bits per group
        String str = uuid.substring(i * 4, i * 4 + 4);
        //Convert 4-bit str to int hexadecimal representation
        int x = Integer.parseInt(str, 16);

        //Use the hexadecimal number to take modulo 62 (hexadecimal representation is 314 (14 is E)),
        // and the result is used as an index to extract the character
        shortBuffer.append(chars[x % 0x3E]);
      }
      return shortBuffer.toString();
    }
  }

}
