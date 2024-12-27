package io.opendid.web2gateway.methodtest;

import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.method.MethodOracleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;

@SpringBootTest
public class OracleSendSignatureBodyTest {

  @Autowired
  private MethodOracleRequest methodOracleRequest;


  @Test
  public void testOracleRequest() throws Exception, JsonRpc2ServerErrorException {

    LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<>();
    linkedHashMap.put("jobId","9330d9fc54ab48ada8373493b0ef9cf3");
    linkedHashMap.put("data","{\"authApplyRetainData\":\"0446fb7fe1a26b8503af5a291e80f533da3a74cf39ccca041697175f6282ebd0a0f1213ba1f52fc0d8625decaee18d2c557aa40ea49edf7807b8682e1e572e1cf32542773b5964e21ae3d4941422c85a241e3c993e5a44bc1ffea8f46e7c01057ce42db05703f9d7be276d646bf3f4ecb7f69199cdb30c5c6288ebdd1eabd8877551659cf00963d3fe627e96a4f50b81b188945d8b5e3989f7cf8968bc484dee19723255ff0c0324\",\"authHash\":\"D9B1E71175D6AB1FC166F4883BCFFFC1C9BB9456E3E4CCDA5F68C7A75B850974\",\"publicKeyIndex\":1}");

    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        2L,
        "oracle_request",
        linkedHashMap,
        ""
    );

    Object process = methodOracleRequest.process(jsonRpc2Request);

  }




}
