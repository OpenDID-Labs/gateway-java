package io.opendid.web2gateway.oracleweb2process.method;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.JobIdFeeResponseDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateJWTResponseDTO;
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

@Component(Web2MethodName.JWT_ROOT_UPDATE+ Web2Method.BEAN_SUFFIX)
@MethodAdmin
public class MethodAdminUpdateJWT implements Web2Method {

  private Logger logger = LoggerFactory.getLogger(MethodAdminUpdateJWT.class);

  @Autowired
  JwtCreate jwtCreate;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {
    String token = jwtCreate.generateAdminJwt();
    UpdateJWTResponseDTO updateJWTResponseDTO = new UpdateJWTResponseDTO();
    jwtTokenService.updateAdminJwt(token);
    updateJWTResponseDTO.setJwtToken(token);

    return updateJWTResponseDTO;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    return null;
  }
}
