package io.opendid.web2gateway.oraclebodyhandler.factory;

import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainCreateSubscriptionInterface;

public class HomeChainCreateSubscriptionFactory {

    public static HomeChainCreateSubscriptionInterface createSubscriptionHandler(
    ){

        HomeChainCreateSubscriptionInterface bean = SpringContextUtil.getBean(
                HomeChainName.FLARE + HomeChainCreateSubscriptionInterface.BEAN_SUFFIX
                , HomeChainCreateSubscriptionInterface.class);
        return bean;

    }
}
