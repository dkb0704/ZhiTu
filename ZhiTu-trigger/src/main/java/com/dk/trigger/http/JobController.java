package com.dk.trigger.http;

import com.dk.api.dto.job.JobSearchRequestDTO;
import com.dk.api.response.Response;
import com.dk.domain.job.model.entity.CareerTrackEntity;
import com.dk.domain.job.model.entity.JobPositionEntity;
import com.dk.domain.job.service.JobDomainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位浏览接口：分页搜索 + 详情查看。
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Resource
    private JobDomainService jobDomainService;

    /**
     * 分页搜索岗位列表。
     * 支持筛选：title / location / industry / salaryMin / salaryMax
     */
    @GetMapping("/list")
    public Response<Map<String, Object>> listJobs(JobSearchRequestDTO request) {
        int page = request.getPage() != null ? request.getPage() : 1;
        int size = request.getSize() != null ? request.getSize() : 10;

        List<JobPositionEntity> jobs = jobDomainService.searchJobs(
                request.getTitle(), request.getLocation(), request.getIndustry(),
                request.getSalaryMin(), request.getSalaryMax(), page, size);

        int total = jobDomainService.countJobs(
                request.getTitle(), request.getLocation(), request.getIndustry(),
                request.getSalaryMin(), request.getSalaryMax());

        Map<String, Object> result = new HashMap<>();
        result.put("list", jobs);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        return Response.success(result);
    }

    /**
     * 查询岗位详情。
     */
    @GetMapping("/{id}")
    public Response<JobPositionEntity> getJobDetail(@PathVariable Long id) {
        return Response.success(jobDomainService.getJobDetail(id));
    }

    /**
     * 岗位图谱：职业线 + 节点 + 转岗关系（升岗/转岗数据来自 DB，可由 AI 建议后落库）。
     */
    @GetMapping("/career-map")
    public Response<List<CareerTrackEntity>> getCareerMap() {
        return Response.success(jobDomainService.getCareerMap());
    }
}
