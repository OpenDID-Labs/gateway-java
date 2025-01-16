package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.GetWalletAddressRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.repository.mapper.GatewayKeyVaultMapper;
import io.opendid.web2gateway.repository.model.GatewayKeyVault;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.ORACLE_GET_WALLET_ADDRESS + Web2Method.BEAN_SUFFIX)
@MethodPrivate
public class MethodOracleGetWalletAddress implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodOracleGetWalletAddress.class);

  @Autowired
  private GatewayKeyVaultMapper gatewayKeyVaultMapper;

  @Override
  public Object process(JsonRpc2Request request) {

    logger.info("MethodOracleGetWalletAddress receive params: {}", JSONObject.toJSONString(request));

    GatewayKeyVault gatewayKeyVault = gatewayKeyVaultMapper.selectKeyInfo();

    GetWalletAddressRespDTO getWalletAddressRespDTO = new GetWalletAddressRespDTO();

    if (getWalletAddressRespDTO != null) {
      getWalletAddressRespDTO.setWalletAddress(gatewayKeyVault.getWalletAddress());
    }

    return getWalletAddressRespDTO;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    return null;
  }
}
