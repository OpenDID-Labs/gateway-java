package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.model.dto.oracle.GetTransactionRespDTO;
import io.opendid.web2gateway.repository.model.OracleMsgRecord;
import io.opendid.web2gateway.repository.model.OracleMsgRecordWithBLOBs;

public interface OracleMsgRecordMapper {
    int deleteByPrimaryKey(Long msgId);

    int insert(OracleMsgRecordWithBLOBs record);

    int insertSelective(OracleMsgRecordWithBLOBs record);

    OracleMsgRecordWithBLOBs selectByPrimaryKey(Long msgId);

    int updateByPrimaryKeySelective(OracleMsgRecordWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(OracleMsgRecordWithBLOBs record);

    int updateByPrimaryKey(OracleMsgRecord record);

    GetTransactionRespDTO selectByRequestId(String requestId);

    int updateByRequestId(OracleMsgRecordWithBLOBs record);
}