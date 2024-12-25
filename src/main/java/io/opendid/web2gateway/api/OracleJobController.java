package io.opendid.web2gateway.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidParamsException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2InvalidRequestException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ParseErrorException;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.Web2MethodProcess;
import io.opendid.web2gateway.oracleweb2process.Web2MethodProcessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/oracle")
public class OracleJobController {


  private Logger logger= LoggerFactory.getLogger(OracleJobController.class);

  @PostMapping("send")
  public JsonRpc2Response oracleJobWeb2Api(
      @RequestBody String bodyStr) throws JsonRpc2ServerErrorException, Exception {

    try {

      String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);

      JsonRpc2Request body = checkJsonString(bodyStr);
      Web2MethodProcess methodProcess = Web2MethodProcessFactory.getMethodProcess(body.getMethod());
      checkParams(body, methodProcess, traceId);

      Object processResult = methodProcess.process(body);
      JsonRpc2Response jsonRpc2Response = new
          JsonRpc2Response(body.getId(),
          processResult
      );

      return jsonRpc2Response;
    }catch (Exception ex){
      logger.error("error: ",ex);
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getCode(),
          "",
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32000.getMessage(),
          ex.getMessage());
    }

  }

  private JsonRpc2Request checkJsonString(String bodystr) {
    String traceId = MDC.get(LogTraceIdConstant.TRACE_ID);
    try {
      JsonRpc2Request jsonRpc2Request = JSON.parseObject(bodystr, JsonRpc2Request.class);
      if (jsonRpc2Request.checkJsonInvalid()){
        throw new JsonRpc2InvalidRequestException(traceId,"invalid jsonrpc2.0 body");
      }
      return jsonRpc2Request;
    }catch (JsonRpc2InvalidRequestException eee){
      throw  eee;
    }catch (JSONException e){
      logger.error("error",e);
      throw  new JsonRpc2ParseErrorException(traceId,
          e.getMessage(),"");
    }

  }

  private void checkParams(JsonRpc2Request body, Web2MethodProcess methodProcess,
      String traceId) {
    String checkParams = methodProcess.checkParams(body);
    if (checkParams!=null){
      throw new JsonRpc2InvalidParamsException(
          traceId,"invalid params",checkParams
      );
    }
  }

}
