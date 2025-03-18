package io.opendid.web2gateway.repository.mapper;

import io.opendid.web2gateway.model.dto.oracle.SelectJobIdMappingDTO;
import io.opendid.web2gateway.repository.model.VngatewayJobidMapping;

import java.util.List;

public interface VngatewayJobidMappingMapper {
    int deleteByPrimaryKey(Long vnJobidMappingid);

    int insert(VngatewayJobidMapping record);

    int insertSelective(VngatewayJobidMapping record);

    VngatewayJobidMapping selectByPrimaryKey(Long vnJobidMappingid);

    int updateByPrimaryKeySelective(VngatewayJobidMapping record);

    int updateByPrimaryKey(VngatewayJobidMapping record);

    VngatewayJobidMapping selectByJobIdVnCode(SelectJobIdMappingDTO selectJobIdMappingDTO);

    List<VngatewayJobidMapping> selectByJobId(String jobId);

    int deleteAll();

}