package io.opendid.web2gateway.model.dto.oracle.subscription;

import org.web3j.crypto.RawTransaction;

public class OracleSubTokenTransferSignatureDTO {

    private  String vnWalletPrivateKey;

    private RawTransaction rawTransaction;

    private Long chainId;

    public String getVnWalletPrivateKey() {
        return vnWalletPrivateKey;
    }

    public void setVnWalletPrivateKey(String vnWalletPrivateKey) {
        this.vnWalletPrivateKey = vnWalletPrivateKey;
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
