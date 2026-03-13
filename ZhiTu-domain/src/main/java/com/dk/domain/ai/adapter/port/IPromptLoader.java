package com.dk.domain.ai.adapter.port;

/**
 * Prompt 模板加载端口：领域层定义接口，基础设施层提供实现。
 */
public interface IPromptLoader {

    /**
     * 加载指定名称的 prompt 模板。
     *
     * @param name 模板名称（如 "job-profile"）
     * @return prompt 文本内容
     */
    String load(String name);
}
