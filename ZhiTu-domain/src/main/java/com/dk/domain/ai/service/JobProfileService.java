package com.dk.domain.ai.service;

import com.dk.domain.ai.adapter.port.ILlmGateway;
import com.dk.domain.ai.adapter.repository.IJobProfileRepository;
import com.dk.domain.ai.model.entity.JobProfileEntity;
import com.dk.domain.job.adapter.repository.IJobPositionRepository;
import com.dk.domain.job.model.entity.JobPositionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位画像生成服务。
 * 从 job_position 中按 title 聚合 JD，调用 LLM 生成结构化画像。
 */
@Slf4j
@Service
public class JobProfileService {

    @Resource
    private ILlmGateway llmGateway;

    @Resource
    private IJobProfileRepository jobProfileRepository;

    @Resource
    private IJobPositionRepository jobPositionRepository;

    /** 赛题要求不少于 10 个岗位画像，定义需要生成画像的标准岗位列表 */
    private static final List<String> TARGET_JOB_TITLES = Arrays.asList(
            "Java", "前端开发", "测试工程师", "C/C++", "软件测试",
            "技术支持工程师", "实施工程师", "硬件测试", "科研人员",
            "项目专员/助理", "网络客服", "销售工程师"
    );

    /**
     * 为指定岗位名称生成画像。
     * 如果该岗位画像已存在则更新，不存在则新建。
     */
    public JobProfileEntity generateProfile(String jobTitle, String systemPrompt) {
        List<JobPositionEntity> jobs = jobPositionRepository.findByCondition(
                jobTitle, null, null, null, null, 0, 20);

        if (jobs.isEmpty()) {
            throw new RuntimeException("未找到与 \"" + jobTitle + "\" 相关的岗位数据");
        }

        String userMessage = buildUserMessage(jobTitle, jobs);
        String llmOutput = llmGateway.chat(systemPrompt, userMessage, 0.3f);

        JobProfileEntity entity = parseProfile(jobTitle, llmOutput, jobs);

        JobProfileEntity existing = jobProfileRepository.findByJobTitle(jobTitle);
        if (existing != null) {
            entity.setId(existing.getId());
            entity.setUpdateTime(new Date());
            jobProfileRepository.update(entity);
            entity = jobProfileRepository.findById(existing.getId());
        } else {
            entity.setCreateTime(new Date());
            jobProfileRepository.save(entity);
            entity = jobProfileRepository.findByJobTitle(jobTitle);
        }

        log.info("岗位画像生成完成: {}", jobTitle);
        return entity;
    }

    /**
     * 批量生成所有目标岗位的画像。
     */
    public List<JobProfileEntity> batchGenerate(String systemPrompt) {
        List<JobProfileEntity> results = new ArrayList<>();
        for (String title : TARGET_JOB_TITLES) {
            try {
                JobProfileEntity profile = generateProfile(title, systemPrompt);
                results.add(profile);
            } catch (Exception e) {
                log.error("生成岗位画像失败: {} - {}", title, e.getMessage());
            }
        }
        return results;
    }

    public JobProfileEntity getById(Long id) {
        return jobProfileRepository.findById(id);
    }

    public List<JobProfileEntity> listAll() {
        return jobProfileRepository.findAll();
    }

    public List<String> getTargetJobTitles() {
        return Collections.unmodifiableList(TARGET_JOB_TITLES);
    }

    /**
     * 拼接 LLM 用户消息：将多条 JD 合并为结构化输入。
     */
    private String buildUserMessage(String jobTitle, List<JobPositionEntity> jobs) {
        StringBuilder sb = new StringBuilder();
        sb.append("请为「").append(jobTitle).append("」这一类岗位生成画像。\n\n");
        sb.append("以下是 ").append(jobs.size()).append(" 条该类岗位的 JD 数据：\n\n");

        for (int i = 0; i < jobs.size(); i++) {
            JobPositionEntity job = jobs.get(i);
            sb.append("--- JD ").append(i + 1).append(" ---\n");
            sb.append("职位名称: ").append(job.getTitle()).append("\n");
            sb.append("公司: ").append(job.getCompanyName()).append("\n");
            sb.append("行业: ").append(job.getIndustry()).append("\n");
            sb.append("薪资: ").append(job.getSalaryRange()).append("\n");
            if (job.getDescription() != null) {
                String desc = job.getDescription();
                if (desc.length() > 800) {
                    desc = desc.substring(0, 800) + "...";
                }
                sb.append("职位描述: ").append(desc).append("\n");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * 解析 LLM 输出，组装 JobProfileEntity。
     * LLM 输出应为 JSON，直接存储各字段。
     */
    private JobProfileEntity parseProfile(String jobTitle, String llmJson,
                                          List<JobPositionEntity> jobs) {
        com.alibaba.fastjson.JSONObject json;
        try {
            json = com.alibaba.fastjson.JSON.parseObject(llmJson);
        } catch (Exception e) {
            log.warn("LLM 输出 JSON 解析失败，原始内容: {}", llmJson);
            json = new com.alibaba.fastjson.JSONObject();
        }

        String sourceIds = jobs.stream()
                .map(j -> String.valueOf(j.getId()))
                .collect(Collectors.joining(","));

        return JobProfileEntity.builder()
                .jobTitle(jobTitle)
                .jobFamily(json.getString("jobFamily"))
                .description(json.getString("description"))
                .hardSkills(json.containsKey("hardSkills") ? json.getJSONArray("hardSkills").toJSONString() : null)
                .softSkills(json.containsKey("softSkills") ? json.getJSONArray("softSkills").toJSONString() : null)
                .certRequirements(json.containsKey("certRequirements") ? json.getJSONArray("certRequirements").toJSONString() : null)
                .abilityModel(json.containsKey("abilityModel") ? json.getJSONObject("abilityModel").toJSONString() : null)
                .salaryRange(json.getString("salaryRange"))
                .careerPath(json.getString("careerPath"))
                .sourceJobIds(sourceIds)
                .build();
    }
}
