package io.opendid.web2gateway.model.dto.OAuth2;

public class GetTokenResDTO {

  private String accessToken;

  private Integer expiresIn;

  private Integer refreshExpiresIn;

  private String tokenType;

  private String idToken;

  private Integer notBeforePolicy;

  private String scope;


  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  public Integer getRefreshExpiresIn() {
    return refreshExpiresIn;
  }

  public void setRefreshExpiresIn(Integer refreshExpiresIn) {
    this.refreshExpiresIn = refreshExpiresIn;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  public Integer getNotBeforePolicy() {
    return notBeforePolicy;
  }

  public void setNotBeforePolicy(Integer notBeforePolicy) {
    this.notBeforePolicy = notBeforePolicy;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
