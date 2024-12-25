package io.opendid.web2gateway.model.dto.oracle;

import io.opendid.web2gateway.model.dto.oracle.aptos.Signature;
import io.opendid.web2gateway.model.dto.oracle.aptos.TransactionPayload;

/**
*  WaitSignDataRequestDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class WarpReqBodyResponseDTO {

  private Long jobIdFee;

  private String encodeHash;

  private Signature signature;

  private String sender;

  private String sequenceNumber;

  private String maxGasAmount;

  private String gasUnitPrice;

  private String expirationTimestampSecs;

  private TransactionPayload payload;

  public Long getJobIdFee() {
    return jobIdFee;
  }

  public void setJobIdFee(Long jobIdFee) {
    this.jobIdFee = jobIdFee;
  }

  public String getEncodeHash() {
    return encodeHash;
  }

  public void setEncodeHash(String encodeHash) {
    this.encodeHash = encodeHash;
  }

  public Signature getSignature() {
    return signature;
  }

  public void setSignature(Signature signature) {
    this.signature = signature;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(String sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public String getMaxGasAmount() {
    return maxGasAmount;
  }

  public void setMaxGasAmount(String maxGasAmount) {
    this.maxGasAmount = maxGasAmount;
  }

  public String getGasUnitPrice() {
    return gasUnitPrice;
  }

  public void setGasUnitPrice(String gasUnitPrice) {
    this.gasUnitPrice = gasUnitPrice;
  }

  public String getExpirationTimestampSecs() {
    return expirationTimestampSecs;
  }

  public void setExpirationTimestampSecs(String expirationTimestampSecs) {
    this.expirationTimestampSecs = expirationTimestampSecs;
  }

  public TransactionPayload getPayload() {
    return payload;
  }

  public void setPayload(TransactionPayload payload) {
    this.payload = payload;
  }
}
