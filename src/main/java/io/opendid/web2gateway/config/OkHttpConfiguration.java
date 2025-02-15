package io.opendid.web2gateway.config;

import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.*;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OkHttpConfiguration {

  @Value("${ok.http.connect-timeout}")
  private Integer connectTimeout;
  @Value("${ok.http.read-timeout}")
  private Integer readTimeout;
  @Value("${ok.http.write-timeout}")
  private Integer writeTimeout;
  @Value("${ok.http.max-idle-connections}")
  private Integer maxIdleConnections;
  @Value("${ok.http.keep-alive-duration}")
  private Long keepAliveDuration;


  @Bean
  public SSLSocketFactory sslSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    try {
      trustManagerFactory.init((KeyStore) null);
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize TrustManagerFactory", e);
    }

    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
      throw new IllegalStateException("Unexpected default trust managers: " + trustManagers);
    }
    X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagers, null);
    return sslContext.getSocketFactory();
  }
  /**
   * Create a new connection pool with tuning parameters appropriate for a single-user application.
   * The tuning parameters in this pool are subject to change in future OkHttp releases. Currently
   */
  @Bean
  public ConnectionPool pool() {
    return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);
  }
  @Bean
  public OkHttpClient okHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init((KeyStore) null);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

    return new OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory(), trustManager) // Use authentic textbooks
        .retryOnConnectionFailure(false)
        .connectionPool(pool())
        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .writeTimeout(writeTimeout,TimeUnit.SECONDS)
        .hostnameVerifier((hostname, session) -> true)
        .build();
  }


}
