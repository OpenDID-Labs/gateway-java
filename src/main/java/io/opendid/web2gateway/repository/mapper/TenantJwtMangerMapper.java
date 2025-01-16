package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.TenantJwtManger;

public interface TenantJwtMangerMapper {
    int deleteByPrimaryKey(Long tenantJwtId);

    int insert(TenantJwtManger record);

    int insertSelective(TenantJwtManger record);

    TenantJwtManger selectByPrimaryKey(Long tenantJwtId);

    int updateByPrimaryKeySelective(TenantJwtManger record);

    int updateByPrimaryKey(TenantJwtManger record);


    TenantJwtManger selectBySubjectId(String subjectId);

    int deleteBySubId(String jwt);
}