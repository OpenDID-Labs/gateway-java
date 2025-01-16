package io.opendid.web2gateway.security.authorization;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationBearer {

  static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$", Pattern.CASE_INSENSITIVE);

  public static String getBearer(String authorization) {
    Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = authorizationPattern.matcher(authorization);
    if(matcher.find()){
      return matcher.group("token");
    }
    return null;
  }
}
