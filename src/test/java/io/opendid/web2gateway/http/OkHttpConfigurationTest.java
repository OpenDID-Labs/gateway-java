package io.opendid.web2gateway.http;

import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ssl.SSLHandshakeException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
public class OkHttpConfigurationTest {

  @Resource
  private OkHttpClient okHttpClient;

  @Test
  public void testSuccess() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

    Request request = new Request.Builder()
        .url("https://www.baidu.com")
        .build();

    try (Response response = okHttpClient.newCall(request).execute()) {
      System.out.println("Success: HTTP" + response.code());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  @Test
  public void testFail() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

    String testUrl = "https://self-signed.badssl.com/";

    Request request = new Request.Builder().url(testUrl).build();

    try (Response response = okHttpClient.newCall(request).execute()) {
      System.out.println("Success: HTTP " + response.code());
    } catch (SSLHandshakeException e) {
      System.out.println("SSLHandshakeException");
    } catch (Exception ex) {
      System.out.println("Exception");
    }

  }


}
