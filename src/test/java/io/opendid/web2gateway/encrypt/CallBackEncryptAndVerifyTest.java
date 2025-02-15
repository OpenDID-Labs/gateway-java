package io.opendid.web2gateway.encrypt;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.utils.ECDSAUtils;
import io.opendid.web2gateway.common.utils.ECIESUtil;
import io.opendid.web2gateway.model.dto.oracle.OracleCallBackResultDTO;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class CallBackEncryptAndVerifyTest {




  @Test
  public void testEncryptAndVerify() throws Exception {

    String privateKey = "";

    String encryptData = "";

    String vnPublicKey = "";  //  Not with 0x

    String decryptData = ECIESUtil.hexDecrypt(privateKey, encryptData);

    OracleCallBackResultDTO oracleCallBackResultDTO =
        JSONObject.parseObject(decryptData, OracleCallBackResultDTO.class);

    String waitSignStr = oracleCallBackResultDTO.getRequestId()
        + oracleCallBackResultDTO.getOracleFulfillTxHash()
        + oracleCallBackResultDTO.getData()
        + oracleCallBackResultDTO.getStatus();

    boolean verify = ECDSAUtils.verify(
        waitSignStr,
        new BigInteger(vnPublicKey,16).toString(),
        oracleCallBackResultDTO.getSignData());


    System.out.println("result="+verify);
  }



}
