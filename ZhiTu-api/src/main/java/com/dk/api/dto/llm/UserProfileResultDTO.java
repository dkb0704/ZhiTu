package com.dk.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户画像抽取结果 DTO。
 *
 * 该结构用于承接模型从学生基础信息、简历、问卷等内容中抽取出的画像结果，
 * 便于后续与岗位画像做结构化匹配。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResultDTO {

    /**
     * 用户 ID，一般对应 user.id。
     */
    private String userId;

    /**
     * 用户当前已具备的技能集合。
     */
    private List<String> skills;

    /**
     * 用户潜在适合的发展方向，例如后端开发、测试开发、数据分析等。
     */
    private List<String> potentialTracks;

    /**
     * 用户的软特质，例如学习能力强、执行力好、沟通能力一般等。
     */
    private List<String> softTraits;

    /**
     * 用户当前的风险点或短板，例如项目经验不足、算法基础弱等。
     */
    private List<String> riskPoints;

    /**
     * 当前阶段适合投递的岗位类别。
     */
    private List<String> suitableJobCategories;
}
