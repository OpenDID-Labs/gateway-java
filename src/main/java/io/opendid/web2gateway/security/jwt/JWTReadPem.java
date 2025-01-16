package io.opendid.web2gateway.security.jwt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JWTReadPem {

  public static  String readPem(String pemFileFullPath) throws IOException {
    String pemContents = Files.readString(Path.of(
        pemFileFullPath));
    return pemContents;
  }
}
