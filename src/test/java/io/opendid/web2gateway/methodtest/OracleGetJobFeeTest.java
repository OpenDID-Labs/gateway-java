package io.opendid.web2gateway.methodtest;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.method.MethodOracleGetJobFee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

@SpringBootTest
public class OracleGetJobFeeTest {

    @Autowired
    private MethodOracleGetJobFee methodOracleGetJobFee;

    @Test
    void getJobFee() throws JsonRpc2ServerErrorException {
        Long id = 1L;
        String method = "oracle_get_job_fee";
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("jobId", "1028e09602ca4bdd89a6bba67212001f");
        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(id, method, params, "");
        Object process = methodOracleGetJobFee.process(jsonRpc2Request);
        System.out.println(JSONObject.toJSONString(process));
    }

}
