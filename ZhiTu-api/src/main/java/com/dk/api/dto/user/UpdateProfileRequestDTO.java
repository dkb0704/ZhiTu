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
    /** 语言能力 JSON */
    private String languages;
    /** 自我评价 */
    private String selfEvaluation;
    /** 实习经历 JSON */
    private String internships;
}
