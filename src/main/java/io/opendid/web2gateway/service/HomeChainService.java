package io.opendid.web2gateway.service;

import io.opendid.web2gateway.common.utils.AptosUtils;
import io.opendid.web2gateway.model.dto.oracle.GeneratePubKeyAndAddrDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HomeChainService {


  @Value("${wallet.privatekey}")
  private String walletPrivateKey;

  public GeneratePubKeyAndAddrDTO generatePublicKeyAndAddr() throws Exception {

    String pubKeyStr = AptosUtils.generatePublicKeyFromPrivateKey(walletPrivateKey);

    String address = AptosUtils.generateAddressFromPublicKey(pubKeyStr);

    GeneratePubKeyAndAddrDTO generatePubKeyAndAddrDTO = new GeneratePubKeyAndAddrDTO();
    generatePubKeyAndAddrDTO.setPublicKey(pubKeyStr);
    generatePubKeyAndAddrDTO.setWalletAddress(address);

    return generatePubKeyAndAddrDTO;
  }


}
