package io.opendid.web2gateway.model.dto.oracle;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public class OracleRequestSendMessageDTO {

    private OracleRequestMetaDataDTO metaData;

    private OracleRequestSignatureResDTO signatureResDTO;


    private JsonRpc2Request requestClientBody;
    public OracleRequestMetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(OracleRequestMetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public OracleRequestSignatureResDTO getSignatureResDTO() {
        return signatureResDTO;
    }

    public void setSignatureResDTO(OracleRequestSignatureResDTO signatureResDTO) {
        this.signatureResDTO = signatureResDTO;
    }

    public JsonRpc2Request getRequestClientBody() {
        return requestClientBody;
    }

    public void setRequestClientBody(JsonRpc2Request requestClientBody) {
        this.requestClientBody = requestClientBody;
    }
}
