package io.opendid.web2gateway.common.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GatewayKeyVaultUtil {

  public static String servicePublicKey = "service_public_key";
  public static String walletPublicKey = "wallet_public_key";
  public static String walletAddress = "wallet_address";

  private static Map<String, String> keys = new ConcurrentHashMap<>();

  public static String getKey(String key){
    return keys.get(key);
  }

  public static void putValue(String key,String value){
    keys.put(key,value);
  }


}
