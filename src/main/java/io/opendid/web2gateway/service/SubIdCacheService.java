package io.opendid.web2gateway.service;

import io.opendid.web2gateway.common.catches.ChainSignKeyVaultCache;
import io.opendid.web2gateway.common.catches.SubIdCache;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.model.dto.subscription.SubIdCacheDTO;
import io.opendid.web2gateway.repository.mapper.SubscriptionInfoMapper;
import io.opendid.web2gateway.repository.model.SubscriptionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubIdCacheService {

    @Autowired
    private SubscriptionInfoMapper subscriptionInfoMapper;

    public void subIdToCache(){
        List<SubscriptionInfo> subscriptionInfos = subscriptionInfoMapper.selectAllSubIds();
        for (SubscriptionInfo subscriptionInfo : subscriptionInfos) {
            ChainKeyDTO chainKeyDTO =
                    ChainSignKeyVaultCache.getChainKeyByWalletAddress(subscriptionInfo.getOwnerAddress());

            SubIdCacheDTO cacheDTO=new SubIdCacheDTO();
            cacheDTO.setSubId(subscriptionInfo.getSubId());
            cacheDTO.setWalletAddress(subscriptionInfo.getOwnerAddress());
            cacheDTO.setWalletPrivKey(chainKeyDTO.getPrivateKey());
            cacheDTO.setWalletPubKey(chainKeyDTO.getPublicKey());

            SubIdCache.putServiceValue(subscriptionInfo.getSubId(),cacheDTO);
        }
    }

    public void subIdToCache(String subId){
        SubscriptionInfo subscriptionInfo = subscriptionInfoMapper.selectBySubId(subId);
        ChainKeyDTO chainKeyDTO =
                ChainSignKeyVaultCache.getChainKeyByWalletAddress(subscriptionInfo.getOwnerAddress());

        SubIdCacheDTO cacheDTO=new SubIdCacheDTO();
        cacheDTO.setSubId(subscriptionInfo.getSubId());
        cacheDTO.setWalletAddress(subscriptionInfo.getOwnerAddress());
        cacheDTO.setWalletPrivKey(chainKeyDTO.getPrivateKey());
        cacheDTO.setWalletPubKey(chainKeyDTO.getPublicKey());
        SubIdCache.putServiceValue(subscriptionInfo.getSubId(),cacheDTO);
    }
}
