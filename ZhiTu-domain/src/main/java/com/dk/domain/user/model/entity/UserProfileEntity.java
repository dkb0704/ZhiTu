package com.dk.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户个人资料实体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {

    /** 主键 */
    private Long id;
    /** 关联用户 ID */
    private Long userId;
    /** 专业（必填） */
    private String major;
    /** 年级（如 大一/大二/研一） */
    private String grade;
    /** 意向城市 JSON 数组，最多 3 个 */
    private String baseCities;
    /** 意向岗位 */
    private String targetPosition;
    /** 绩点 */
    private String gpa;
    /** 政治面貌 */
    private String politicalStatus;
    /** 荣誉及证书 JSON 数组 */
    private String honors;
    /** 项目经历 JSON 数组 */
    private String projects;
    /** 校园经历 JSON 数组 */
    private String campus;
    /** 技能 JSON 数组 */
    private String skills;
    /** 语言能力 JSON 数组 */
    private String languages;
    /** 自我评价 */
    private String selfEvaluation;
    /** 实习经历 JSON 数组 */
    private String internships;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
