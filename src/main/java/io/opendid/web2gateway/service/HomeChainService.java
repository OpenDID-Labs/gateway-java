package io.opendid.web2gateway.service;

import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.config.WalletPrivateKeyConfig;
import io.opendid.web2gateway.model.dto.oracle.GeneratePubKeyAndAddrDTO;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HomeChainService {

  private static Logger logger = LoggerFactory.getLogger(HomeChainService.class);

  /*wallet.privatekey*/
  @Autowired
  private WalletPrivateKeyConfig walletPrivateKeyConfig;

  @Autowired
  private VngatewayRouteInfoService vngatewayRouteInfoService;

  public List<GeneratePubKeyAndAddrDTO> generatePublicKeyAndAddr() throws Exception {

    List<Map<String,String>> walletPrivateKeyList = walletPrivateKeyConfig.getPrivatekey();

    List<VngatewayRouteInfo> vngatewayRouteInfos = vngatewayRouteInfoService.selectAllVn();

    // The number of configured private keys must be consistent with the number of vn
    if (vngatewayRouteInfos.size() != walletPrivateKeyList.size()){
      throw new RuntimeException("In the configuration: wallet.privatekey, " +
          "the number of private keys included must be consistent with the number of Vn." +
          "Vn quantity: "+vngatewayRouteInfos.size());
    }


    List<GeneratePubKeyAndAddrDTO> resultList = new ArrayList<>();

    for (int i = 0; i < walletPrivateKeyList.size(); i++) {

      String privateKeyCode= walletPrivateKeyList.get(i).get(WalletPrivateKeyConfig.KEY_NAME);
      if (StringUtils.isBlank(privateKeyCode)){
        logger.warn(" KeyCode is missing");
        continue;
      }
      String walletPrivateKey = walletPrivateKeyList.get(i).get(WalletPrivateKeyConfig.KEY_VALUE);
      VngatewayRouteInfo vngatewayRouteInfo = vngatewayRouteInfos.get(i);

      String pubKeyStr = AptosUtils.generatePublicKeyFromPrivateKey(walletPrivateKey);
      String address = AptosUtils.generateAddressFromPublicKey(pubKeyStr);

      GeneratePubKeyAndAddrDTO generatePubKeyAndAddrDTO = new GeneratePubKeyAndAddrDTO();
      generatePubKeyAndAddrDTO.setPublicKey(pubKeyStr);
      generatePubKeyAndAddrDTO.setWalletAddress(address);
      generatePubKeyAndAddrDTO.setVnCode(vngatewayRouteInfo.getVnCode());
      generatePubKeyAndAddrDTO.setPrivateKey(walletPrivateKey);
      generatePubKeyAndAddrDTO.setPrivateKeyCode(privateKeyCode);

      resultList.add(generatePubKeyAndAddrDTO);
    }

    return resultList;
  }


}
