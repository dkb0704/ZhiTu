package com.dk.domain.job.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 职业线路实体。
 *
 * 表示一条完整的职业发展线路，一般对应 career_track 表中的一条记录，
 * 内部包含从入门到高阶的若干岗位节点。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerTrackEntity {
    /** 线路编码，例如 product / tech / operation */
    private String id;
    /** 线路名称，例如「产品线」「技术线」 */
    private String name;
    /** 该线路下按顺序排列的岗位节点列表 */
    private List<CareerNodeEntity> nodes;
}
