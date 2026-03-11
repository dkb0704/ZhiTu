package com.dk.domain.job.service;

import com.dk.domain.job.adapter.repository.ICareerMapRepository;
import com.dk.domain.job.adapter.repository.IJobPositionRepository;
import com.dk.domain.job.model.entity.CareerTrackEntity;
import com.dk.domain.job.model.entity.JobPositionEntity;
import com.dk.types.exception.AppException;
import com.dk.types.enums.ResponseCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位领域服务。
 */
@Service
public class JobDomainService {

    private final IJobPositionRepository jobPositionRepository;

    @Resource
    private ICareerMapRepository careerMapRepository;

    public JobDomainService(IJobPositionRepository jobPositionRepository) {
        this.jobPositionRepository = jobPositionRepository;
    }

    /**
     * 岗位图谱：职业线 + 节点 + 转岗关系（来自 career_track / career_node / career_transfer_option）。
     */
    public List<CareerTrackEntity> getCareerMap() {
        return careerMapRepository.getFullMap();
    }

    /**
     * 分页查询岗位，支持多维度筛选。
     *
     * @param title     岗位名称关键字
     * @param location  地点关键字
     * @param industry  行业关键字
     * @param salaryMin 薪资下限
     * @param salaryMax 薪资上限
     * @param page      页码（从 1 开始）
     * @param size      每页条数
     * @return 岗位列表
     */
    public List<JobPositionEntity> searchJobs(String title, String location, String industry,
            Integer salaryMin, Integer salaryMax,
            int page, int size) {
        int offset = (Math.max(page, 1) - 1) * size;
        return jobPositionRepository.findByCondition(title, location, industry, salaryMin, salaryMax, offset, size);
    }

    /**
     * 统计满足条件的岗位总数。
     */
    public int countJobs(String title, String location, String industry,
            Integer salaryMin, Integer salaryMax) {
        return jobPositionRepository.countByCondition(title, location, industry, salaryMin, salaryMax);
    }

    /**
     * 查询岗位详情。
     */
    public JobPositionEntity getJobDetail(Long id) {
        JobPositionEntity job = jobPositionRepository.findById(id);
        if (job == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "岗位不存在");
        }
        return job;
    }
}
