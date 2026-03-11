package com.dk.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 职业线 PO，对应 career_track 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerTrackPO {
    /** 主键 */
    private Long id;
    /** 线路编码，例如 product / tech / operation */
    private String code;
    /** 线路名称 */
    private String name;
    /** 排序值，数值越小越靠前 */
    private Integer sortOrder;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
