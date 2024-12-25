package io.opendid.web2gateway.model.dto.oracle.aptos;

import java.util.Collections;
import java.util.List;

public class TransactionPayload {

  private String type;

  private String function;

  private List<String> typeArguments = Collections.emptyList();

  private List<Object> arguments;

  private Code code;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFunction() {
    return function;
  }

  public void setFunction(String function) {
    this.function = function;
  }

  public List<String> getTypeArguments() {
    return typeArguments;
  }

  public void setTypeArguments(List<String> typeArguments) {
    this.typeArguments = typeArguments;
  }

  public List<Object> getArguments() {
    return arguments;
  }

  public void setArguments(List<Object> arguments) {
    this.arguments = arguments;
  }

  public Code getCode() {
    return code;
  }

  public void setCode(Code code) {
    this.code = code;
  }
}
