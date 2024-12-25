package io.opendid.web2gateway.service;

import io.opendid.web2gateway.model.dto.oracle.ContractEventLogInsertDTO;
import io.opendid.web2gateway.model.dto.oracle.UpdateEventLogDTO;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.mapper.OracleMsgRecordMapper;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.repository.model.OracleMsgRecordWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OracleContractEventLogService {

  @Autowired
  private OdOracleContractEventlogMapper odOracleContractEventlogMapper;
  @Autowired
  private OracleMsgRecordMapper oracleMsgRecordMapper;


  public void insertContractEventLog(ContractEventLogInsertDTO dto){

    OdOracleContractEventlog odOracleContractEventlog = new OdOracleContractEventlog();
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

    odOracleContractEventlogMapper.insertSelective(odOracleContractEventlog);
  }


  public void updateEventLogByRequestId(UpdateEventLogDTO updateEventLogDTO){

    Date now = new Date();

    OdOracleContractEventlog eventlog = new OdOracleContractEventlog();

    eventlog.setRequestId(updateEventLogDTO.getRequestId());
    eventlog.setProcessStatus(updateEventLogDTO.getProcessStatus());
    eventlog.setCallbackOracleHash(updateEventLogDTO.getCallbackOracleHash());
    eventlog.setUpdateDate(now);

    odOracleContractEventlogMapper.updateByRequestId(eventlog);

    OracleMsgRecordWithBLOBs msgRecordWithBLOBs = new OracleMsgRecordWithBLOBs();
    msgRecordWithBLOBs.setRequestId(updateEventLogDTO.getRequestId());
    msgRecordWithBLOBs.setResponseBody(updateEventLogDTO.getResponseBody());
    msgRecordWithBLOBs.setUpdateDate(now);

    oracleMsgRecordMapper.updateByRequestId(msgRecordWithBLOBs);

  }


}
