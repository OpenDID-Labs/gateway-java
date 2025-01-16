package io.opendid.web2gateway.security.jwt;

import io.opendid.web2gateway.model.dto.jwtclaim.JWTClaimDTO;
import java.util.HashMap;
import java.util.Map;

public class JWTClaims {

  public static Map putClaims(JWTClaimDTO claimDTO){

    Map claims = new HashMap();
    claims.put(JWTClaimDTO.ADMIN, claimDTO.isAdmin());
    claims.put(JWTClaimDTO.SUB_ID,claimDTO.getSubjectId());
//    claims.put(JWTClaimDTO.FIELD1, claimDTO.getField1());
//    claims.put(JWTClaimDTO.FIELD2, claimDTO.getField2());
//    claims.put(JWTClaimDTO.FIELD3, claimDTO.getField3());
    return claims;
  }
}
