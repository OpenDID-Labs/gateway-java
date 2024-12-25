package io.opendid.web2gateway.common.utils;

import java.math.BigInteger;

/**
*  Base Conversion Util
* @author Dong-Jianguo
* @Date: 2024/10/24
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class BaseConversionUtil {


  public static String decToHexAddress(String value){

    BigInteger decimalValue = new BigInteger(value);

    String hexAddress = decimalValue.toString(16);

    hexAddress = String.format("%40s", hexAddress).replace(' ', '0');

    return "0x" + hexAddress;
  }

}
