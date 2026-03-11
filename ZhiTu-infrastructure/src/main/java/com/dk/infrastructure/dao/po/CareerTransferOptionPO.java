package com.dk.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 转岗建议 PO，对应 career_transfer_option 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerTransferOptionPO {
    /** 主键 */
    private Long id;
    /** 来源节点 ID（career_node.id） */
    private Long nodeId;
    /** 目标岗位名称 */
    private String targetTitle;
    /** 目标岗位说明文案 */
    private String targetDesc;
    /** 排序值，控制展示顺序 */
    private Integer sortOrder;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
