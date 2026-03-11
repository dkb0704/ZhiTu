package com.dk.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 统一的大模型请求 DTO。
 *
 * 所有与 LLM 的交互都通过该结构封装，例如：
 * 1. 岗位清洗
 * 2. 岗位画像抽取
 * 3. 用户画像抽取
 * 4. 人岗匹配评分
 *
 * 这样做的目的是把“业务侧输入”与“模型调用实现”解耦。
 * 后续如果切换本地模型、云端 API，或者扩展更多字段，都不需要改动上层业务协议。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlmRequestDTO {

    /**
     * 任务类型。
     *
     * 可选值示例：
     * job_cleaning：岗位清洗 / 是否为计算机相关岗位
     * job_profile：岗位画像抽取
     * user_profile：学生画像抽取
     * match：人岗匹配评分
     */
    private String task;

    /**
     * 请求协议版本，便于后续升级字段而保持向后兼容（例如 v1 / v2）。
     */
    private String version;

    /**
     * 调用上下文信息，例如语言、租户、比赛编号等。
     */
    private ContextDTO context;

    /**
     * 具体业务负载：包含岗位信息、用户信息等。
     */
    private PayloadDTO payload;

    /**
     * 上下文信息 DTO。
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextDTO {

        /**
         * 当前对话/任务的主语言，例如：zh / en。
         */
        private String lang;

        /**
         * 租户或业务标识，例如：competition-a13，用于多租户或环境区分。
         */
        private String tenant;
    }

    /**
     * 业务负载 DTO。
     *
     * 目前包含岗位对象和用户对象。
     * 后续如果需要引入测评结果、历史行为、推荐上下文等信息，也可以继续在这里扩展。
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayloadDTO {

        /**
         * 岗位相关信息（可为空，取决于 task）。
         */
        private JobDTO job;

        /**
         * 学生/用户相关信息（可为空，取决于 task）。
         */
        private UserDTO user;
    }

    /**
     * 岗位信息 DTO，用于提供给 LLM 的上下文。
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobDTO {

        /**
         * 岗位唯一标识，一般对应 job_position.id。
         */
        private String id;

        /**
         * 岗位名称，如“Java开发工程师”“前端开发”等。
         */
        private String title;

        /**
         * 公司名称。
         */
        private String company;

        /**
         * 所属行业，例如“计算机软件”“互联网”等。
         */
        private String industry;

        /**
         * 工作地点，例如“上海-浦东新区”。
         */
        private String location;

        /**
         * 完整 JD 文本（可由原始 JD + 爬虫补充 JD 拼接而成）。
         */
        private String jdText;
    }

    /**
     * 用户/学生信息 DTO，用于画像和匹配。
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {

        /**
         * 用户唯一标识，一般对应 user.id。
         */
        private String id;

        /**
         * 基础信息，如年龄、性别、专业、年级等。
         */
        private BasicDTO basic;

        /**
         * 简历原文或简历关键信息的拼接结果。
         */
        private String resumeText;

        /**
         * 问卷答案，key 为问题维度（如“兴趣方向”“职业价值观”），value 为用户作答内容。
         */
        private Map<String, String> questionnaire;
    }

    /**
     * 学生基础信息 DTO。
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicDTO {

        /**
         * 年龄。
         */
        private Integer age;

        /**
         * 性别，自由文本（可选：男 / 女 / 其他 / 未填写）。
         */
        private String gender;

        /**
         * 所学专业，例如“计算机科学与技术”“软件工程”等。
         */
        private String major;

        /**
         * 学历，例如“本科”“专科”“硕士”等。
         */
        private String degree;

        /**
         * 年级，例如“大一”“大三”“研一”等。
         */
        private String grade;
    }
}
