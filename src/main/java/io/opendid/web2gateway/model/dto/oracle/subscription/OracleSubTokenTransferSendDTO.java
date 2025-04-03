package io.opendid.web2gateway.model.dto.oracle.subscription;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public class OracleSubTokenTransferSendDTO {

    private OracleSubTokenTransferMetaDataDTO metaData;

    private OracleSubTokenTransferSignatureResDTO signatureResDTO;

    private JsonRpc2Request requestClientBody;

    public OracleSubTokenTransferMetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(
        OracleSubTokenTransferMetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public OracleSubTokenTransferSignatureResDTO getSignatureResDTO() {
        return signatureResDTO;
    }

    public void setSignatureResDTO(
        OracleSubTokenTransferSignatureResDTO signatureResDTO) {
        this.signatureResDTO = signatureResDTO;
    }

    public JsonRpc2Request getRequestClientBody() {
        return requestClientBody;
    }

    public void setRequestClientBody(JsonRpc2Request requestClientBody) {
        this.requestClientBody = requestClientBody;
    }
}
