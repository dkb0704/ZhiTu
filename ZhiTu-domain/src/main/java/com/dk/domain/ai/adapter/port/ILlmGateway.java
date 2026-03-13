package com.dk.domain.ai.adapter.port;

/**
 * 大模型网关端口：领域层定义接口，基础设施层提供具体调用实现。
 * 兼容 OpenAI 协议，可通过配置切换 Ollama / DeepSeek / 其他 OpenAI 兼容服务。
 */
public interface ILlmGateway {

    /**
     * 发起一次对话请求。
     *
     * @param systemPrompt 系统提示词，用于约束模型行为和输出格式
     * @param userMessage  用户消息，即本次具体任务内容
     * @return 模型原始输出文本（通常为 JSON 字符串，由调用方自行解析）
     */
    String chat(String systemPrompt, String userMessage);

    /**
     * 发起一次对话请求，可指定温度。
     *
     * @param systemPrompt 系统提示词
     * @param userMessage  用户消息
     * @param temperature  采样温度（0.0 ~ 2.0），越低越确定性，越高越有创造性
     * @return 模型原始输出文本
     */
    String chat(String systemPrompt, String userMessage, float temperature);
}
