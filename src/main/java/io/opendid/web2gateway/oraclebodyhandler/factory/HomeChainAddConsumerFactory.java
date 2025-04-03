package io.opendid.web2gateway.oraclebodyhandler.factory;

import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainAddConsumerInterface;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainCreateSubscriptionInterface;

public class HomeChainAddConsumerFactory {

    public static HomeChainAddConsumerInterface createSubscriptionHandler(
    ){

        HomeChainAddConsumerInterface bean = SpringContextUtil.getBean(
                HomeChainType.getHomeChainName() + HomeChainAddConsumerInterface.BEAN_SUFFIX
                , HomeChainAddConsumerInterface.class);

        return bean;
    }
}
