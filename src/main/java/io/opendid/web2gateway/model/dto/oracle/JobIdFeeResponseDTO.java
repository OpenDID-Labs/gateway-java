package io.opendid.web2gateway.model.dto.oracle;

/**
*  JobIdFeeResponseDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class JobIdFeeResponseDTO {

  private Long jobFee;

  private Long claimFee;

  public Long getClaimFee() {
    return claimFee;
  }

  public void setClaimFee(Long claimFee) {
    this.claimFee = claimFee;
  }

  public Long getJobFee() {
    return jobFee;
  }

  public void setJobFee(Long jobFee) {
    this.jobFee = jobFee;
  }
}
