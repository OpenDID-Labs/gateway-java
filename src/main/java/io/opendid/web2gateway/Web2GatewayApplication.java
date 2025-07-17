package io.opendid.web2gateway;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;

@EnableScheduling
@MapperScan("io.opendid.web2gateway.repository.mapper")
@SpringBootApplication(scanBasePackages = {"io.opendid.web2gateway"})
@EnableDiscoveryClient
@EnableEncryptableProperties
public class Web2GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(Web2GatewayApplication.class, args);
  }

}
