package io.opendid.web2gateway.common.catches;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceKeyVaultCache {

  public static String servicePublicKey = "service_public_key";

  private static Map<String, String> keys = new ConcurrentHashMap<>();

  public static String getServiceKey(String key){
    return keys.get(key);
  }

  public static void putServiceValue(String key, String value){
    keys.put(key,value);
  }




}
