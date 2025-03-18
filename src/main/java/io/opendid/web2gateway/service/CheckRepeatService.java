package io.opendid.web2gateway.service;

import io.opendid.web2gateway.common.enums.status.CancelStatusEnum;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckRepeatService {

  private static Map<String, String> cache = new HashMap<>();

  @Autowired
  private OdOracleContractEventlogMapper eventLogMapper;


  public boolean checkRepeatCancel(String requestId) {

    String cache = getCache(requestId);

    if (cache != null && !"".equals(cache)) {
      return false;
    }

    OdOracleContractEventlog contractEventLog = eventLogMapper.selectByRequestId(requestId);

    Integer cancelStatus = contractEventLog.getCancelStatus();

    return cancelStatus == CancelStatusEnum.NOT_CANCELLED.getCode()
        || cancelStatus == CancelStatusEnum.FAIL.getCode();
  }


  public void setCache(String key, String value) {
    cache.put(key, value);
  }

  public String getCache(String key) {
    return cache.get(key);
  }

  public void removeCache(String key) {
    cache.remove(key);
  }


}
