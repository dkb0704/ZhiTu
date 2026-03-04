package com.dk.infrastructure.dao;

import com.dk.infrastructure.dao.po.JobPositionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位 DAO。
 */
@Mapper
public interface IJobPositionDao {

    /** 按 ID 查询 */
    JobPositionPO selectById(@Param("id") Long id);

    /** 条件分页查询 */
    List<JobPositionPO> selectByCondition(@Param("title") String title,
            @Param("location") String location,
            @Param("industry") String industry,
            @Param("salaryMin") Integer salaryMin,
            @Param("salaryMax") Integer salaryMax,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /** 条件计数 */
    int countByCondition(@Param("title") String title,
            @Param("location") String location,
            @Param("industry") String industry,
            @Param("salaryMin") Integer salaryMin,
            @Param("salaryMax") Integer salaryMax);
}
