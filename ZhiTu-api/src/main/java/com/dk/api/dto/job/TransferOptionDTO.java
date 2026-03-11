package com.dk.api.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转岗建议项 DTO。
 *
 * 用于 /job/career-map 接口中，描述某个节点可转去的目标岗位信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferOptionDTO {
    /** 目标岗位名称，例如「运营经理」「项目经理」 */
    private String title;
    /** 转岗特点或理由的简短文案，例如「从幕后到台前」 */
    private String desc;
}
