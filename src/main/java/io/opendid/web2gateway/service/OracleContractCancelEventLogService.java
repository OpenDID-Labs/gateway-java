package io.opendid.web2gateway.service;

import io.opendid.web2gateway.common.enums.status.CancelStatusEnum;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.utils.DateUtils;
import io.opendid.web2gateway.model.dto.oracle.ContractEventLogInsertDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateCancelEventLogDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateEventLogDTO;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.mapper.OracleMsgRecordMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;
import io.opendid.web2gateway.repository.model.OracleMsgRecordWithBLOBs;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OracleContractCancelEventLogService {

  @Autowired
  private OdOracleContractEventlogMapper odOracleContractEventlogMapper;

  @Autowired
  private OracleMsgRecordMapper oracleMsgRecordMapper;

  @Value("${oracle-cancel-result-task.max-execute}")
  private Integer maxExecute;

  @Value("${oracle-cancel-result-task.interval-second}")
  private Integer intervalSecond;

  @Value("${claim-task.interval-second}")
  private Integer claimIntervalSecond;

  public int updateByPrimaryKeySelective(OdOracleContractEventlogWithBLOBs contractEventlogWithBLOBs){
    return odOracleContractEventlogMapper.updateByPrimaryKeySelective(contractEventlogWithBLOBs);
  }

  public void updateCancelEventLogByRequestId(UpdateCancelEventLogDTO updateCancelEventLogDTO) {
    Date now = new Date();

    OdOracleContractEventlogWithBLOBs eventlog = new OdOracleContractEventlogWithBLOBs();

    eventlog.setRequestId(updateCancelEventLogDTO.getRequestId());
    eventlog.setUpdateDate(now);
    eventlog.setCancelStatus(updateCancelEventLogDTO.getCancelStatus());
    eventlog.setCancelOracleHash(updateCancelEventLogDTO.getCancelOracleHash());

    if (updateCancelEventLogDTO.getCancelStatus().equals(CancelStatusEnum.SUCCESSFULLY.getCode())) {
      eventlog.setProcessStatus(ProcessStatusEnum.CANCELED.getCode());
    }

    odOracleContractEventlogMapper.updateByRequestIdAndHash(eventlog);

  }

  public void updateExecuteCount(Long logId, Integer executeCount) {

    int nextCount = executeCount + 1;

    long nextExecuteTime = (long) nextCount * intervalSecond + DateUtils.getCurrentTimestamps();

    OdOracleContractEventlogWithBLOBs eventlog = new OdOracleContractEventlogWithBLOBs();
    eventlog.setLogId(logId);
    eventlog.setUpdateDate(new Date());
    eventlog.setCancelNextExecuteTime(nextExecuteTime);
    eventlog.setCancelExecuteCount(nextCount);

//    if (maxExecute.equals(nextCount)) {
//      eventlog.setCancelStatus(CancelStatusEnum.EXCEEDING.getCode());
//      eventlog.setCancelErrorMsg("Exceeding the number of queries");
//    }

    odOracleContractEventlogMapper.updateByPrimaryKeySelective(eventlog);

  }

  public void updateClaimExecuteCount(Long logId, Integer executeCount) {

    int nextCount = executeCount + 1;

    long nextExecuteTime = (long) nextCount * claimIntervalSecond + DateUtils.getCurrentTimestamps();

    OdOracleContractEventlogWithBLOBs eventlog = new OdOracleContractEventlogWithBLOBs();
    eventlog.setLogId(logId);
    eventlog.setUpdateDate(new Date());
    eventlog.setClaimExecuteCount(nextCount);
    eventlog.setClaimExecuteTime(nextExecuteTime);

    odOracleContractEventlogMapper.updateByPrimaryKeySelective(eventlog);

  }


}
