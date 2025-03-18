package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.ClaimRecord;
import org.apache.ibatis.annotations.Param;

public interface ClaimRecordMapper {
    int deleteByPrimaryKey(Long claimRecordId);

    int insert(ClaimRecord record);

    int insertSelective(ClaimRecord record);

    ClaimRecord selectByPrimaryKey(Long claimRecordId);

    int updateByPrimaryKeySelective(ClaimRecord record);

    int updateByPrimaryKey(ClaimRecord record);


    ClaimRecord selectByRequestId(@Param("requestId") String requestId);
}