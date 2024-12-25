package io.opendid.web2gateway.model.dto.oracle.aptos;

public class Code {

  private String bytecode;

  private MoveFunction abi;

  public String getBytecode() {
    return bytecode;
  }

  public void setBytecode(String bytecode) {
    this.bytecode = bytecode;
  }

  public MoveFunction getAbi() {
    return abi;
  }

  public void setAbi(MoveFunction abi) {
    this.abi = abi;
  }
}
