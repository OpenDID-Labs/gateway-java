package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.OracleRequestV2Record;
import io.opendid.web2gateway.repository.model.OracleRequestV2RecordWithBLOBs;

public interface OracleRequestV2RecordMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(OracleRequestV2RecordWithBLOBs record);

    int insertSelective(OracleRequestV2RecordWithBLOBs record);

    OracleRequestV2RecordWithBLOBs selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(OracleRequestV2RecordWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(OracleRequestV2RecordWithBLOBs record);

    int updateByPrimaryKey(OracleRequestV2Record record);

    OracleRequestV2RecordWithBLOBs selectByRequestId(String requestId);

    int updateByRequestIdSelective(OracleRequestV2RecordWithBLOBs record);
}
