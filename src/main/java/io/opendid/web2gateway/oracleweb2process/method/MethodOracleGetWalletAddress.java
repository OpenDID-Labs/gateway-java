package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.GetWalletAddressRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.repository.mapper.GatewayKeyVaultMapper;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.repository.model.GatewayKeyVault;
import io.opendid.web2gateway.security.checkaspect.MethodOracle;
import io.opendid.web2gateway.service.HomeChainKeyManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(Web2MethodName.ORACLE_GET_WALLET_ADDRESS + Web2Method.BEAN_SUFFIX)
@MethodOracle
public class MethodOracleGetWalletAddress implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodOracleGetWalletAddress.class);

  @Autowired
  private GatewayKeyVaultMapper gatewayKeyVaultMapper;
  @Autowired
  private HomeChainKeyManageService homeChainKeyManageService;

  @Override
  public Object process(JsonRpc2Request request) {

    logger.info("MethodOracleGetWalletAddress receive params: {}", JSONObject.toJSONString(request));

    GatewayKeyVault gatewayKeyVault = gatewayKeyVaultMapper.selectKeyInfo();

    List<GatewayHomechainKeyManage> gatewayHomechainKeyManages = homeChainKeyManageService.selectAll();


    List<GetWalletAddressRespDTO> resultList = new ArrayList<>();

    for (GatewayHomechainKeyManage gatewayHomechainKeyManage : gatewayHomechainKeyManages) {
      GetWalletAddressRespDTO getWalletAddressRespDTO = new GetWalletAddressRespDTO();
      getWalletAddressRespDTO.setWalletAddress(gatewayHomechainKeyManage.getWalletAddress());
      getWalletAddressRespDTO.setWalletPublicKey(gatewayHomechainKeyManage.getWalletPublicKey());
//      getWalletAddressRespDTO.setVnCode(gatewayHomechainKeyManage.getVnCode());

      resultList.add(getWalletAddressRespDTO);
    }

    return resultList;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    return null;
  }
}
