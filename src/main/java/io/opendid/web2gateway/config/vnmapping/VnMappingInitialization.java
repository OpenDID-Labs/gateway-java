package io.opendid.web2gateway.config.vnmapping;

import io.opendid.web2gateway.model.vnmapping.JobidMappin;
import io.opendid.web2gateway.model.vnmapping.VnMapping;
import io.opendid.web2gateway.repository.mapper.VngatewayJobidMappingMapper;
import io.opendid.web2gateway.repository.mapper.VngatewayRouteInfoMapper;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;
import io.opendid.web2gateway.repository.model.VngatewayRouteInfo;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class VnMappingInitialization implements ApplicationListener<ApplicationStartedEvent> {

  private static Logger logger = LoggerFactory.getLogger(VnMappingInitialization.class);

  @Autowired
  private VnMappingConfig vnMappingConfig;
  @Autowired
  private VngatewayRouteInfoMapper vngatewayRouteInfoMapper;
  @Autowired
  private VngatewayJobidMappingMapper vngatewayJobidMappingMapper;


  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {

    List<VnMapping> vninfo = vnMappingConfig.getVninfo();

    if (!vninfo.isEmpty()) {

      vngatewayRouteInfoMapper.deleteAll();
      vngatewayJobidMappingMapper.deleteAll();

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
      logger.warn("VnMappingInitialization vnInfo is null");
    }

  }

  private void initializeJobIdMapping(JobidMappin jobidmapping, String vnCode) {

    VngatewayJobidMapping vngatewayJobidMapping = new VngatewayJobidMapping();

    vngatewayJobidMapping.setJobId(jobidmapping.getJobid());
    vngatewayJobidMapping.setVnCode(vnCode);
    vngatewayJobidMapping.setPlatformCode(jobidmapping.getPlatform());
    vngatewayJobidMapping.setUpdateDate(new Date());

    vngatewayJobidMappingMapper.insertSelective(vngatewayJobidMapping);

  }


  private void initializeVnRoute(VnMapping vnMapping) {

    VngatewayRouteInfo vngatewayRouteInfo = new VngatewayRouteInfo();

    vngatewayRouteInfo.setVnCode(vnMapping.getVncode());
    vngatewayRouteInfo.setUrl(vnMapping.getVnurl());
    vngatewayRouteInfo.setUpdateDate(new Date());

    vngatewayRouteInfoMapper.insertSelective(vngatewayRouteInfo);


  }

}
