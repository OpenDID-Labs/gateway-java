package io.opendid.web2gateway.oracleweb2process.method;

import io.opendid.web2gateway.common.web2.Web2MethodName;
import io.opendid.web2gateway.model.dto.oracle.subscription.OracleRequestBillMethodResDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.security.checkaspect.MethodSubscription;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(Web2MethodName.ORACLE_REQUEST_BILL + Web2Method.BEAN_SUFFIX)
@MethodSubscription
public class MethodSubOracleRequestBill implements Web2SubscriptionMethod {

  private Logger logger = LoggerFactory.getLogger(MethodSubOracleRequestBill.class);

  @Autowired
  private OracleContractEventLogService eventLogService;

  @Override
  public Object process(JsonRpc2Request request) throws Exception {

    String requestId = request.getParams().get("requestId").toString();

    OdOracleContractEventlog contractEventLog = eventLogService.selectByRequestId(requestId);

    if(contractEventLog != null){

      OracleRequestBillMethodResDTO resDTO = new OracleRequestBillMethodResDTO();
      resDTO.setPayFee(contractEventLog.getUserPayFee());

      return resDTO;
    }

    return null;
  }

  @Override
  public String checkParams(JsonRpc2Request request) {
    Object walletAddress = request.getParams().get("requestId");
    if (walletAddress == null || StringUtils.isBlank(String.valueOf(walletAddress))) {
      return "requestId is empty";
    }
    return null;
  }

}
