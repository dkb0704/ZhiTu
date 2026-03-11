package com.dk.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 人岗匹配结果 DTO。
 *
 * 用于保存模型对“学生画像”和“岗位画像”的匹配结果，
 * 既包含总分，也包含分维度评分和解释建议。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultDTO {

    /**
     * 用户 ID。
     */
    private String userId;

    /**
     * 岗位 ID。
     */
    private String jobId;

    /**
     * 总体匹配分，建议使用 0 到 1 的小数表示。
     */
    private Double score;

    /**
     * 各维度评分，例如技能匹配、经验匹配、兴趣匹配、软技能匹配等。
     */
    private Map<String, Double> dimensionScores;

    /**
     * 对匹配结果的简要总结。
     */
    private String summary;

    /**
     * 给用户的提升建议或投递建议。
     */
    private List<String> advice;
}
