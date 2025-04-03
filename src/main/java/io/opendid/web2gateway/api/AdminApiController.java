package io.opendid.web2gateway.api;

import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ExceptionObject;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.MethodExecutor;
import io.opendid.web2gateway.security.checkaspect.PrivateApiSecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping()
public class AdminApiController {


  private Logger logger= LoggerFactory.getLogger(AdminApiController.class);



  @PostMapping("v1/admin")
  @PrivateApiSecurityCheck
  public Mono<JsonRpc2Response> oracleAdmin(
          ServerWebExchange exchange,
          @RequestBody String bodyStr) throws Exception {

    try {

      return Mono.just(MethodExecutor.adminMethod(bodyStr));
    }catch (JsonRpc2ExceptionObject e){
      throw e;
    }catch (Exception ex){
      logger.error("error: ",ex);
      throw new JsonRpc2ServerErrorException(
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getCode(),
              MDC.get(LogTraceIdConstant.TRACE_ID),
              JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getMessage(),
              "Server error");
    }

  }


}
