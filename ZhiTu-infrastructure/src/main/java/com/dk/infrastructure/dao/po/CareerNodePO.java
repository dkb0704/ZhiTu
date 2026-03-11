package com.dk.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 职业节点 PO，对应 career_node 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerNodePO {
    /** 主键 */
    private Long id;
    /** 所属职业线路 ID */
    private Long trackId;
    /** 节点编码 */
    private String code;
    /** 节点名称（岗位名称） */
    private String title;
    /** 在线路中的顺序（升岗顺序） */
    private Integer sortOrder;
    /** 是否存在转岗选项 0 否 1 是 */
    private Integer hasTransfer;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
