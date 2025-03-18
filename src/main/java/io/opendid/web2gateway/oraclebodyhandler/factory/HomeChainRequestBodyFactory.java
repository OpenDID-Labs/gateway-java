package io.opendid.web2gateway.oraclebodyhandler.factory;

import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestBodyInterface;

public class HomeChainRequestBodyFactory {

  public static HomeChainRequestBodyInterface createRequestBodyHandler(
  ){

    HomeChainRequestBodyInterface bean = SpringContextUtil.getBean(
            HomeChainName.APTOS + HomeChainRequestBodyInterface.BEAN_SUFFIX
        , HomeChainRequestBodyInterface.class);
    return bean;

  }
}
