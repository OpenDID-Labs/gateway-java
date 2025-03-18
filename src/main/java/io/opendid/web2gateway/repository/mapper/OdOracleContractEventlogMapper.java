package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.model.dto.oracle.*;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlog;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;

import java.util.List;

public interface OdOracleContractEventlogMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(OdOracleContractEventlog record);

    int insertSelective(OdOracleContractEventlog record);

    OdOracleContractEventlog selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(OdOracleContractEventlogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(OdOracleContractEventlog record);

    int updateByPrimaryKey(OdOracleContractEventlog record);

    List<EventLogPendingOutputDTO> selectPendingData(EventLogPendingInputDTO eventLogPendingInputDTO);

    int updateByRequestId(OdOracleContractEventlogWithBLOBs record);

    OdOracleContractEventlog selectByRequestId(String requestId);

    List<GetCancelTransactionRespDTO> selectCancelStatusByRequestId(String requestId);

    List<CancelEventLogPendingOutDTO> selectCancelPendingData(CancelEventLogPendingInDTO inDTO);

    int updateByRequestIdAndHash(OdOracleContractEventlogWithBLOBs record);

    int updateByRequestTransactionHash(OdOracleContractEventlogWithBLOBs record);

    List<ClaimEventLogPendingOutDTO> selectClaimPendingData(ClaimEventLogPendingInDTO inDTO);

    int updateClaimStatusByRequestId(OdOracleContractEventlogWithBLOBs record);


    OdOracleContractEventlog selectByRequestOracleHash(String requestOracleHash);

}