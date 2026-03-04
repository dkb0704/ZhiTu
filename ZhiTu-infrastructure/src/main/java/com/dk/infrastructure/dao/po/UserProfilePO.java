package com.dk.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户个人资料 PO，对应 user_profile 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfilePO {

    /** 主键 */
    private Long id;
    /** 关联用户 ID */
    private Long userId;
    /** 专业 */
    private String major;
    /** 年级 */
    private String grade;
    /** 意向城市 JSON */
    private String baseCities;
    /** 意向岗位 */
    private String targetPosition;
    /** 绩点 */
    private String gpa;
    /** 政治面貌 */
    private String politicalStatus;
    /** 荣誉及证书 JSON */
    private String honors;
    /** 项目经历 JSON */
    private String projects;
    /** 校园经历 JSON */
    private String campus;
    /** 技能 JSON */
    private String skills;
    /** 语言能力 JSON */
    private String languages;
    /** 自我评价 */
    private String selfEvaluation;
    /** 实习经历 JSON */
    private String internships;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
