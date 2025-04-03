package io.opendid.web2gateway.oraclebodyhandler.factory;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HomeChainType {
  private static final Logger logger = LoggerFactory.getLogger(HomeChainType.class);

  @Value("${homechain_name:APTOS}")
  private String homeChainName;

  private static String staticHomeChainName;

  @PostConstruct
  public void init() {
    staticHomeChainName = homeChainName;
  }

  public static String getHomeChainName() {
    logger.info("HomeChainType staticHomeChainName={}",staticHomeChainName);
    return staticHomeChainName;
  }

}
