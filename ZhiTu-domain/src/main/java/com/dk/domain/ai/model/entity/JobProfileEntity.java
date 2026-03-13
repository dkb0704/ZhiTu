package com.dk.domain.ai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 岗位画像实体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobProfileEntity {

    private Long id;
    private String jobTitle;
    private String jobFamily;
    private String description;
    /** 专业技能 JSON 数组 */
    private String hardSkills;
    /** 软技能 JSON 数组 */
    private String softSkills;
    /** 证书要求 JSON 数组 */
    private String certRequirements;
    /** 能力模型 JSON */
    private String abilityModel;
    private String salaryRange;
    private String careerPath;
    /** 依据的 job_position id 列表 */
    private String sourceJobIds;
    private Date createTime;
    private Date updateTime;
}
