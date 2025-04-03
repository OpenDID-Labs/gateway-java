package io.opendid.web2gateway.config.vnmapping;

import com.alibaba.fastjson.JSON;
import io.opendid.web2gateway.common.catches.ChainSignKeyVaultCache;
import io.opendid.web2gateway.common.utils.ECDSAUtils;
import io.opendid.web2gateway.common.catches.*;
import io.opendid.web2gateway.model.dto.chainkey.ChainKeyDTO;
import io.opendid.web2gateway.oraclebodyhandler.factory.HomeChainRequestBodyFactory;
import io.opendid.web2gateway.oraclebodyhandler.interfaces.HomeChainRequestBodyInterface;
import io.opendid.web2gateway.repository.model.GatewayHomechainKeyManage;
import io.opendid.web2gateway.security.jwt.JWTKeyLoader;
import io.opendid.web2gateway.model.dto.oracle.GeneratePubKeyAndAddrDTO;
import io.opendid.web2gateway.model.vnmapping.JobidMappin;
import io.opendid.web2gateway.model.vnmapping.VnMapping;
import io.opendid.web2gateway.repository.model.GatewayKeyVault;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import java.util.Date;
import java.util.List;

import io.opendid.web2gateway.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class InitializationData implements ApplicationListener<ApplicationStartedEvent> {

  private static Logger logger = LoggerFactory.getLogger(InitializationData.class);

  @Autowired
  private VnMappingConfig vnMappingConfig;
  @Autowired
  private VngatewayRouteInfoService vngatewayRouteInfoService;
  @Autowired
  private VngatewayJobidMappingService vngatewayJobidMappingService;
  @Autowired
  private HomeChainService homeChainService;
  @Autowired
  private GatewayKeyVaultService gatewayKeyVaultService;
  @Autowired
  private HomeChainKeyManageService homeChainKeyManageService;
  @Autowired
  private SubIdCacheService subIdCacheService;

  @Autowired
  public JWTKeyLoader jwtKeyLoader;

  @Value("${service-key.privatekey}")
  private String servicePrivateKey;



  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {

    initializationVnMapping();

    initializationGatewayPublicKey();

    initializationRootJWTToken();

    initializationSubIdInfo();
  }

  private void initializationRootJWTToken() {
    jwtKeyLoader.load();
  }


  private void initializationGatewayPublicKey(){

    try {

      GatewayKeyVault gatewayKeyVaultOld = gatewayKeyVaultService.selectCurrentKeyVault();

      if (gatewayKeyVaultOld != null){
        gatewayKeyVaultService.deleteByKey(gatewayKeyVaultOld.getKeyId());
      }

      String servicePubKey = ECDSAUtils.getHexPubKey(servicePrivateKey);

      // Get homeChain process class
      HomeChainRequestBodyInterface homeChainRequestHandler = HomeChainRequestBodyFactory.createRequestBodyHandler();

      // Generate public key and address
      List<GeneratePubKeyAndAddrDTO> generatePubKeyAndAddrDTOS = homeChainRequestHandler.generatePublicKeyAndAddr();

      // Insert GatewayKeyVault
      GatewayKeyVault gatewayKeyVault = new GatewayKeyVault();
      gatewayKeyVault.setServicePublicKey(servicePubKey);
      if (gatewayKeyVaultOld != null){
        gatewayKeyVault.setAdminJwt(gatewayKeyVaultOld.getAdminJwt());
      }
      gatewayKeyVault.setUpdateDate(new Date());

      gatewayKeyVaultService.insertKeyVault(gatewayKeyVault);

      // Delete Delete existing homechain key data
      homeChainKeyManageService.deleteAll();
      // Insert gateway_homechain_key_manage
      for (GeneratePubKeyAndAddrDTO generatePubKeyAndAddrDTO : generatePubKeyAndAddrDTOS) {
        GatewayHomechainKeyManage gatewayHomechainKeyManage = new GatewayHomechainKeyManage();
        gatewayHomechainKeyManage.setVnCode(generatePubKeyAndAddrDTO.getVnCode());
        gatewayHomechainKeyManage.setWalletPublicKey(generatePubKeyAndAddrDTO.getPublicKey());
        gatewayHomechainKeyManage.setWalletAddress(generatePubKeyAndAddrDTO.getWalletAddress());
        gatewayHomechainKeyManage.setUpdateDate(new Date());
        gatewayHomechainKeyManage.setKeyCode(generatePubKeyAndAddrDTO.getPrivateKeyCode());

//        GatewayKeyVaultUtil.putValue(generatePubKeyAndAddrDTO.getVnCode() + GatewayKeyVaultUtil.walletPrivateKey,
//            generatePubKeyAndAddrDTO.getPrivateKey());

        ChainKeyDTO chainKeyDTO = new ChainKeyDTO();
        chainKeyDTO.setWalletAddress(generatePubKeyAndAddrDTO.getWalletAddress());
        chainKeyDTO.setVnCode(generatePubKeyAndAddrDTO.getVnCode());
        chainKeyDTO.setKeyCode(generatePubKeyAndAddrDTO.getPrivateKeyCode());
        chainKeyDTO.setPrivateKey(generatePubKeyAndAddrDTO.getPrivateKey());
        chainKeyDTO.setPublicKey(generatePubKeyAndAddrDTO.getPublicKey());
        ChainSignKeyVaultCache.putChainValueByKeyCode(chainKeyDTO);
        ChainSignKeyVaultCache.putChainValueByVnCode(chainKeyDTO);
        ChainSignKeyVaultCache.putChainValueByWalletAddress(chainKeyDTO);

        homeChainKeyManageService.insertHomeChainKey(gatewayHomechainKeyManage);
      }

      ServiceKeyVaultCache.putServiceValue(ServiceKeyVaultCache.servicePublicKey,servicePubKey);

    } catch (Exception exception) {
      logger.error("GeneratePublicKey error:",exception);
      throw new RuntimeException("GeneratePublicKey error");
    }

  }


  private void initializationVnMapping(){

    List<VnMapping> vninfo = vnMappingConfig.getVninfo();

    if (!vninfo.isEmpty()) {

      vngatewayRouteInfoService.deleteAll();
      vngatewayJobidMappingService.deleteAll();
      logger.info("VN Route SAVE Starting " );

      for (VnMapping vnMapping : vninfo) {

        initializeVnRoute(vnMapping);

        List<JobidMappin> jobidmappings = vnMapping.getJobidmapping();

        if (!jobidmappings.isEmpty()) {

          for (JobidMappin jobidmapping : jobidmappings) {

            initializeJobIdMapping(jobidmapping, vnMapping.getVncode());

          }

        }

      }

    } else {
      logger.warn("VN Route VnMappingInitialization vnInfo is null");
    }
  }

  private void initializeJobIdMapping(JobidMappin jobidmapping, String vnCode) {

    VngatewayJobidMapping vngatewayJobidMapping = new VngatewayJobidMapping();

    vngatewayJobidMapping.setJobId(jobidmapping.getJobid());
    vngatewayJobidMapping.setVnCode(vnCode);
    vngatewayJobidMapping.setPlatformCode(jobidmapping.getPlatform());
    vngatewayJobidMapping.setUpdateDate(new Date());

    vngatewayJobidMappingService.insertJobIdMapping(vngatewayJobidMapping);
    logger.info("VN Route JobMapping SAVE: " + JSON.toJSONString(vngatewayJobidMapping,
        true));

  }


  private void initializeVnRoute(VnMapping vnMapping) {

    VngatewayRouteInfo vngatewayRouteInfo = new VngatewayRouteInfo();

    vngatewayRouteInfo.setVnCode(vnMapping.getVncode());
    vngatewayRouteInfo.setUrl(vnMapping.getVnurl());
    vngatewayRouteInfo.setUpdateDate(new Date());

    vngatewayRouteInfoService.insertRouteInfo(vngatewayRouteInfo);
    logger.info("VN Route SAVE: " + JSON.toJSONString(vngatewayRouteInfo,true));


  }

  private void  initializationSubIdInfo(){
    subIdCacheService.subIdToCache();
  }

}
