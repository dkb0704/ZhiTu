package com.dk.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户职业生涯实体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCareerEntity {

    /** 主键 */
    private Long id;
    /** 关联用户 ID */
    private Long userId;
    /** 目标行业 */
    private String targetIndustry;
    /** 目标岗位 */
    private String targetPosition;
    /** 学习目标及进度 JSON，如 [{"name":"Java","progress":60}] */
    private String learningGoals;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
