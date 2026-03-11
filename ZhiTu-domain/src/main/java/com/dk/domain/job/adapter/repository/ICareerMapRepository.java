package com.dk.domain.job.adapter.repository;

import com.dk.domain.job.model.entity.CareerTrackEntity;

import java.util.List;

/**
 * 岗位图谱仓储端口。
 */
public interface ICareerMapRepository {

    /** 查询完整图谱（所有线路 + 节点 + 转岗建议）。 */
    List<CareerTrackEntity> getFullMap();
}
