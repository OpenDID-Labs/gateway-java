package io.opendid.web2gateway.service;

import com.alibaba.fastjson.JSON;
import io.opendid.web2gateway.common.enums.status.ProcessStatusEnum;
import io.opendid.web2gateway.common.utils.DateUtils;
import io.opendid.web2gateway.model.dto.oracle.ContractEventLogInsertDTO;
import io.opendid.web2gateway.model.dto.oracle.ContractEventLogUpdateForCancelDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateEventLogDTO;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.mapper.OracleMsgRecordMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;
import io.opendid.web2gateway.repository.model.OracleMsgRecordWithBLOBs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OracleContractEventLogService {

  private final Logger logger = LoggerFactory.getLogger(OracleContractEventLogService.class);

  @Autowired
  private OdOracleContractEventlogMapper odOracleContractEventlogMapper;
  @Autowired
  private OracleMsgRecordMapper oracleMsgRecordMapper;

  @Value("${oracle-result-task.max-execute}")
  private Integer maxExecute;

  @Value("${oracle-result-task.interval-second}")
  private Integer intervalSecond;


  public void insertContractEventLog(ContractEventLogInsertDTO dto){

    OdOracleContractEventlogWithBLOBs odOracleContractEventlog = new OdOracleContractEventlogWithBLOBs();
    odOracleContractEventlog.setRequestId(dto.getRequestId());
    odOracleContractEventlog.setVnCode(dto.getVnCode());
    odOracleContractEventlog.setJobId(dto.getJobId());
    odOracleContractEventlog.setJobIdFee(dto.getJobIdFee());
    odOracleContractEventlog.setPlatformCode(dto.getPlatformCode());
    odOracleContractEventlog.setProcessStatus(dto.getProcessStatus());
    odOracleContractEventlog.setTraceId(dto.getTraceId());
    odOracleContractEventlog.setCreateDate(new Date());
    odOracleContractEventlog.setUpdateDate(new Date());
    odOracleContractEventlog.setRequestOracleHash(dto.getRequestOracleHash());
    odOracleContractEventlog.setRequestAptosVersion(dto.getRequestAptosVersion());
    odOracleContractEventlog.setErrorMsg(dto.getErrorMsg());
    odOracleContractEventlog.setRequestBody(dto.getRequestBody());
    odOracleContractEventlog.setTransactionBatchCode(dto.getTransactionBatchCode());
    odOracleContractEventlog.setKeyCode(dto.getKeyCode());
    odOracleContractEventlog.setClaimFee(dto.getClaimFee());
    odOracleContractEventlog.setClaimStatus(dto.getClaimStatus());

    odOracleContractEventlogMapper.insertSelective(odOracleContractEventlog);
  }


  public void updateEventLogByRequestId(UpdateEventLogDTO updateEventLogDTO){

    Date now = new Date();

    OdOracleContractEventlogWithBLOBs eventlog = new OdOracleContractEventlogWithBLOBs();

    eventlog.setRequestId(updateEventLogDTO.getRequestId());
    eventlog.setProcessStatus(updateEventLogDTO.getProcessStatus());
    eventlog.setCallbackOracleHash(updateEventLogDTO.getCallbackOracleHash());
    eventlog.setResponseBody(updateEventLogDTO.getResponseBody());
    eventlog.setUpdateDate(now);
    eventlog.setRequestOracleHash(updateEventLogDTO.getRequestTransactionHash());

    odOracleContractEventlogMapper.updateByRequestTransactionHash(eventlog);


    /*OracleMsgRecordWithBLOBs msgRecordWithBLOBs = new OracleMsgRecordWithBLOBs();
    msgRecordWithBLOBs.setRequestId(updateEventLogDTO.getRequestId());
    msgRecordWithBLOBs.setResponseBody(updateEventLogDTO.getResponseBody());
    msgRecordWithBLOBs.setUpdateDate(now);

    oracleMsgRecordMapper.updateByRequestId(msgRecordWithBLOBs);*/

  }

  public void cancelTxLogHandle(ContractEventLogUpdateForCancelDTO cancelDTO){

    Date now = new Date();

    OdOracleContractEventlogWithBLOBs contractEventlog = new OdOracleContractEventlogWithBLOBs();
    contractEventlog.setRequestId(cancelDTO.getRequestId());
    contractEventlog.setCancelStatus(cancelDTO.getCancelStatus());
    contractEventlog.setCancelOracleHash(cancelDTO.getCancelOracleHash());
    contractEventlog.setCancelErrorMsg(cancelDTO.getCancelErrorMsg());
    contractEventlog.setUpdateDate(now);
    contractEventlog.setCancelResponseBody(cancelDTO.getRequestBody());

    odOracleContractEventlogMapper.updateByRequestId(contractEventlog);


    /*OracleMsgRecordWithBLOBs msgRecordWithBLOBs = new OracleMsgRecordWithBLOBs();

    msgRecordWithBLOBs.setRequestId(cancelDTO.getRequestId());
    msgRecordWithBLOBs.setCancelRequestBody(cancelDTO.getRequestBody());
    msgRecordWithBLOBs.setUpdateDate(now);

    oracleMsgRecordMapper.updateByRequestId(msgRecordWithBLOBs);*/



  }

  public void updateExecuteCount(Long logId, Integer executeCount) {

    int nextCount = executeCount + 1;

    long nextExecuteTime = (long) nextCount * intervalSecond + DateUtils.getCurrentTimestamps();

    OdOracleContractEventlogWithBLOBs eventlog = new OdOracleContractEventlogWithBLOBs();
    eventlog.setLogId(logId);
    eventlog.setUpdateDate(new Date());
    eventlog.setNextExecuteTime(nextExecuteTime);
    eventlog.setExecuteCount(nextCount);

//    if (maxExecute.equals(nextCount)) {
//      eventlog.setProcessStatus(ProcessStatusEnum.EXCEEDING.getCode());
//      eventlog.setErrorMsg("Exceeding the number of queries");
//    }

    logger.info("OracleContractEventLogService updateExecuteCount params={}", JSON.toJSONString(eventlog));

    odOracleContractEventlogMapper.updateByPrimaryKeySelective(eventlog);

  }

  public int updateByPrimaryKeySelective(OdOracleContractEventlogWithBLOBs record){
    return odOracleContractEventlogMapper.updateByPrimaryKeySelective(record);
  }

}
