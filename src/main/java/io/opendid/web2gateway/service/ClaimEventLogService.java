package io.opendid.web2gateway.service;

import io.opendid.web2gateway.common.enums.status.ClaimStatusEnum;
import io.opendid.web2gateway.repository.mapper.ClaimRecordMapper;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.model.ClaimRecord;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class ClaimEventLogService {

    @Resource
    private OdOracleContractEventlogMapper oracleContractEventlogMapper;

    @Resource
    private ClaimRecordMapper claimRecordMapper;

    private static final Logger logger = LoggerFactory.getLogger(ClaimEventLogService.class);

    public void updateEventLogClaimStatus(String requestId) {

        OdOracleContractEventlogWithBLOBs odOracleContractEventlogWithBLOBs = new OdOracleContractEventlogWithBLOBs();
        odOracleContractEventlogWithBLOBs.setRequestId(requestId);
        odOracleContractEventlogWithBLOBs.setClaimStatus(ClaimStatusEnum.CREATE_SUCCESS.getCode());

        oracleContractEventlogMapper.updateClaimStatusByRequestId(odOracleContractEventlogWithBLOBs);
    }


    public void insertClaim(LinkedHashMap params) {
        ClaimRecord claimRecord = new ClaimRecord();
        claimRecord.setChainName(params.get("chainName").toString());
        claimRecord.setRequestId(params.get("requestId").toString());
        claimRecord.setClaimId(params.get("claimId").toString());
        claimRecord.setIssuer(params.get("issuer").toString());
        claimRecord.setRequestTxnHash(params.get("requestTxnHash").toString());
        claimRecord.setRequestDataHash(params.get("requestDataHash").toString());
        claimRecord.setResponseTxnHash(params.get("responseTxnHash").toString());
        claimRecord.setResponseDataHash(params.get("responseDataHash").toString());
        claimRecord.setCustomizeHash(params.get("customizeHash").toString());
        claimRecord.setIdSystem(params.get("idSystem").toString());
        claimRecord.setIssuanceDate(params.get("issuanceDate").toString());
        claimRecord.setExpirationDate(params.get("expirationDate").toString());
        claimRecord.setSignature(params.get("signature").toString());
        claimRecord.setTransactionHash(params.get("transactionHash").toString());
        claimRecord.setVersion(params.get("version").toString());
        claimRecord.setContractParams(params.get("contractParams").toString());

        try {
            claimRecordMapper.insertSelective(claimRecord);
        } catch (DuplicateKeyException e) {
            logger.info("insert claimRecord throw DuplicateKeyException, {}", claimRecord.getClaimId());
        }

    }
}
