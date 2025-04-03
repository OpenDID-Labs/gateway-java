package io.opendid.web2gateway.model.dto.oracle.subscription;


import org.web3j.crypto.RawTransaction;

public class WarpFlareSubCreateBodyRespDTO {

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
