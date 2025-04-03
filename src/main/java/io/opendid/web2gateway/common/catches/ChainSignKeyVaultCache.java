package io.opendid.web2gateway.common.catches;

import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChainSignKeyVaultCache {

  public static String walletPrivateKey = "wallet_private_key";

  private static Map<String, ChainKeyDTO> chainKeys = new ConcurrentHashMap<>();




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

  public static void putChainValueByWalletAddress( ChainKeyDTO value){
    chainKeys.put(value.getWalletAddress(),value);
  }

  public static ChainKeyDTO getChainKeyByWalletAddress( String walletAddress){
    return chainKeys.get(walletAddress);
  }



}
