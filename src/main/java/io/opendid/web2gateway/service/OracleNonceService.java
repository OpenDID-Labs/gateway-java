package io.opendid.web2gateway.service;

import io.opendid.web2gateway.model.dto.oracle.OracleNonceUpdateDTO;
import io.opendid.web2gateway.repository.mapper.OracleNonceMapper;
import io.opendid.web2gateway.repository.model.OracleNonce;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import org.springframework.stereotype.Service;

/**
* OracleNonceService
* @author Dong-Jianguo
* @Date: 2024/12/17
* @version 1.0.0
*
* @history date, modifier,and description
**/
@Service
public class OracleNonceService {

  @Autowired
  private OracleNonceMapper oracleNonceMapper;


  public Long updateOracleNonce(OracleNonceUpdateDTO oracleNonceUpdateDTO){

    OracleNonce oracleNonce = oracleNonceMapper.selectByPublicKey(oracleNonceUpdateDTO.getPublicKey());

    if (oracleNonce == null){
      oracleNonce = new OracleNonce();
      oracleNonce.setNonceValue(1L);
      oracleNonce.setPublicKey(oracleNonceUpdateDTO.getPublicKey());
      oracleNonce.setAccountAddress(oracleNonceUpdateDTO.getAddress());
      oracleNonce.setCreateDate(new Date());
      oracleNonce.setUpdateDate(new Date());

      oracleNonceMapper.insertSelective(oracleNonce);
    }else {
      Long originalNonceValue = oracleNonce.getNonceValue();

      oracleNonce.setNonceValue(Math.addExact(originalNonceValue,1));
      oracleNonce.setUpdateDate(new Date());

      oracleNonceMapper.updateByPrimaryKeySelective(oracleNonce);
    }

    return oracleNonce.getNonceValue();
  }


}
