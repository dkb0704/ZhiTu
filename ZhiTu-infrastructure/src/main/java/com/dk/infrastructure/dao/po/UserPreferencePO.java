package com.dk.infrastructure.dao.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户偏好与测试 PO，对应 user_preference 表。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencePO {

    /** 主键 */
    private Long id;
    /** 关联用户 ID */
    private Long userId;
    /** 求职偏好权重 JSON */
    private String jobPreferences;
    /** 软实力自评 JSON */
    private String softSkills;
    /** MBTI 结果 */
    private String mbti;
    /** 霍兰德结果 */
    private String holland;
    /** BIG FIVE 结果 */
    private String bigFive;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
