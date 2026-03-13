package com.dk.infrastructure.dao;

import com.dk.infrastructure.dao.po.JobProfilePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IJobProfileDao {

    void insert(JobProfilePO po);

    void update(JobProfilePO po);

    JobProfilePO selectById(@Param("id") Long id);

    JobProfilePO selectByJobTitle(@Param("jobTitle") String jobTitle);

    List<JobProfilePO> selectAll();

    int count();
}
