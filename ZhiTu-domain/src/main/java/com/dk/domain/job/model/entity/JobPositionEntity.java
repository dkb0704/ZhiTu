package com.dk.domain.job.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 岗位实体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPositionEntity {

    /** 主键 */
    private Long id;
    /** 岗位名称 */
    private String title;
    /** 工作地点 */
    private String location;
    /** 薪资范围原始文本 */
    private String salaryRange;
    /** 薪资下限（元） */
    private Integer salaryMin;
    /** 薪资上限（元） */
    private Integer salaryMax;
    /** 公司名称 */
    private String companyName;
    /** 所属行业 */
    private String industry;
    /** 公司规模 */
    private String companySize;
    /** 公司类型/融资阶段 */
    private String companyType;
    /** 岗位编码 */
    private String jobCode;
    /** 岗位详情 JD */
    private String description;
    /** 原始更新日期 */
    private String updateDate;
    /** 公司详情 */
    private String companyDesc;
    /** 来源地址 */
    private String sourceUrl;
    /** 入库时间 */
    private Date createTime;
}
