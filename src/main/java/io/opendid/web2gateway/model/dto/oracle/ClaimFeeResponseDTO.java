package io.opendid.web2gateway.model.dto.oracle;

/**
*  JobIdFeeResponseDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class ClaimFeeResponseDTO {

  private Long gasAmount;

  private Boolean free;

  public Long getGasAmount() {
    return gasAmount;
  }

  public void setGasAmount(Long gasAmount) {
    this.gasAmount = gasAmount;
  }

  public Boolean getFree() {
    return free;
  }

  public void setFree(Boolean free) {
    this.free = free;
  }
}
