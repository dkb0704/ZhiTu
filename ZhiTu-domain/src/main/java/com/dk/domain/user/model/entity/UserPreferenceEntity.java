package com.dk.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户偏好与测试实体。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceEntity {

    /** 主键 */
    private Long id;
    /** 关联用户 ID */
    private Long userId;
    /** 求职偏好权重 JSON，维度可动态扩展 */
    private String jobPreferences;
    /** 软实力自评 JSON，维度可动态扩展 */
    private String softSkills;
    /** MBTI 测试结果（如 INTJ） */
    private String mbti;
    /** 霍兰德职业兴趣测试结果（如 IAR） */
    private String holland;
    /** BIG FIVE 大五人格测试结果 */
    private String bigFive;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
