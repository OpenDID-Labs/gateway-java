package io.opendid.web2gateway.config.vnmapping;

import io.opendid.web2gateway.config.yml.YmlLoaderFactory;
import io.opendid.web2gateway.model.vnmapping.VnMapping;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@PropertySource(
    value = "file:${vn-mapping-file-path}",
    encoding = "UTF-8",
    factory = YmlLoaderFactory.class)
@ConfigurationProperties(prefix = "vn-mapping")
@Component
public class VnMappingConfig {

  private List<VnMapping> vninfo;

  public List<VnMapping> getVninfo() {
    return vninfo;
  }

  public void setVninfo(List<VnMapping> vninfo) {
    this.vninfo = vninfo;
  }
}
