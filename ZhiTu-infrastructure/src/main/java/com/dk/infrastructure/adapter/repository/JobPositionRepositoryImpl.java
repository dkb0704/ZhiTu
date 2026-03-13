package com.dk.infrastructure.adapter.repository;

import com.dk.domain.job.adapter.repository.IJobPositionRepository;
import com.dk.domain.job.model.entity.JobPositionEntity;
import com.dk.infrastructure.dao.IJobPositionDao;
import com.dk.infrastructure.dao.po.JobPositionPO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位仓储实现。
 */
@Repository
public class JobPositionRepositoryImpl implements IJobPositionRepository {

    private final IJobPositionDao jobDao;

    public JobPositionRepositoryImpl(IJobPositionDao jobDao) {
        this.jobDao = jobDao;
    }

    @Override
    public JobPositionEntity findById(Long id) {
        if (id == null)
            return null;
        JobPositionPO po = jobDao.selectById(id);
        return po == null ? null : toEntity(po);
    }

    @Override
    public List<JobPositionEntity> findByCondition(String title, String location, String industry,
            Integer salaryMin, Integer salaryMax,
            int offset, int limit) {
        List<JobPositionPO> list = jobDao.selectByCondition(title, location, industry, salaryMin, salaryMax, offset,
                limit);
        return list.stream().map(JobPositionRepositoryImpl::toEntity).collect(Collectors.toList());
    }

    @Override
    public int countByCondition(String title, String location, String industry,
            Integer salaryMin, Integer salaryMax) {
        return jobDao.countByCondition(title, location, industry, salaryMin, salaryMax);
    }

    private static JobPositionEntity toEntity(JobPositionPO po) {
        return JobPositionEntity.builder()
                .id(po.getId())
                .title(po.getTitle())
                .location(po.getLocation())
                .salaryRange(po.getSalaryRange())
                .salaryMin(po.getSalaryMin())
                .salaryMax(po.getSalaryMax())
                .companyName(po.getCompanyName())
                .industry(po.getIndustry())
                .companySize(po.getCompanySize())
                .companyType(po.getCompanyType())
                .jobCode(po.getJobCode())
                .description(po.getDescription())
                .updateDate(po.getUpdateDate())
                .companyDesc(po.getCompanyDesc())
                .sourceUrl(po.getSourceUrl())
                .jobDomain(po.getJobDomain())
                .jobCategory(po.getJobCategory())
                .createTime(po.getCreateTime())
                .build();
    }
}
