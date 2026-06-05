package io.opendid.web2gateway.model.dto.oracle;

public class OracleRequestV2MetaDataDTO {

    private String jobId;

    private String vnCode;

    private String data;

    private String callbackUrl;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getVnCode() {
        return vnCode;
    }

    public void setVnCode(String vnCode) {
        this.vnCode = vnCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
