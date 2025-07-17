package io.opendid.web2gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

public class JasyptDecryptEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

  private static final Logger logger = LoggerFactory.getLogger(JasyptDecryptEnvironmentPostProcessor.class);

  private static final String nacosConfigPasswordKey = "spring.cloud.nacos.config.password";
  private static final String nacosDiscoveryPasswordKey = "spring.cloud.nacos.discovery.password";
  private static final String jasyptEncryptorPasswordKey = "jasypt.encryptor.password";
  private static final String jasyptEncryptorAlgorithmKey = "jasypt.encryptor.algorithm";
  private static final String jasyptEncryptorKeyObtentionIterationsKey = "jasypt.encryptor.key.obtention.iterations";
  private static final String jasyptEncryptorPropertyPrefixKey = "ENC(";

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

    try {

      String jasyptPassword = environment.getProperty(jasyptEncryptorPasswordKey);

      if (StringUtils.isNotBlank(jasyptPassword)){

//        String nacosConfigPasswordEncryption = environment.getProperty(nacosConfigPasswordKey);
        String nacosDiscoveryPasswordEncryption = environment.getProperty(nacosDiscoveryPasswordKey);

//        String nacosConfigPasswordDecryption = jasypt(environment,nacosDiscoveryPasswordEncryption);
        String nacosPasswordDecryption = jasypt(environment,nacosDiscoveryPasswordEncryption);

        System.setProperty(nacosConfigPasswordKey, nacosPasswordDecryption);
        System.setProperty(nacosDiscoveryPasswordKey, nacosPasswordDecryption);
      }
    }catch (Exception ex){
      ex.printStackTrace();
      logger.error("postProcessEnvironment decryption jasypt error!");
      throw ex;
    }

  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }


  private String jasypt(ConfigurableEnvironment environment, String encryptedText) {

    if (StringUtils.isNotBlank(encryptedText) && encryptedText.startsWith(jasyptEncryptorPropertyPrefixKey)) {
      encryptedText = encryptedText.substring(jasyptEncryptorPropertyPrefixKey.length(),encryptedText.length()-1);
    }

    String jasyptPassword = environment.getProperty(jasyptEncryptorPasswordKey);
    String jasyptAlgorithm = environment.getProperty(jasyptEncryptorAlgorithmKey);
    Integer jasyptEncryptorKeyObtentionIterations = environment.getProperty(jasyptEncryptorKeyObtentionIterationsKey,Integer.class);

    // Creating an Encryptor
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    // Setting encryption parameters
    encryptor.setPassword(jasyptPassword);
    encryptor.setAlgorithm(jasyptAlgorithm);
    encryptor.setKeyObtentionIterations(jasyptEncryptorKeyObtentionIterations);
    encryptor.setIvGenerator(new RandomIvGenerator());
    encryptor.setSaltGenerator(new RandomSaltGenerator());

    // Optional: Verify decryption
    String decryptedText = encryptor.decrypt(encryptedText);

    return decryptedText;
  }


}
