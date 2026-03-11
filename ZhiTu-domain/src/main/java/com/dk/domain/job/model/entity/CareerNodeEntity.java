package com.dk.domain.job.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 岗位图谱节点实体。
 *
 * 表示图谱中的单个岗位节点，通常来源于 career_node 表，
 * 包含岗位名称以及是否支持转岗等信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerNodeEntity {
    /** 节点编码，例如 p1/p2/t3 */
    private String id;
    /** 节点展示名称，即岗位名称 */
    private String title;
    /** 是否存在可转岗选项 */
    private Boolean hasTransfer;
    /** 可转岗建议列表 */
    private List<TransferOptionEntity> transferOptions;
}
