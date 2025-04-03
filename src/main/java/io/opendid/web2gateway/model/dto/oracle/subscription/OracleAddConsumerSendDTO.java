package io.opendid.web2gateway.model.dto.oracle.subscription;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public class OracleAddConsumerSendDTO {
    private OracleAddConsumerMetaDataDTO metaData;

    private OracleAddConsumerSignatureResDTO signatureResDTO;

    private JsonRpc2Request requestClientBody;

    public OracleAddConsumerMetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(OracleAddConsumerMetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public OracleAddConsumerSignatureResDTO getSignatureResDTO() {
        return signatureResDTO;
    }

    public void setSignatureResDTO(
        OracleAddConsumerSignatureResDTO signatureResDTO) {
        this.signatureResDTO = signatureResDTO;
    }

    public JsonRpc2Request getRequestClientBody() {
        return requestClientBody;
    }

    public void setRequestClientBody(JsonRpc2Request requestClientBody) {
        this.requestClientBody = requestClientBody;
    }
}
