package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.OracleNonce;

public interface OracleNonceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OracleNonce record);

    int insertSelective(OracleNonce record);

    OracleNonce selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OracleNonce record);

    int updateByPrimaryKey(OracleNonce record);

    OracleNonce selectByPublicKey(String publicKey);

}