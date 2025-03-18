package io.opendid.web2gateway.tasks.claimtask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.request.MethodEnum;
import io.opendid.web2gateway.common.enums.status.CancelStatusEnum;
import io.opendid.web2gateway.common.enums.status.ClaimStatusEnum;
import io.opendid.web2gateway.common.traceid.TraceIdPutUtil;
import io.opendid.web2gateway.common.utils.DateUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.model.dto.oracle.CancelEventLogPendingOutDTO;
import io.opendid.web2gateway.model.dto.oracle.ClaimEventLogPendingInDTO;
import io.opendid.web2gateway.model.dto.oracle.ClaimEventLogPendingOutDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.ClaimRecord;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;
import io.opendid.web2gateway.service.ClaimRecordDataService;
import io.opendid.web2gateway.service.OracleContractCancelEventLogService;
import jakarta.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClaimHandler {

  private final Logger logger = LoggerFactory.getLogger(ClaimHandler.class);

  @Autowired
  private OdOracleContractEventlogMapper oracleContractEventlogMapper;
  @Resource
  private VnGatewayClient vnGatewayClient;
  @Resource
  private OracleContractCancelEventLogService cancelEventLogService;
  @Autowired
  private ClaimRecordDataService claimRecordDataService;

  public void claimManage() {

    ClaimEventLogPendingInDTO claimEventLogPendingInDTO = new ClaimEventLogPendingInDTO();
    claimEventLogPendingInDTO.setClaimExecuteTime(DateUtils.getCurrentTimestamps());

    List<ClaimEventLogPendingOutDTO> claimEventLogPendingOutDTOS =
        oracleContractEventlogMapper.selectClaimPendingData(claimEventLogPendingInDTO);

    logger.info("ClaimHandler pending data size: {}", claimEventLogPendingOutDTOS.size());

    try {

      for (ClaimEventLogPendingOutDTO pendingOutDTO : claimEventLogPendingOutDTOS) {

        TraceIdPutUtil.newPutTraceId();
        logger.info("process cancel pending data: {}", JSON.toJSONString(pendingOutDTO));

        JsonRpc2Response request = vnGatewayClient.request(requestParameters(pendingOutDTO));

        if (request != null) {

          String resultStr = JSONObject.toJSONString(request.getResult());
          logger.info("ClaimHandler opendid return value : {}", resultStr);

          JSONObject jsonObject = JSONObject.parseObject(resultStr);

          if (jsonObject.getInteger("claimStatus")
              .equals(ClaimStatusEnum.CREATE_SUCCESS.getCode())) {

            OdOracleContractEventlogWithBLOBs contractEventlogWithBLOBs =
                new OdOracleContractEventlogWithBLOBs();
            contractEventlogWithBLOBs.setLogId(pendingOutDTO.getLogId());
            contractEventlogWithBLOBs.setClaimStatus(ClaimStatusEnum.CREATE_SUCCESS.getCode());

            cancelEventLogService.updateByPrimaryKeySelective(contractEventlogWithBLOBs);

            insertClaim(jsonObject.getJSONObject("claimRecordRespDTO"));
          } else {
            cancelEventLogService.updateClaimExecuteCount(pendingOutDTO.getLogId(),
                pendingOutDTO.getClaimExecuteCount());
          }

        } else {
          cancelEventLogService.updateClaimExecuteCount(pendingOutDTO.getLogId(),
              pendingOutDTO.getClaimExecuteCount());
        }


      }
    } catch (Exception e) {
      logger.error("ClaimHandler run failed:", e);
      throw new RuntimeException(e);
    }

  }

  private void insertClaim(JSONObject jsonData){

    ClaimRecord claimRecord = new ClaimRecord();

    claimRecord.setChainName(jsonData.getString("chainName"));
    claimRecord.setRequestId(jsonData.getString("requestId"));
    claimRecord.setClaimId(jsonData.getString("claimId"));
    claimRecord.setIssuer(jsonData.getString("issuer"));
    claimRecord.setRequestTxnHash(jsonData.getString("requestTxnHash"));
    claimRecord.setRequestDataHash(jsonData.getString("requestDataHash"));
    claimRecord.setResponseTxnHash(jsonData.getString("responseTxnHash"));
    claimRecord.setResponseDataHash(jsonData.getString("responseDataHash"));
    claimRecord.setCustomizeHash(jsonData.getString("customizeHash"));
    claimRecord.setIdSystem(jsonData.getString("idSystem"));
    claimRecord.setIssuanceDate(jsonData.getString("issuanceDate"));
    claimRecord.setExpirationDate(jsonData.getString("expirationDate"));
    claimRecord.setSignature(jsonData.getString("signature"));
    claimRecord.setTransactionHash(jsonData.getString("transactionHash"));
    claimRecord.setVersion(jsonData.getString("version"));
    claimRecord.setContractParams(jsonData.getString("contractParams"));

    claimRecordDataService.insertSelective(claimRecord);
  }

  private VnClientJobIdDTO requestParameters(ClaimEventLogPendingOutDTO pendingOutDTO) {
    LinkedHashMap<String, String> params = new LinkedHashMap<>();

    params.put("requestId", pendingOutDTO.getRequestId());

    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        1L,
        MethodEnum.REQUEST_CLAIM.getCode(),
        params,
        ""
    );

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setJobId(pendingOutDTO.getJobId());
    vnClientJobIdDTO.setVnCode(pendingOutDTO.getVnCode());
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);

    return vnClientJobIdDTO;
  }

}
