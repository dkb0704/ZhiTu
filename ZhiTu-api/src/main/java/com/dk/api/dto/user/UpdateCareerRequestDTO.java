package com.dk.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新职业生涯请求 DTO。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCareerRequestDTO {

    /** 目标行业 */
    private String targetIndustry;
    /** 目标岗位 */
    private String targetPosition;
    /** 学习目标及进度 JSON */
    private String learningGoals;
}
