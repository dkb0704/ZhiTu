package com.dk.trigger.http;

import com.dk.api.response.Response;
import com.dk.domain.ai.adapter.port.IPromptLoader;
import com.dk.domain.ai.model.entity.JobProfileEntity;
import com.dk.domain.ai.service.JobProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * AI 能力接口控制器。
 * 当前提供岗位画像生成能力，后续扩展学生画像、人岗匹配等。
 */
@Slf4j
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private JobProfileService jobProfileService;

    @Resource
    private IPromptLoader promptLoader;

    /**
     * 为指定岗位生成画像。
     */
    @PostMapping("/job-profile/generate")
    public Response<JobProfileEntity> generateJobProfile(@RequestParam String jobTitle) {
        String systemPrompt = promptLoader.load("job-profile");
        JobProfileEntity profile = jobProfileService.generateProfile(jobTitle, systemPrompt);
        return Response.success(profile);
    }

    /**
     * 批量生成所有预定义岗位的画像。
     */
    @PostMapping("/job-profile/batch")
    public Response<Map<String, Object>> batchGenerateJobProfiles() {
        String systemPrompt = promptLoader.load("job-profile");
        List<JobProfileEntity> results = jobProfileService.batchGenerate(systemPrompt);

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("total", results.size());
        resp.put("targetTitles", jobProfileService.getTargetJobTitles());
        resp.put("profiles", results);
        return Response.success(resp);
    }

    /**
     * 查询单个岗位画像。
     */
    @GetMapping("/job-profile/{id}")
    public Response<JobProfileEntity> getJobProfile(@PathVariable Long id) {
        JobProfileEntity profile = jobProfileService.getById(id);
        if (profile == null) {
            return Response.fail("岗位画像不存在");
        }
        return Response.success(profile);
    }

    /**
     * 查询所有已生成的岗位画像列表。
     */
    @GetMapping("/job-profile/list")
    public Response<List<JobProfileEntity>> listJobProfiles() {
        return Response.success(jobProfileService.listAll());
    }
}
