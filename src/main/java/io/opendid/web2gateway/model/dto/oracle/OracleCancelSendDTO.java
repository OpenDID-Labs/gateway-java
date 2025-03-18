package io.opendid.web2gateway.model.dto.oracle;

import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;

public class OracleCancelSendDTO {

    private OracleRequestCancelMetaDataDTO metaData;

    private OracleCancelSignatureResDTO signatureResDTO;


    private JsonRpc2Request requestClientBody;





    public JsonRpc2Request getRequestClientBody() {
        return requestClientBody;
    }

    public void setRequestClientBody(JsonRpc2Request requestClientBody) {
        this.requestClientBody = requestClientBody;
    }

    public OracleCancelSignatureResDTO getSignatureResDTO() {
        return signatureResDTO;
    }

    public void setSignatureResDTO(OracleCancelSignatureResDTO signatureResDTO) {
        this.signatureResDTO = signatureResDTO;
    }

    public OracleRequestCancelMetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(OracleRequestCancelMetaDataDTO metaData) {
        this.metaData = metaData;
    }
}
