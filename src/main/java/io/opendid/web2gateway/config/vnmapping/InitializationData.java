package io.opendid.web2gateway.config.vnmapping;

import com.alibaba.fastjson.JSON;
import io.opendid.web2gateway.common.utils.ECDSAUtils;
import io.opendid.web2gateway.common.utils.GatewayKeyVaultUtil;
import io.opendid.web2gateway.security.jwt.JWTKeyLoader;
import io.opendid.web2gateway.model.dto.oracle.GeneratePubKeyAndAddrDTO;
import io.opendid.web2gateway.model.vnmapping.JobidMappin;
import io.opendid.web2gateway.model.vnmapping.VnMapping;
import io.opendid.web2gateway.repository.mapper.GatewayKeyVaultMapper;
import io.opendid.web2gateway.repository.mapper.VngatewayJobidMappingMapper;
import io.opendid.web2gateway.repository.mapper.VngatewayRouteInfoMapper;
import io.opendid.web2gateway.repository.model.GatewayKeyVault;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import java.util.Date;
import java.util.List;

import io.opendid.web2gateway.service.GatewayKeyVaultService;
import io.opendid.web2gateway.service.HomeChainService;
import io.opendid.web2gateway.service.VngatewayJobidMappingService;
import io.opendid.web2gateway.service.VngatewayRouteInfoService;
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
  public JWTKeyLoader jwtKeyLoader;

  @Value("${service-key.privatekey}")
  private String servicePrivateKey;



  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {

    initializationVnMapping();

    initializationGatewayPublicKey();

    initializationRootJWTToken();
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

      GeneratePubKeyAndAddrDTO generatePubKeyAndAddrDTO = homeChainService.generatePublicKeyAndAddr();

      GatewayKeyVault gatewayKeyVault = new GatewayKeyVault();
      gatewayKeyVault.setWalletPublicKey(generatePubKeyAndAddrDTO.getPublicKey());
      gatewayKeyVault.setWalletAddress(generatePubKeyAndAddrDTO.getWalletAddress());
      gatewayKeyVault.setServicePublicKey(servicePubKey);
      if (gatewayKeyVaultOld != null){
        gatewayKeyVault.setAdminJwt(gatewayKeyVaultOld.getAdminJwt());
      }


      gatewayKeyVault.setUpdateDate(new Date());

      gatewayKeyVaultService.insertKeyVault(gatewayKeyVault);

      GatewayKeyVaultUtil.putValue(GatewayKeyVaultUtil.servicePublicKey,servicePubKey);
      GatewayKeyVaultUtil.putValue(GatewayKeyVaultUtil.walletPublicKey,generatePubKeyAndAddrDTO.getPublicKey());
      GatewayKeyVaultUtil.putValue(GatewayKeyVaultUtil.walletAddress,generatePubKeyAndAddrDTO.getWalletAddress());

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

}
