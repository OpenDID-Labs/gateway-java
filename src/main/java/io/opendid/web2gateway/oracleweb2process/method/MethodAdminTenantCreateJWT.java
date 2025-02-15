package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.contextfilter.security.SecurityContext;
import io.opendid.web2gateway.contextfilter.security.SecurityContextHolder;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.admin.TenantJwCreateReqDTO;
import io.opendid.web2gateway.model.dto.admin.TenantJwtGenerateResDTO;
import io.opendid.web2gateway.model.dto.oracle.JobIdFeeResponseDTO;
import io.opendid.web2gateway.model.dto.oracle.TenantJwtGenerateResponseDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.security.checkaspect.MethodAdmin;
import io.opendid.web2gateway.security.checkaspect.MethodPrivate;
import io.opendid.web2gateway.security.jwt.JwtCreate;
import io.opendid.web2gateway.service.JwtTokenService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.JWT_TENANT_CREATE+ Web2Method.BEAN_SUFFIX)
@MethodAdmin
public class MethodAdminTenantCreateJWT implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodAdminTenantCreateJWT.class);

  @Autowired
  JwtCreate jwtCreate;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    TenantJwtGenerateResDTO tenantJwtGenerateResDTO = null;
    Object exp = request.getParams().get("exp");
    if (exp!=null){
      Long exp1 = Long.valueOf (String.valueOf(exp));
      tenantJwtGenerateResDTO = jwtCreate.generateTenantJwt(exp1);
    }else {
      tenantJwtGenerateResDTO = jwtCreate.generateTenantJwt();
    }
    //call JwtTokenService save in DB
    TenantJwCreateReqDTO tenantJwCreateReqDTO = new TenantJwCreateReqDTO();
    tenantJwCreateReqDTO.setSubject((String) request.getParams().get("subjectName"));
    tenantJwCreateReqDTO.setJwt(tenantJwtGenerateResDTO.getJwtToken());
    tenantJwCreateReqDTO.setSubjectId(tenantJwtGenerateResDTO.getSubId());
    tenantJwCreateReqDTO.setExpTime(tenantJwtGenerateResDTO.getExpTime());
    jwtTokenService.insertTenantJwt(tenantJwCreateReqDTO);
    TenantJwtGenerateResponseDTO tenantJwtGenerateResponseDTO = new TenantJwtGenerateResponseDTO();
    tenantJwtGenerateResponseDTO.setJwtToken(tenantJwtGenerateResDTO.getJwtToken());
    return tenantJwtGenerateResponseDTO;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    Object subjectName = request.getParams().get("subjectName");
    if (subjectName == null || StringUtils.isBlank(String.valueOf(subjectName))) {
      return " subjectName is empty";
    }
    Object exp = request.getParams().get("exp");
    if (exp!=null&&Long.parseLong(String.valueOf(exp))<=0){
      return "exp must be greater than 0";
    }
    return null;
  }
}
