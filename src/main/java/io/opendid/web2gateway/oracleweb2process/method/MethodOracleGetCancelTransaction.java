package io.opendid.web2gateway.oracleweb2process.method;

import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.security.checkaspect.MethodOracle;
import io.opendid.web2gateway.service.OracleMsgRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.ORACLE_GET_CANCEL_TRANSACTION + Web2Method.BEAN_SUFFIX)
@MethodOracle
public class MethodOracleGetCancelTransaction implements Web2Method {


  @Autowired
  private OracleMsgRecordService oracleMsgRecordService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    return oracleMsgRecordService.selectCancelStatusByRequestId(String.valueOf(request.getParams().get("requestId")));
  }

  @Override
  public String checkParams(JsonRpc2Request request) {

    Object jobId = request.getParams().get("requestId");
    if (jobId == null || StringUtils.isBlank(String.valueOf(jobId))) {
      return " requestId is empty";
    }

    return null;
  }
}
