package io.opendid.web2gateway.common.utils;

import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GatewayKeyVaultUtil {

  public static String servicePublicKey = "service_public_key";
//  public static String walletPublicKey = "wallet_public_key";
//  public static String walletAddress = "wallet_address";
  public static String walletPrivateKey = "wallet_private_key";

  private static Map<String, String> keys = new ConcurrentHashMap<>();
  private static Map<String, ChainKeyDTO> chainKeys = new ConcurrentHashMap<>();

  public static String getServiceKey(String key){
    return keys.get(key);
  }

  public static void putServiceValue(String key, String value){
    keys.put(key,value);
  }



  public static ChainKeyDTO getChainKeyByKeyCode(String keyCode){
    return chainKeys.get(keyCode);
  }

  public static void putChainValueByKeyCode(ChainKeyDTO value){
    chainKeys.put(value.getKeyCode(),value);
  }


  public static ChainKeyDTO getChainKeyByVnCode(String keyCode){
    return chainKeys.get(keyCode);
  }

  public static void putChainValueByVnCode( ChainKeyDTO value){
    chainKeys.put(value.getVnCode(),value);
  }


}
