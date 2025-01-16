package io.opendid.web2gateway.oracleweb2process.method;

import io.jsonwebtoken.*;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.utils.PemToKey;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.contextfilter.security.SecurityContext;
import io.opendid.web2gateway.contextfilter.security.SecurityContextHolder;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidParamsException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.admin.TenantJwCancelReqDTO;
import io.opendid.web2gateway.model.dto.admin.TenantJwCancelResDTO;
import io.opendid.web2gateway.model.dto.jwtclaim.JWTClaimDTO;
import io.opendid.web2gateway.model.dto.jwtkey.JWTCatchDTO;
import io.opendid.web2gateway.model.dto.oracle.TenantJwCancelResponseDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodAdmin;
import io.opendid.web2gateway.security.jwt.JWTCatch;
import io.opendid.web2gateway.security.jwt.JWTUtil;
import io.opendid.web2gateway.security.jwt.JwtCreate;
import io.opendid.web2gateway.service.JwtTokenService;
import io.vertx.core.buffer.Buffer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

@Component(Web2MethodName.JWT_TENANT_CANCEL + Web2Method.BEAN_SUFFIX)
@MethodAdmin
public class MethodAdminTenantCancelJWT implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodAdminTenantCancelJWT.class);

  @Autowired
  private JwtCreate jwtCreate;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  JWTCatch jwtCatch;

  @Override
  public Object process(JsonRpc2Request request)  {

    String traceid = MDC.get(LogTraceIdConstant.TRACE_ID);

    TenantJwCancelReqDTO reqDTO=new TenantJwCancelReqDTO();
    String tenantJwtToken = (String) request.getParams().get("jwtToken");

    //call JwtTokenService delete in DB
    JWTCatchDTO jwtCatch1 = jwtCatch.getJwtCatch();
    Jws<Claims> claimsJws = JWTUtil.parseJwtToken(tenantJwtToken, jwtCatch1.getPublicKey());


    boolean admin=(boolean)claimsJws.getPayload().get(JWTClaimDTO.ADMIN);

    if (admin){
      throw  new JsonRpc2InvalidParamsException(traceid,"This interface cannot cancel the jwt of admin");
    }

    String subId = (String)claimsJws.getPayload().get(JWTClaimDTO.SUB_ID);
    reqDTO.setSubId(subId);
    int count = jwtTokenService.deleteTenantJwt(reqDTO);

    if (count==0){
      throw new JsonRpc2InvalidParamsException(traceid,"This jwt does not exist");
    }
    TenantJwCancelResponseDTO tenantJwCancelResponseDTO = new TenantJwCancelResponseDTO();
    tenantJwCancelResponseDTO.setCanceledJwtToken(tenantJwtToken);
    return tenantJwCancelResponseDTO;



  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    Object tenantJwtToken = request.getParams().get("jwtToken");
    if (tenantJwtToken == null || StringUtils.isBlank(String.valueOf(tenantJwtToken))) {
      return " tenantJwtToken is empty";
    }

    return null;
  }
}
