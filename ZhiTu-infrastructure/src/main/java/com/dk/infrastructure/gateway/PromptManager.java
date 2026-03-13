package com.dk.infrastructure.gateway;

import com.dk.domain.ai.adapter.port.IPromptLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Prompt 模板管理器。
 * 从 classpath:prompts/ 下加载 .txt 文件，内存缓存避免重复 IO。
 */
@Slf4j
@Component
public class PromptManager implements IPromptLoader {

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    /**
     * 加载指定名称的 prompt 模板。
     *
     * @param name 模板名称（不含后缀），如 "job-profile" 对应 prompts/job-profile.txt
     * @return prompt 文本内容
     */
    public String load(String name) {
        return cache.computeIfAbsent(name, this::doLoad);
    }

    private String doLoad(String name) {
        String path = "prompts/" + name + ".txt";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Prompt 模板不存在: " + path);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            String content = out.toString(StandardCharsets.UTF_8.name());
            log.info("加载 prompt 模板: {} ({}字符)", path, content.length());
            return content;
        } catch (Exception e) {
            throw new RuntimeException("加载 prompt 失败: " + path, e);
        }
    }
}
