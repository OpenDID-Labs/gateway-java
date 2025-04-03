package io.opendid.web2gateway.oraclebodyhandler.factory;

import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainCreateSubscriptionInterface;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainSubTokenTransferInterface;

public class HomeChainSubTokenTransferFactory {

  public static HomeChainSubTokenTransferInterface createSubscriptionHandler(
  ){

    HomeChainSubTokenTransferInterface bean = SpringContextUtil.getBean(
            HomeChainType.getHomeChainName() + HomeChainSubTokenTransferInterface.BEAN_SUFFIX
        , HomeChainSubTokenTransferInterface.class);
    return bean;

  }
}
