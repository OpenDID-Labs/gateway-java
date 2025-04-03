package io.opendid.web2gateway.model.dto.oracle.subscription;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public class OracleRemoveConsumerSendDTO {

    private OracleRemoveConsumerMetaDataDTO metaData;

    private OracleRemoveConsumerSignatureResDTO signatureResDTO;

    private JsonRpc2Request requestClientBody;

    public OracleRemoveConsumerMetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(OracleRemoveConsumerMetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public OracleRemoveConsumerSignatureResDTO getSignatureResDTO() {
        return signatureResDTO;
    }

    public void setSignatureResDTO(
        OracleRemoveConsumerSignatureResDTO signatureResDTO) {
        this.signatureResDTO = signatureResDTO;
    }

    public JsonRpc2Request getRequestClientBody() {
        return requestClientBody;
    }

    public void setRequestClientBody(JsonRpc2Request requestClientBody) {
        this.requestClientBody = requestClientBody;
    }
}
