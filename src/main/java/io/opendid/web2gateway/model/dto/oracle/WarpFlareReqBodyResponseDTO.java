package io.opendid.web2gateway.model.dto.oracle;

import io.opendid.web2gateway.model.dto.oracle.aptos.Signature;
import io.opendid.web2gateway.model.dto.oracle.aptos.TransactionPayload;
import org.web3j.crypto.RawTransaction;

/**
*  WarpFlareReqBodyResponseDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class WarpFlareReqBodyResponseDTO {

  private RawTransaction rawTransaction;

  private Long chainId;

  private Long jobIdFee;

  private Long claimFee;

  private Boolean jobIdFree;

  private Boolean claimFeeFree;

  public Boolean getJobIdFree() {
    return jobIdFree;
  }

  public void setJobIdFree(Boolean jobIdFree) {
    this.jobIdFree = jobIdFree;
  }

  public Boolean getClaimFeeFree() {
    return claimFeeFree;
  }

  public void setClaimFeeFree(Boolean claimFeeFree) {
    this.claimFeeFree = claimFeeFree;
  }

  public Long getJobIdFee() {
    return jobIdFee;
  }

  public void setJobIdFee(Long jobIdFee) {
    this.jobIdFee = jobIdFee;
  }

  public Long getClaimFee() {
    return claimFee;
  }

  public void setClaimFee(Long claimFee) {
    this.claimFee = claimFee;
  }

  public RawTransaction getRawTransaction() {
    return rawTransaction;
  }

  public void setRawTransaction(RawTransaction rawTransaction) {
    this.rawTransaction = rawTransaction;
  }

  public Long getChainId() {
    return chainId;
  }

  public void setChainId(Long chainId) {
    this.chainId = chainId;
  }
}
