package io.opendid.web2gateway;


import io.opendid.web2gateway.security.authorization.AuthorizationBearer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public class HeaderBearerTest {

  @Test
  public void test(){
    String header="Bearer xxxxxx";

    String bearer = AuthorizationBearer.getBearer(header);

    System.out.println(bearer);
  }
}
