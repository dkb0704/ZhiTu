package com.dk.infrastructure.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dk.domain.ai.adapter.port.ILlmGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 大模型网关实现，走 OpenAI 兼容的 /v1/chat/completions 协议。
 * 通过 application-dev.yml 中的 llm.* 配置切换不同的模型服务。
 */
@Slf4j
@Component
public class LlmGatewayImpl implements ILlmGateway {

    @Resource
    private RestTemplate restTemplate;

    @Value("${llm.endpoint}")
    private String endpoint;

    @Value("${llm.api-key:}")
    private String apiKey;

    @Value("${llm.model}")
    private String model;

    private static final float DEFAULT_TEMPERATURE = 0.3f;

    @Override
    public String chat(String systemPrompt, String userMessage) {
        return chat(systemPrompt, userMessage, DEFAULT_TEMPERATURE);
    }

    @Override
    public String chat(String systemPrompt, String userMessage, float temperature) {
        JSONObject requestBody = buildRequestBody(systemPrompt, userMessage, temperature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.set("Authorization", "Bearer " + apiKey);
        }

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toJSONString(), headers);

        try {
            log.info("LLM 请求 model={}, userMessage 长度={}", model, userMessage.length());
            ResponseEntity<String> response = restTemplate.exchange(
                    endpoint, HttpMethod.POST, entity, String.class);

            String raw = extractContent(response.getBody());
            log.info("LLM 响应长度={}", raw.length());
            return normalizeJsonContent(raw);
        } catch (Exception e) {
            log.error("LLM 调用失败: {}", e.getMessage(), e);
            throw new RuntimeException("大模型调用失败: " + e.getMessage(), e);
        }
    }

    private JSONObject buildRequestBody(String systemPrompt, String userMessage, float temperature) {
        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("temperature", temperature);

        JSONObject sysMsg = new JSONObject();
        sysMsg.put("role", "system");
        sysMsg.put("content", systemPrompt);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);

        body.put("messages", Arrays.asList(sysMsg, userMsg));
        return body;
    }

    /**
     * 从 OpenAI 兼容的响应结构中提取 assistant 回复内容。
     */
    private String extractContent(String responseBody) {
        JSONObject resp = JSON.parseObject(responseBody);
        JSONArray choices = resp.getJSONArray("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("LLM 返回 choices 为空");
        }
        return choices.getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    /**
     * 归一化模型输出：去除 Markdown 代码块包裹，提取纯 JSON。
     */
    private String normalizeJsonContent(String raw) {
        if (raw == null) return "";
        String trimmed = raw.trim();

        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            if (firstNewline > 0) {
                trimmed = trimmed.substring(firstNewline + 1);
            }
            if (trimmed.endsWith("```")) {
                trimmed = trimmed.substring(0, trimmed.length() - 3);
            }
            trimmed = trimmed.trim();
        }

        int jsonStart = -1;
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '{' || c == '[') {
                jsonStart = i;
                break;
            }
        }
        if (jsonStart > 0) {
            trimmed = trimmed.substring(jsonStart);
        }

        return trimmed;
    }
}
