package io.opendid.web2gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("io.opendid.web2gateway.repository.mapper")
@SpringBootApplication(scanBasePackages = {"io.opendid.web2gateway"})
@EnableDiscoveryClient
public class Web2GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(Web2GatewayApplication.class, args);
  }

}
