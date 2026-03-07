package com.dk.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新个人资料请求 DTO（对应 user_profile 表）。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDTO {

    /** 专业 */
    private String major;
    /** 学历（最高学历，如 专科/本科/硕士/博士） */
    private String education;
    /** 教育背景 JSON 数组，如 [{"degree":"本科","school":"XX大学"},{"degree":"硕士","school":"YY大学"}] */
    private String educationBackground;
    /** 年级 */
    private String grade;
    /** 意向城市 JSON 数组 */
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

    /** 自我评价 */
    private String selfEvaluation;
    /** 实习经历 JSON */
    private String internships;
}
