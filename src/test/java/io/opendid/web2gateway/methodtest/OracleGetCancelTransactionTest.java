package io.opendid.web2gateway.methodtest;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.method.MethodOracleGetCancelTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

@SpringBootTest
public class OracleGetCancelTransactionTest {

    @Autowired
    private MethodOracleGetCancelTransaction methodOracleGetCancelTransaction;

    @Test
    void getCancelTransaction() throws JsonRpc2ServerErrorException, Exception {
        Long id = 1L;
        String method = "oracle_get_cancel_transaction";
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("requestId", "0x6088b359e267dc9a0f8ba3c5288c83a06dbfb180739ed9ccbe8351197ea1034b");
        JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(id, method, params, "");
        Object process = methodOracleGetCancelTransaction.process(jsonRpc2Request);
        System.out.println(JSONObject.toJSONString(process));
    }
}
