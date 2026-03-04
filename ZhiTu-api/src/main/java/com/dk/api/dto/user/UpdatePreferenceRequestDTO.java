package com.dk.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新偏好与测试请求 DTO。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePreferenceRequestDTO {

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
}
