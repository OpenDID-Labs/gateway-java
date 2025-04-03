package io.opendid.web2gateway.tasks.oracleResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.request.MethodEnum;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.traceid.TraceIdPutUtil;
import io.opendid.web2gateway.common.utils.DateUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.model.dto.oracle.EventLogPendingInputDTO;
import io.opendid.web2gateway.model.dto.oracle.EventLogPendingOutputDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.MethodExecutor;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import jakarta.annotation.Resource;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OracleResultHandler {

  private final Logger logger = LoggerFactory.getLogger(OracleResultHandler.class);

  @Value("${oracle-result-task.max-execute}")
  private Integer maxExecute;


  @Resource
  private VnGatewayClient vnGatewayClient;

  @Autowired
  private OdOracleContractEventlogMapper oracleContractEventLogMapper;

  @Autowired
  private OracleContractEventLogService oracleContractEventLogService;


  public void resultManage() {

    EventLogPendingInputDTO eventLogPendingInputDTO = new EventLogPendingInputDTO();
    eventLogPendingInputDTO.setProcessStatus(ProcessStatusEnum.PENDING.getCode());
//    eventLogPendingInputDTO.setExecuteCount(maxExecute);
    eventLogPendingInputDTO.setNextExecuteTime(DateUtils.getCurrentTimestamps());

    List<EventLogPendingOutputDTO> eventLogPendingOutputDTOS = oracleContractEventLogMapper.selectPendingData(
        eventLogPendingInputDTO);

    logger.info("OracleResultHandler pending data size: {}", eventLogPendingOutputDTOS.size());

    if (!eventLogPendingOutputDTOS.isEmpty()) {

      for (EventLogPendingOutputDTO pendingData : eventLogPendingOutputDTOS) {
        TraceIdPutUtil.newPutTraceId();
        try {

          logger.info("process pending data: {}", JSON.toJSONString(pendingData));
          JsonRpc2Response request = vnGatewayClient.requestJobSend(requestParameters(pendingData));

          logger.info("process pending data response={}", JSON.toJSONString(request));

          if (request != null && request.getResult() != null) {
            logger.info("process pending data request != null");

            String resultStr = JSONObject.toJSONString(request.getResult());
            logger.info("process pending data resultStr={}",resultStr);

            JSONObject resultJson = JSONObject.parseObject(resultStr);

            String oracleRequestTxHash = resultJson.getString("oracleRequestTxHash");
            if (pendingData.getRequestOracleHash().equals(oracleRequestTxHash)) {
              logger.info("process pending data hash same,RequestOracleHash={},oracleRequestTxHash={}",
                  pendingData.getRequestOracleHash(),oracleRequestTxHash);

              if (Integer.valueOf(ProcessStatusEnum.PROCESSED.getCode()).equals(resultJson.getInteger("status"))) {
                logger.info("process pending data state is PROCESSED");
                LinkedHashMap<Object, Object> resultLinkedHashMap = new LinkedHashMap<>();
                resultLinkedHashMap.put("requestId", resultJson.getString("requestId"));
                resultLinkedHashMap.put("oracleFulfillTxHash", resultJson.getString("oracleFulfillTxHash"));
                resultLinkedHashMap.put("oracleRequestTxHash", oracleRequestTxHash);
                resultLinkedHashMap.put("data", resultJson.getString("data"));
                resultLinkedHashMap.put("userPayFee", resultJson.getString("userPayFee"));
                resultLinkedHashMap.put("coinType", resultJson.getInteger("coinType"));


                JsonRpc2Request oracleCallBack = new JsonRpc2Request(1L, "oracle_callback", resultLinkedHashMap, "");
                MethodExecutor.publicMethod(JSONObject.toJSONString(oracleCallBack));
              } else {
                logger.info("process pending data state is not PROCESSED");

                if (StringUtils.isNotBlank(resultJson.getString("requestId"))) {
                  logger.info("process pending data RequestId={}",resultJson.getString("requestId"));

                  OdOracleContractEventlogWithBLOBs updateB = new OdOracleContractEventlogWithBLOBs();
                  updateB.setLogId(pendingData.getLogId());
                  updateB.setRequestId(resultJson.getString("requestId"));
                  updateB.setCoinType(resultJson.getInteger("coinType"));
                  oracleContractEventLogService.updateByPrimaryKeySelective(updateB);
                }
                oracleContractEventLogService.updateExecuteCount(pendingData.getLogId(), pendingData.getExecuteCount());
              }
            }
          } else {
            oracleContractEventLogService.updateExecuteCount(pendingData.getLogId(), pendingData.getExecuteCount());
          }

        } catch (Exception e) {
          logger.error("process OracleResultHandler pending data error", e);
          throw new RuntimeException(e);
        }
      }
    }
  }


  private VnClientJobIdDTO requestParameters(EventLogPendingOutputDTO pendingData) {

    LinkedHashMap<String, String> params = new LinkedHashMap<>();

    params.put("oracleRequestTxHash", pendingData.getRequestOracleHash());

    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        1L,
        MethodEnum.RESULT.getCode(),
        params,
        ""
    );

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();

    vnClientJobIdDTO.setJobId(pendingData.getJobId());
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);
    vnClientJobIdDTO.setVnCode(pendingData.getVnCode());

    return vnClientJobIdDTO;
  }


}
