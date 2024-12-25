package io.opendid.web2gateway.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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
  public X509TrustManager x509TrustManager(){
    return new X509TrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
      }
      @Override
      public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
      }
      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    };

  }
  @Bean
  public SSLSocketFactory sslSocketFactory() {
    try {
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
      return sslContext.getSocketFactory();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }
    return null;
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
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory(), x509TrustManager())
        .retryOnConnectionFailure(false)
        .connectionPool(pool())
        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .writeTimeout(writeTimeout,TimeUnit.SECONDS)
        .hostnameVerifier((hostname, session) -> true)
        .build();
  }


}
