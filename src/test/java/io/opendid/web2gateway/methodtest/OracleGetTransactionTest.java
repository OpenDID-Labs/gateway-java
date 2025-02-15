package io.opendid.web2gateway.methodtest;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.model.dto.oracle.GetTransactionRespDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.method.MethodOracleGetTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

@SpringBootTest
public class OracleGetTransactionTest {

    @Autowired
    private MethodOracleGetTransaction methodOracleGetTransaction;

    @Test
    void getTransaction() throws Exception {
        Long id = 1L;
        String method = "oracle_get_transaction";
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("requestId", "123456789");
        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(id, method, params, "");
        Object process = methodOracleGetTransaction.process(jsonRpc2Request);
        GetTransactionRespDTO getTransactionRespDTO = new GetTransactionRespDTO();
        if (process != null) {
            getTransactionRespDTO = (GetTransactionRespDTO) process;
        }
        System.out.println(JSONObject.toJSONString(getTransactionRespDTO));
    }

}
