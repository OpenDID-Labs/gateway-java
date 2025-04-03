package io.opendid.web2gateway.model.dto.oracle.subscription;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public class OracleSubscriptionSendDTO {
    private OracleSubscriptionMetaDataDTO metaData;

    private OracleSubscriptionSignatureResDTO signatureResDTO;


    private JsonRpc2Request requestClientBody;

    public OracleSubscriptionMetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(OracleSubscriptionMetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public OracleSubscriptionSignatureResDTO getSignatureResDTO() {
        return signatureResDTO;
    }

    public void setSignatureResDTO(OracleSubscriptionSignatureResDTO signatureResDTO) {
        this.signatureResDTO = signatureResDTO;
    }

    public JsonRpc2Request getRequestClientBody() {
        return requestClientBody;
    }

    public void setRequestClientBody(JsonRpc2Request requestClientBody) {
        this.requestClientBody = requestClientBody;
    }
}
