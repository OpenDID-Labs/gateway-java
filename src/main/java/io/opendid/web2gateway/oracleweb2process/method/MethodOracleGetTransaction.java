package io.opendid.web2gateway.oracleweb2process.method;

import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.oracleweb2process.Web2MethodProcess;
import io.opendid.web2gateway.service.OracleMsgRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.ORACLE_GET_TRANSACTION + Web2MethodProcess.BEAN_SUFFIX)
public class MethodOracleGetTransaction implements Web2MethodProcess {


  @Autowired
  private OracleMsgRecordService oracleMsgRecordService;

  @Override
  public Object process(JsonRpc2Request request) {

    return oracleMsgRecordService.selectMsgRecordByRequestId(String.valueOf(request.getParams().get("requestId")));
  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    return null;
  }
}
