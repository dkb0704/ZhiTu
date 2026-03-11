package com.dk.infrastructure.gateway.llm;

import com.alibaba.fastjson2.JSON;
import com.dk.domain.llm.adapter.port.ILlmService;
import com.dk.domain.llm.model.valobj.LlmRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * ILlmService 的基础实现。
 *
 * 该实现负责把统一的 LLM 请求 DTO 转成 OpenAI 兼容格式，
 * 然后发送给具体的推理服务。
 *
 * 这样做的好处是：
 * 1. 可以兼容本地部署模型服务
 * 2. 也可以兼容云端 OpenAI 兼容接口
 * 3. 上层业务不需要关心底层模型提供方是谁
 */
@Slf4j
@Service
public class LlmServiceImpl implements ILlmService {

    private final RestTemplate restTemplate;

    @Value("${llm.endpoint:http://localhost:8081/v1/chat/completions}")
    private String llmEndpoint;

    @Value("${llm.api-key:}")
    private String apiKey;

    @Value("${llm.model:qwen}")
    private String modelName;

    @Value("${llm.prompt-base-path:prompts/llm}")
    private String promptBasePath;

    public LlmServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T infer(LlmRequestVO request, Class<T> responseType) {
        String systemPrompt = loadSystemPrompt(request.getTask());
        String userMessage = JSON.toJSONString(request.getPayload());

        // 构造 OpenAI 兼容格式（vLLM / Ollama / 云端 API 均兼容）
        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", modelName);
        bodyMap.put("messages", Arrays.asList(systemMsg, userMsg));
        bodyMap.put("temperature", 0.1);
        bodyMap.put("max_tokens", 2048);

        String body = JSON.toJSONString(bodyMap);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (apiKey != null && !apiKey.isEmpty()) {
            headers.set("Authorization", "Bearer " + apiKey);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        log.debug("LLM request task={}, endpoint={}", request.getTask(), llmEndpoint);

        ResponseEntity<String> resp = restTemplate.exchange(
                llmEndpoint, HttpMethod.POST, entity, String.class);

        // 从 OpenAI 兼容响应中提取 content
        String rawJson = resp.getBody();
        String content = extractContentFromResponse(rawJson);

        log.debug("LLM response content={}", content);

        return JSON.parseObject(content, responseType);
    }

    /**
     * 从 OpenAI 兼容格式响应中提取 assistant message content。
     */
    private String extractContentFromResponse(String rawJson) {
        com.alibaba.fastjson2.JSONObject root = JSON.parseObject(rawJson);
        return root.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    /**
     * 从 classpath 资源文件中加载系统提示词。
     *
     * 文件路径规则：
     * prompts/llm/{task}.txt
     * 例如：
     * prompts/llm/job_cleaning.txt
     */
    private String loadSystemPrompt(String task) {
        String resourcePath = String.format("%s/%s.txt", promptBasePath, task);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("LLM prompt file not found: " + resourcePath);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load LLM prompt: " + resourcePath, e);
        }
    }
}
