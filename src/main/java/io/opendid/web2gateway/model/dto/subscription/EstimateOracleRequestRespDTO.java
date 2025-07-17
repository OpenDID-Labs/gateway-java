package io.opendid.web2gateway.model.dto.subscription;

import java.math.BigInteger;

public class EstimateOracleRequestRespDTO {

  private BigInteger jobFee;

  public BigInteger getJobFee() {
    return jobFee;
  }

  public void setJobFee(BigInteger jobFee) {
    this.jobFee = jobFee;
  }
}
