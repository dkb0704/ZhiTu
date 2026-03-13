package com.dk.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 岗位画像 PO，对应 job_profile 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobProfilePO {

    private Long id;
    private String jobTitle;
    private String jobFamily;
    private String description;
    private String hardSkills;
    private String softSkills;
    private String certRequirements;
    private String abilityModel;
    private String salaryRange;
    private String careerPath;
    private String sourceJobIds;
    private Date createTime;
    private Date updateTime;
}
