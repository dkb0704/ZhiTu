package com.dk.domain.job.service;

import com.dk.domain.job.model.entity.JobPositionEntity;
import com.dk.domain.llm.adapter.port.ILlmService;
import com.dk.domain.llm.model.valobj.JobCleaningResultVO;
import com.dk.domain.llm.model.valobj.LlmRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 岗位清洗服务。
 *
 * 该服务负责对岗位进行初步归类和质量判断，主要用于：
 * 1. 从海量岗位中筛出计算机相关岗位
 * 2. 识别 JD 是否足够完整，能否继续用于画像抽取
 * 3. 为后续的人岗匹配、推荐和数据清洗提供结构化标签
 *
 * 当前实现采用“两段式”策略：
 * 先通过规则做粗筛，再对灰区岗位调用 LLM 做精细判断。
 */
@Slf4j
@Service
public class JobCleaningService {

    private final ILlmService llmService;

    private static final List<String> CS_TITLE_KEYWORDS = List.of(
            "Java", "java", "Python", "python", "C++", "C#", "Go", "PHP", "前端", "后端", "全栈",
            "测试", "运维", "DevOps", "算法", "数据", "嵌入式", "Android", "iOS", "Web", "web",
            "架构", "DBA", "开发", "软件", "程序", "IT", "信息", "人工智能", "机器学习", "大数据"
    );

    private static final List<String> NON_CS_TITLE_KEYWORDS = List.of(
            "司机", "厨师", "保安", "保洁", "服务员", "护士", "医生", "教师", "会计",
            "销售", "行政", "文员", "客服", "美工", "设计师", "律师", "翻译"
    );

    public JobCleaningService(ILlmService llmService) {
        this.llmService = llmService;
    }

    /**
     * 对一批岗位做清洗：先规则判断，灰区才调 LLM。
     *
     * @param jobs 待清洗的岗位列表
     * @return 每条岗位的清洗结果
     */
    public List<JobCleaningResultVO> cleanBatch(List<JobPositionEntity> jobs) {
        List<JobCleaningResultVO> results = new ArrayList<>();
        for (JobPositionEntity job : jobs) {
            results.add(cleanSingle(job));
        }
        return results;
    }

    /**
     * 对单条岗位做清洗判定。
     */
    public JobCleaningResultVO cleanSingle(JobPositionEntity job) {
        // 1) 规则：明确非 CS
        if (matchesAny(job.getTitle(), NON_CS_TITLE_KEYWORDS)) {
            return JobCleaningResultVO.builder()
                    .csJob(false)
                    .jobDomain("非计算机")
                    .reason("岗位名命中非计算机关键词")
                    .build();
        }

        // 2) 规则：明确 CS
        if (matchesAny(job.getTitle(), CS_TITLE_KEYWORDS)) {
            return JobCleaningResultVO.builder()
                    .csJob(true)
                    .jobDomain("计算机/信息技术")
                    .reason("岗位名命中计算机关键词")
                    .build();
        }

        // 3) 灰区：调 LLM 精判
        return cleanViaLlm(job);
    }

    private JobCleaningResultVO cleanViaLlm(JobPositionEntity job) {
        try {
            LlmRequestVO request = LlmRequestVO.builder()
                    .task("job_cleaning")
                    .version("v1")
                    .context(LlmRequestVO.ContextVO.builder()
                            .lang("zh")
                            .tenant("competition-a13")
                            .build())
                    .payload(LlmRequestVO.PayloadVO.builder()
                            .job(LlmRequestVO.JobVO.builder()
                                    .id(String.valueOf(job.getId()))
                                    .title(job.getTitle())
                                    .company(job.getCompanyName())
                                    .industry(job.getIndustry())
                                    .location(job.getLocation())
                                    .jdText(job.getDescription())
                                    .build())
                            .build())
                    .build();

            return llmService.infer(request, JobCleaningResultVO.class);
        } catch (Exception e) {
            log.warn("LLM clean failed for job id={}, title={}, err={}", job.getId(), job.getTitle(), e.getMessage());
            return JobCleaningResultVO.builder()
                    .csJob(false)
                    .reason("LLM 调用失败: " + e.getMessage())
                    .build();
        }
    }

    private boolean matchesAny(String text, List<String> keywords) {
        if (text == null) return false;
        for (String kw : keywords) {
            if (text.contains(kw)) return true;
        }
        return false;
    }
}
