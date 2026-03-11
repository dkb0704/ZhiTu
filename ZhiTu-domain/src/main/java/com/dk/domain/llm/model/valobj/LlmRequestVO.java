package com.dk.domain.llm.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 统一的大模型请求值对象。
 *
 * 该对象定义在 domain 模块中，供领域服务和基础设施实现共同使用，
 * 避免 domain 反向依赖 api 模块。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlmRequestVO {

    /**
     * 任务类型，例如 job_cleaning、job_profile、user_profile、match。
     */
    private String task;

    /**
     * 请求协议版本，例如 v1。
     */
    private String version;

    /**
     * 请求上下文，例如语言、租户标识。
     */
    private ContextVO context;

    /**
     * 业务负载，包含岗位信息和用户信息。
     */
    private PayloadVO payload;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextVO {
        /**
         * 语言，例如 zh / en。
         */
        private String lang;

        /**
         * 业务标识，例如 competition-a13。
         */
        private String tenant;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PayloadVO {
        /**
         * 岗位信息。
         */
        private JobVO job;

        /**
         * 用户信息。
         */
        private UserVO user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobVO {
        /**
         * 岗位 ID。
         */
        private String id;

        /**
         * 岗位名称。
         */
        private String title;

        /**
         * 公司名称。
         */
        private String company;

        /**
         * 行业名称。
         */
        private String industry;

        /**
         * 工作地点。
         */
        private String location;

        /**
         * 岗位 JD 文本。
         */
        private String jdText;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserVO {
        /**
         * 用户 ID。
         */
        private String id;

        /**
         * 用户基础信息。
         */
        private BasicVO basic;

        /**
         * 简历文本。
         */
        private String resumeText;

        /**
         * 问卷答案。
         */
        private Map<String, String> questionnaire;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicVO {
        /**
         * 年龄。
         */
        private Integer age;

        /**
         * 性别。
         */
        private String gender;

        /**
         * 专业。
         */
        private String major;

        /**
         * 学历。
         */
        private String degree;

        /**
         * 年级。
         */
        private String grade;
    }
}
