package io.opendid.web2gateway.keytet;

import io.opendid.web2gateway.common.utils.AptosUtils;
import org.junit.jupiter.api.Test;

public class AptosTest {

    @Test
    public void getPubKey() throws Exception {
        String s = AptosUtils.generatePublicKeyFromPrivateKey("0xd67946a885804714166d72e876b2ca27ea4461a4bd85edbcfed687020cd0b641");

        System.out.println(s);
    }
}
