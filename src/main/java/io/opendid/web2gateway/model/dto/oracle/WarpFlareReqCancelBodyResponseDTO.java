package io.opendid.web2gateway.model.dto.oracle;

import io.opendid.web2gateway.model.dto.oracle.aptos.Signature;
import io.opendid.web2gateway.model.dto.oracle.aptos.TransactionPayload;
import org.web3j.crypto.RawTransaction;

/**
*  WarpFlareReqCancelBodyResponseDTO
* @author Dong-Jianguo
* @Date: 2024/12/13
* @version 1.0.0
*
* @history date, modifier,and description
**/
public class WarpFlareReqCancelBodyResponseDTO {

  private RawTransaction rawTransaction;

  private Long chainId;

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
