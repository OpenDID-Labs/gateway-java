package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;

public interface VngatewayJobidMappingMapper {
    int deleteByPrimaryKey(Long vnJobidMappingid);

    int insert(VngatewayJobidMapping record);

    int insertSelective(VngatewayJobidMapping record);

    VngatewayJobidMapping selectByPrimaryKey(Long vnJobidMappingid);

    int updateByPrimaryKeySelective(VngatewayJobidMapping record);

    int updateByPrimaryKey(VngatewayJobidMapping record);

    VngatewayJobidMapping selectByJobId(String jobId);

    int deleteAll();

}