package com.dk.trigger.http;

import com.dk.api.response.Response;
import com.dk.domain.job.model.entity.JobPositionEntity;
import com.dk.domain.job.service.JobCleaningService;
import com.dk.domain.job.service.JobDomainService;
import com.dk.domain.llm.model.valobj.JobCleaningResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * LLM 相关接口控制器。
 *
 * 当前先提供岗位清洗能力，后续可以继续扩展：
 * 1. 岗位画像抽取
 * 2. 用户画像抽取
 * 3. 人岗匹配评分
 */
@Slf4j
@RestController
@RequestMapping("/llm")
public class LlmController {

    @Resource
    private JobCleaningService jobCleaningService;

    @Resource
    private JobDomainService jobDomainService;

    /**
     * 批量清洗岗位：判断是否为计算机相关 + JD 质量评估。
     *
     * @param page 页码（从 1 开始）
     * @param size 每页条数（默认 50）
     * @return 每条岗位的清洗结果
     */
    @GetMapping("/job-cleaning")
    public Response<Map<String, Object>> batchCleanJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {

        List<JobPositionEntity> jobs = jobDomainService.searchJobs(
                null, null, null, null, null, page, size);

        List<Map<String, Object>> results = new ArrayList<>();
        int csCount = 0;
        for (JobPositionEntity job : jobs) {
            JobCleaningResultVO result = jobCleaningService.cleanSingle(job);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", job.getId());
            row.put("title", job.getTitle());
            row.put("company", job.getCompanyName());
            row.put("industry", job.getIndustry());
            row.put("csJob", result.isCsJob());
            row.put("jobDomain", result.getJobDomain());
            row.put("jobCategory", result.getJobCategory());
            row.put("quality", result.getQuality());
            row.put("reason", result.getReason());
            results.add(row);
            if (result.isCsJob()) csCount++;
        }

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("total", results.size());
        resp.put("csJobCount", csCount);
        resp.put("page", page);
        resp.put("size", size);
        resp.put("results", results);
        return Response.success(resp);
    }

    /**
     * 单条岗位清洗。
     */
    @GetMapping("/job-cleaning/{id}")
    public Response<JobCleaningResultVO> cleanSingleJob(@PathVariable Long id) {
        JobPositionEntity job = jobDomainService.getJobDetail(id);
        return Response.success(jobCleaningService.cleanSingle(job));
    }
}
