
package io.opendid.web2gateway.oraclebodyhandler.factory;

import io.opendid.web2gateway.common.homechain.HomeChainName;
import io.opendid.web2gateway.core.SpringContextUtil;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainAddConsumerInterface;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRemoveConsumerInterface;

public class HomeChainRemoveConsumerFactory {

    public static HomeChainRemoveConsumerInterface createSubscriptionHandler(){

        HomeChainRemoveConsumerInterface bean = SpringContextUtil.getBean(
                HomeChainType.getHomeChainName()+ HomeChainRemoveConsumerInterface.BEAN_SUFFIX
                , HomeChainRemoveConsumerInterface.class);

        return bean;
    }
}
