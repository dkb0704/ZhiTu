package com.dk.api.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 岗位搜索请求 DTO。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchRequestDTO {

    /** 岗位名称关键字 */
    private String title;
    /** 地点关键字 */
    private String location;
    /** 行业关键字 */
    private String industry;
    /** 薪资下限（元） */
    private Integer salaryMin;
    /** 薪资上限（元） */
    private Integer salaryMax;
    /** 页码（从 1 开始，默认 1） */
    private Integer page;
    /** 每页条数（默认 10） */
    private Integer size;
}
