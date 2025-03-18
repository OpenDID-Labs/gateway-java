package io.opendid.web2gateway.service;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.common.codes.JsonRpc2MessageCodeEnum;
import io.opendid.web2gateway.common.traceid.LogTraceIdConstant;
import io.opendid.web2gateway.exception.throwentity.jsonrpc2.JsonRpc2ServerErrorException;
import io.opendid.web2gateway.model.dto.oracle.GetCancelTransactionRespDTO;
import io.opendid.web2gateway.model.dto.oracle.GetTransactionRespDTO;
import io.opendid.web2gateway.model.dto.oracle.MsgRecordInsertDTO;
import io.opendid.web2gateway.repository.mapper.ClaimRecordMapper;
import io.opendid.web2gateway.repository.mapper.OdOracleContractEventlogMapper;
import io.opendid.web2gateway.repository.mapper.OracleMsgRecordMapper;
import io.opendid.web2gateway.repository.model.ClaimRecord;
import io.opendid.web2gateway.repository.model.OdOracleContractEventlogWithBLOBs;
import io.opendid.web2gateway.repository.model.OracleMsgRecordWithBLOBs;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OracleMsgRecordService {

  @Resource
  private OracleMsgRecordMapper oracleMsgRecordMapper;

  @Resource
  private OdOracleContractEventlogMapper contractEventlogMapper;

  @Autowired
  private ClaimRecordMapper claimRecordMapper;

  public  GetTransactionRespDTO selectMsgRecordByRequestId(String requestTransactionHash) throws Exception {


    GetTransactionRespDTO getTransactionRespDTO =
            oracleMsgRecordMapper.selectByRequestTransactionHash(requestTransactionHash);

    if (requestTransactionHash != null ) {
        if (getTransactionRespDTO.getResponseBody() != null) {
          getTransactionRespDTO.setResponseBody(
              JSONObject.parseObject(getTransactionRespDTO.getResponseBody().toString())
          );
        }
      ClaimRecord claimRecord = claimRecordMapper.selectByRequestId(getTransactionRespDTO.getRequestId());

        if (claimRecord != null) {
          if (StringUtils.isNotBlank(claimRecord.getContractParams())){
            JSONObject jsonObject = JSONObject.parseObject(claimRecord.getContractParams());
            jsonObject.put("claimId",claimRecord.getClaimId());
            getTransactionRespDTO.setClaimBody(jsonObject);
          }


        }

      return getTransactionRespDTO;
    } else {
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getMessage(),
          "The transaction data you requested does not exist");
    }

  }




  public List<GetCancelTransactionRespDTO> selectCancelStatusByRequestId(String requestId) throws Exception {

    List<GetCancelTransactionRespDTO> cancelTransactionRespDTO =
        contractEventlogMapper.selectCancelStatusByRequestId(requestId);

    if(cancelTransactionRespDTO != null && cancelTransactionRespDTO.size() > 0){
      return cancelTransactionRespDTO;
    } else {
      throw new JsonRpc2ServerErrorException(
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getCode(),
          MDC.get(LogTraceIdConstant.TRACE_ID),
          JsonRpc2MessageCodeEnum.JSON_RPC2_CODE_32004.getMessage(),
          "The transaction data you requested does not exist");
    }

  }

}
