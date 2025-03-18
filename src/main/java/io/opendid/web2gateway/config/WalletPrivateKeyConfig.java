package io.opendid.web2gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
*  WalletPrivateKeyConfig
* @author Dong-Jianguo
* @Date: 2025/2/22
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Component
@ConfigurationProperties(prefix = "wallet")
public class WalletPrivateKeyConfig {


  public static final String KEY_NAME="keyCode";
  public static final String KEY_VALUE="keyValue";


  private List<Map<String,String>> privatekey;


    public List<Map<String, String>> getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(List<Map<String, String>> privatekey) {
        this.privatekey = privatekey;
    }
}
