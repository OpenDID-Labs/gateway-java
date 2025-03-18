package io.opendid.web2gateway.tasks.oraclecancel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.enums.request.MethodEnum;
import io.opendid.web2gateway.common.enums.status.CancelStatusEnum;
import io.opendid.web2gateway.common.traceid.TraceIdPutUtil;
import io.opendid.web2gateway.common.utils.DateUtils;
import io.opendid.web2gateway.common.vnclient.VnGatewayClient;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.CancelEventLogPendingInDTO;
import io.opendid.web2gateway.model.dto.oracle.CancelEventLogPendingOutDTO;
import io.opendid.web2gateway.model.dto.vnclient.VnClientJobIdDTO;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Request;
import io.opendid.web2gateway.model.jsonrpc2.JsonRpc2Response;
import io.opendid.web2gateway.oracleweb2process.MethodExecutor;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.service.OracleContractCancelEventLogService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
public class OracleCancelHandler {

  private final Logger logger = LoggerFactory.getLogger(OracleCancelHandler.class);

  @Value("${oracle-cancel-result-task.max-execute}")
  private Integer maxExecute;

  @Value("${oracle-cancel-result-task.interval-second}")
  private Integer intervalSecond;

  @Resource
  private OracleContractCancelEventLogService cancelEventLogService;

  @Resource
  private OdOracleContractEventlogMapper odOracleContractEventlogMapper;

  @Resource
  private VnGatewayClient vnGatewayClient;

  public void cancelManage() {

    CancelEventLogPendingInDTO cancelEventLogPendingInDTO = new CancelEventLogPendingInDTO();
    cancelEventLogPendingInDTO.setCancelStatus(CancelStatusEnum.PENDING.getCode());
//    cancelEventLogPendingInDTO.setCancelExecuteCount(maxExecute);
    cancelEventLogPendingInDTO.setCancelNextExecuteTime(DateUtils.getCurrentTimestamps());

    // query data which cancel_status is pending
    List<CancelEventLogPendingOutDTO> cancelEventLogPendingList = odOracleContractEventlogMapper.selectCancelPendingData(
        cancelEventLogPendingInDTO);

    logger.info("OracleCancelHandler pending data size: {}", cancelEventLogPendingList.size());
    try {

      for (CancelEventLogPendingOutDTO pendingOutDTO : cancelEventLogPendingList) {

        TraceIdPutUtil.newPutTraceId();
        logger.info("process cancel pending data: {}", JSON.toJSONString(pendingOutDTO));
        JsonRpc2Response request = null;

        request = vnGatewayClient.request(requestParameters(pendingOutDTO));

        if (request != null) {

          String resultStr = JSONObject.toJSONString(request.getResult());

          logger.info("cancel task opendid return value : {}", JSON.toJSONString(pendingOutDTO));

          JSONArray resultJsonArray = JSONArray.parseArray(resultStr);

          for (int i = 0; i < resultJsonArray.size(); i++) {
            JSONObject resultJson = resultJsonArray.getJSONObject(i);
            Integer status = resultJson.getInteger("status");

            if (
                status.equals(CancelStatusEnum.SUCCESSFULLY.getCode())
                    || status.equals(CancelStatusEnum.NOT_EXPIRED.getCode())
                    || status.equals(CancelStatusEnum.INSUFFICIENT_BALANCE.getCode())
            ) {

              LinkedHashMap<Object, Object> resultLinkedHashMap = new LinkedHashMap<>();
              resultLinkedHashMap.put("requestId", resultJson.getString("requestId"));
              resultLinkedHashMap.put("status", status);
              resultLinkedHashMap.put("cancelTxHash", resultJson.getString("cancelTxHash"));
              JsonRpc2Request oracleCallBack = new JsonRpc2Request(1L, "request_cancel_callback",
                  resultLinkedHashMap, "");
              MethodExecutor.publicMethod(JSONObject.toJSONString(oracleCallBack));

            } else {
              cancelEventLogService.updateExecuteCount(pendingOutDTO.getLogId(),pendingOutDTO.getCancelExecuteCount());
            }

          }
        } else {
          cancelEventLogService.updateExecuteCount(pendingOutDTO.getLogId(),pendingOutDTO.getCancelExecuteCount());
        }

      }
    } catch (Exception e) {
      logger.error("OracleCancelHandler run failed:", e);
      throw new RuntimeException(e);
    }

  }

  private VnClientJobIdDTO requestParameters(CancelEventLogPendingOutDTO pendingOutDTO) {
    LinkedHashMap<String, String> params = new LinkedHashMap<>();

    params.put("requestId", pendingOutDTO.getRequestId());

    JsonRpc2Request jsonRpc2Request = new JsonRpc2Request(
        1L,
        MethodEnum.CANCEL_RESULT.getCode(),
        params,
        ""
    );

    VnClientJobIdDTO vnClientJobIdDTO = new VnClientJobIdDTO();
    vnClientJobIdDTO.setJobId(pendingOutDTO.getJobId());
    vnClientJobIdDTO.setRequestBody(jsonRpc2Request);
    vnClientJobIdDTO.setVnCode(pendingOutDTO.getVnCode());

    return vnClientJobIdDTO;
  }

}
