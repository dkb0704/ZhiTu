package com.dk.domain.ai.adapter.repository;

import com.dk.domain.ai.model.entity.JobProfileEntity;

import java.util.List;

/**
 * 岗位画像仓储接口。
 */
public interface IJobProfileRepository {

    void save(JobProfileEntity entity);

    void update(JobProfileEntity entity);

    JobProfileEntity findById(Long id);

    JobProfileEntity findByJobTitle(String jobTitle);

    List<JobProfileEntity> findAll();

    int count();
}
