package com.dk.infrastructure.adapter.repository;

import com.dk.domain.ai.adapter.repository.IJobProfileRepository;
import com.dk.domain.ai.model.entity.JobProfileEntity;
import com.dk.infrastructure.dao.IJobProfileDao;
import com.dk.infrastructure.dao.po.JobProfilePO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JobProfileRepositoryImpl implements IJobProfileRepository {

    private final IJobProfileDao dao;

    public JobProfileRepositoryImpl(IJobProfileDao dao) {
        this.dao = dao;
    }

    @Override
    public void save(JobProfileEntity entity) {
        dao.insert(toPO(entity));
    }

    @Override
    public void update(JobProfileEntity entity) {
        dao.update(toPO(entity));
    }

    @Override
    public JobProfileEntity findById(Long id) {
        JobProfilePO po = dao.selectById(id);
        return po == null ? null : toEntity(po);
    }

    @Override
    public JobProfileEntity findByJobTitle(String jobTitle) {
        JobProfilePO po = dao.selectByJobTitle(jobTitle);
        return po == null ? null : toEntity(po);
    }

    @Override
    public List<JobProfileEntity> findAll() {
        return dao.selectAll().stream()
                .map(JobProfileRepositoryImpl::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return dao.count();
    }

    private static JobProfilePO toPO(JobProfileEntity e) {
        return JobProfilePO.builder()
                .id(e.getId())
                .jobTitle(e.getJobTitle())
                .jobFamily(e.getJobFamily())
                .description(e.getDescription())
                .hardSkills(e.getHardSkills())
                .softSkills(e.getSoftSkills())
                .certRequirements(e.getCertRequirements())
                .abilityModel(e.getAbilityModel())
                .salaryRange(e.getSalaryRange())
                .careerPath(e.getCareerPath())
                .sourceJobIds(e.getSourceJobIds())
                .createTime(e.getCreateTime())
                .updateTime(e.getUpdateTime())
                .build();
    }

    private static JobProfileEntity toEntity(JobProfilePO po) {
        return JobProfileEntity.builder()
                .id(po.getId())
                .jobTitle(po.getJobTitle())
                .jobFamily(po.getJobFamily())
                .description(po.getDescription())
                .hardSkills(po.getHardSkills())
                .softSkills(po.getSoftSkills())
                .certRequirements(po.getCertRequirements())
                .abilityModel(po.getAbilityModel())
                .salaryRange(po.getSalaryRange())
                .careerPath(po.getCareerPath())
                .sourceJobIds(po.getSourceJobIds())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .build();
    }
}
