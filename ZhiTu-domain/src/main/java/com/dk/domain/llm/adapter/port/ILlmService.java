package com.dk.domain.llm.adapter.port;

import com.dk.domain.llm.model.valobj.LlmRequestVO;

/**
 * 大模型推理端口。
 *
 * 该接口定义在 domain 层，用于约束业务侧如何调用大模型能力。
 * 具体是接本地部署模型、vLLM、Ollama，还是外部 API，
 * 都由 infrastructure 层去实现，业务侧不关心底层细节。
 */
public interface ILlmService {

    /**
     * 发送推理请求，返回指定类型的结构化结果。
     *
     * @param request      统一的 LLM 请求体
     * @param responseType 期望的返回类型（如 JobCleaningResultDTO.class）
     */
    <T> T infer(LlmRequestVO request, Class<T> responseType);
}
