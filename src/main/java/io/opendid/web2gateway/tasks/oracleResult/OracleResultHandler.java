package io.opendid.web2gateway.tasks.oracleResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.request.MethodEnum;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.traceid.TraceIdPutUtil;
import io.opendid.web2gateway.common.utils.DateUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.EventLogPendingInputDTO;
import io.opendid.web2gateway.model.dto.oracle.EventLogPendingOutputDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateEventLogDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.service.OracleContractEventLogService;
import jakarta.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
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

  @Value("${oracle-result-task.interval-second}")
  private Integer intervalSecond;

  @Resource
  private VnGatewayClient vnGatewayClient;

  @Autowired
  private OdOracleContractEventlogMapper oracleContractEventLogMapper;

  @Autowired
  private OracleContractEventLogService oracleContractEventLogService;


  public void resultManage() {

    EventLogPendingInputDTO eventLogPendingInputDTO = new EventLogPendingInputDTO();
    eventLogPendingInputDTO.setProcessStatus(ProcessStatusEnum.PENDING.getCode());
    eventLogPendingInputDTO.setExecuteCount(maxExecute);
    eventLogPendingInputDTO.setNextExecuteTime(DateUtils.getCurrentTimestamps());

    List<EventLogPendingOutputDTO> eventLogPendingOutputDTOS = oracleContractEventLogMapper.selectPendingData(
        eventLogPendingInputDTO);

    logger.info("OracleResultHandler pending data size: {}", eventLogPendingOutputDTOS.size());

    if (!eventLogPendingOutputDTOS.isEmpty()) {

      for (EventLogPendingOutputDTO pendingData : eventLogPendingOutputDTOS) {
        TraceIdPutUtil.newPutTraceId();
        try {

          logger.info("process pending data: {}", JSON.toJSONString(pendingData));
          JsonRpc2Response request = vnGatewayClient.request(requestParameters(pendingData));

          if (request != null) {

            String resultStr = JSONObject.toJSONString(request.getResult());

            JSONObject resultJson = JSONObject.parseObject(resultStr);

            if (resultJson.getString("requestId") != null
                && !"".equals(resultJson.getString("requestId"))) {

              UpdateEventLogDTO updateEventLogDTO = new UpdateEventLogDTO();

              updateEventLogDTO.setRequestId(pendingData.getRequestId());
              updateEventLogDTO.setProcessStatus(ProcessStatusEnum.PROCESSED.getCode());
              updateEventLogDTO.setResponseBody(resultStr);
              updateEventLogDTO.setCallbackOracleHash(resultJson.getString("oracleFulfillTxHash"));

              oracleContractEventLogService.updateEventLogByRequestId(updateEventLogDTO);

            } else {
              updateExecuteCount(pendingData.getLogId(), pendingData.getExecuteCount());
            }

          } else {
            updateExecuteCount(pendingData.getLogId(), pendingData.getExecuteCount());
          }


        } catch (JsonRpc2ServerErrorException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private void updateExecuteCount(Long logId, Integer executeCount) {

    int nextCount = executeCount + 1;

    long nextExecuteTime = (long) nextCount * intervalSecond + DateUtils.getCurrentTimestamps();

    OdOracleContractEventlog eventlog = new OdOracleContractEventlog();
    eventlog.setLogId(logId);
    eventlog.setUpdateDate(new Date());
    eventlog.setNextExecuteTime(nextExecuteTime);
    eventlog.setExecuteCount(nextCount);

    if (maxExecute.equals(nextCount)) {
      eventlog.setProcessStatus(ProcessStatusEnum.EXCEEDING.getCode());
      eventlog.setErrorMsg("Exceeding the number of queries");
    }

    oracleContractEventLogMapper.updateByPrimaryKeySelective(eventlog);

  }

  private VnClientJobIdDTO requestParameters(EventLogPendingOutputDTO pendingData) {

    LinkedHashMap<String, String> params = new LinkedHashMap<>();

    params.put("requestId", pendingData.getRequestId());

    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        1L,
        MethodEnum.RESULT.getCode(),
        params,
        ""
    );

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();

    vnClientJobIdDTO.setJobId(pendingData.getJobId());
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);

    return vnClientJobIdDTO;
  }


}
