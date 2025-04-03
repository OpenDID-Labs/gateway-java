package io.opendid.web2gateway.common.catches;

import io.opendid.web2gateway.model.dto.subscription.SubIdCacheDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SubIdCache {
    private static Map<String, SubIdCacheDTO> subIdCache = new ConcurrentHashMap<>();

    public static SubIdCacheDTO getServiceKey(String subId){
        return subIdCache.get(subId);
    }

    public static void putServiceValue(String subId,SubIdCacheDTO catcheDTO){
        subIdCache.put(subId,catcheDTO);
    }

}
