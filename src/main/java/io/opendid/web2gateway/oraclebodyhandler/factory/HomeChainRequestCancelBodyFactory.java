package io.opendid.web2gateway.oraclebodyhandler.factory;

import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterface;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestCancelBodyInterfaceV2;

public class HomeChainRequestCancelBodyFactory {

  public static HomeChainRequestCancelBodyInterfaceV2 createCancelBodyHandler(
  ){

    HomeChainRequestCancelBodyInterfaceV2 bean = SpringContextUtil.getBean(
            HomeChainType.getHomeChainName()+ HomeChainRequestCancelBodyInterfaceV2.BEAN_SUFFIX
        , HomeChainRequestCancelBodyInterfaceV2.class);
    return bean;

  }
}
