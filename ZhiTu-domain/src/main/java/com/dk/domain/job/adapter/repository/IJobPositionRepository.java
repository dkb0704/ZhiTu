package com.dk.domain.job.adapter.repository;

import com.dk.domain.job.model.entity.JobPositionEntity;

import java.util.List;

/**
 * 岗位仓储端口。
 */
public interface IJobPositionRepository {

    /** 按 ID 查询岗位详情 */
    JobPositionEntity findById(Long id);

    /**
     * 分页查询岗位列表，支持筛选。
     *
     * @param title     岗位名称（模糊匹配，可为 null）
     * @param location  工作地点（模糊匹配，可为 null）
     * @param industry  行业（模糊匹配，可为 null）
     * @param salaryMin 最低薪资（可为 null）
     * @param salaryMax 最高薪资（可为 null）
     * @param offset    偏移量
     * @param limit     每页条数
     * @return 岗位列表
     */
    List<JobPositionEntity> findByCondition(String title, String location, String industry,
            Integer salaryMin, Integer salaryMax,
            int offset, int limit);

    /**
     * 统计满足筛选条件的总数。
     */
    int countByCondition(String title, String location, String industry,
            Integer salaryMin, Integer salaryMax);
}
