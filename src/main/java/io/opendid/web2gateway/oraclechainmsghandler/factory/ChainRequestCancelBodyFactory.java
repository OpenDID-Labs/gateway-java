package io.opendid.web2gateway.oraclechainmsghandler.factory;

import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.oraclechainmsghandler.interfaces.ChainRequestCancelBodyHandler;

public class ChainRequestCancelBodyFactory {

  public static ChainRequestCancelBodyHandler createCancelBodyHandler(
      String  homeChainName
  ){

    ChainRequestCancelBodyHandler bean = SpringContextUtil.getBean(
        homeChainName + ChainRequestCancelBodyHandler.BEAN_SUFFIX
        , ChainRequestCancelBodyHandler.class);
    return bean;

  }
}
