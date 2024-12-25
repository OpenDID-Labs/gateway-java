package io.opendid.web2gateway.service;

import com.alibaba.fastjson.JSONObject;
import io.opendid.web2gateway.model.dto.oracle.GetTransactionRespDTO;
import io.opendid.web2gateway.model.dto.oracle.MsgRecordInsertDTO;
import io.opendid.web2gateway.repository.mapper.OracleMsgRecordMapper;
import io.opendid.web2gateway.repository.model.OracleMsgRecordWithBLOBs;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OracleMsgRecordService {

  @Resource
  private OracleMsgRecordMapper oracleMsgRecordMapper;

  public GetTransactionRespDTO selectMsgRecordByRequestId(String requestId) {
    GetTransactionRespDTO getTransactionRespDTO = oracleMsgRecordMapper.selectByRequestId(requestId);
    if (getTransactionRespDTO != null) {
      if (getTransactionRespDTO.getResponseBody() != null) {
        getTransactionRespDTO.setResponseBody(JSONObject.parseObject(getTransactionRespDTO.getResponseBody().toString()));
      }
      return getTransactionRespDTO;
    } else {
      return new GetTransactionRespDTO();
    }
  }


  public void insertMsgRecord(MsgRecordInsertDTO dto) {

    OracleMsgRecordWithBLOBs oracleMsgRecord = new OracleMsgRecordWithBLOBs();
    oracleMsgRecord.setVnCode(dto.getVnCode());
    oracleMsgRecord.setRequestId(dto.getRequestId());
    oracleMsgRecord.setRequestBody(dto.getRequestBody());
    oracleMsgRecord.setCreateDate(new Date());
    oracleMsgRecord.setUpdateDate(new Date());

    oracleMsgRecordMapper.insertSelective(oracleMsgRecord);
  }

}
