package com.dk.domain.job.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转岗建议项实体。
 *
 * 描述从当前岗位节点可以转去的一个目标岗位及其简要说明，
 * 与前端的 TransferOption 结构保持一致。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOptionEntity {
    /** 目标岗位名称 */
    private String title;
    /** 转岗特点或理由的简要说明 */
    private String desc;
}
