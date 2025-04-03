package io.opendid.web2gateway.tasks.subscription;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.model.dto.oracle.subscription.SubTokenTransferInfoReqDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oraclebodyhandler.proxyclient.HomeWrapSubTokenTransferBodyClient;
import io.opendid.web2gateway.repository.model.SubscriptionTransferTokenRecord;
import io.opendid.web2gateway.service.SubTransferTokenDataService;
import io.opendid.web2gateway.tasks.oracleResult.OracleResultHandler;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubTokenTransferHandler {

  private final Logger logger = LoggerFactory.getLogger(SubTokenTransferHandler.class);

  @Autowired
  private SubTransferTokenDataService subTransferTokenDataService;
  @Autowired
  private HomeWrapSubTokenTransferBodyClient homeWrapSubTokenTransferBodyClient;

  public void tokenTransferManage() {

    List<SubscriptionTransferTokenRecord> subscriptionTransferTokenRecords =
        subTransferTokenDataService.selectWaitingResultData();

    for (SubscriptionTransferTokenRecord transferTokenRecord : subscriptionTransferTokenRecords) {

      try {

        SubTokenTransferInfoReqDTO subTokenTransferInfoReqDTO = new SubTokenTransferInfoReqDTO();
        subTokenTransferInfoReqDTO.setTransactionHash(transferTokenRecord.getTransactionHash());
        subTokenTransferInfoReqDTO.setVnCode(transferTokenRecord.getRequestVnCode());

        JsonRpc2Response request = homeWrapSubTokenTransferBodyClient.getTokenTransferInfo(
            subTokenTransferInfoReqDTO
        );

        if (request.getResult().toString() != null && !"".equals(request.getResult().toString())) {

          JSONObject jsonOData = JSONObject.parseObject(
              JSONObject.toJSONString(request.getResult().toString()));


          SubscriptionTransferTokenRecord updateRecord = new SubscriptionTransferTokenRecord();
          updateRecord.setTransferId(transferTokenRecord.getTransferId());
          updateRecord.setOldBalance(jsonOData.getBigDecimal("oldBalance"));
          updateRecord.setLatestBalance(jsonOData.getBigDecimal("newBalance"));

          subTransferTokenDataService.updateByPrimaryKeySelective(updateRecord);

        } else {
          logger.info("Task:SubTokenTransferTask query transaction request is null. hash:{}",
              transferTokenRecord.getTransactionHash());
        }

      } catch (Exception e) {
        logger.error("Task:SubTokenTransferTask query transaction error. hash:{}",
            transferTokenRecord.getTransactionHash());
        e.printStackTrace();
      }

    }
  }

}
