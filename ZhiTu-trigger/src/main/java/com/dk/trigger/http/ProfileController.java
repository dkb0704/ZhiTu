package com.dk.trigger.http;

import com.dk.api.dto.user.UpdateCareerRequestDTO;
import com.dk.api.dto.user.UpdatePreferenceRequestDTO;
import com.dk.api.dto.user.UpdateProfileRequestDTO;
import com.dk.api.response.Response;
import com.dk.domain.user.model.aggregate.CareerCardAggregate;
import com.dk.domain.user.model.aggregate.ProfileCardAggregate;
import com.dk.domain.user.model.entity.*;
import com.dk.domain.user.service.UserDomainService;
import com.dk.trigger.http.auth.JwtSupport;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户资料接口：个人资料、偏好测试、职业生涯。
 * 所有接口从 Authorization 头的 JWT 中解析 userId。
 */
@RestController
@RequestMapping("/user")
public class ProfileController {

    @Resource
    private UserDomainService userDomainService;

    @Resource
    private JwtSupport jwtSupport;

    // ===== 个人资料 (user_profile) =====

    /** 更新个人资料 */
    @PutMapping("/profile")
    public Response<?> updateProfile(@RequestHeader("Authorization") String token,
            @RequestBody UpdateProfileRequestDTO request) {
        Long userId = parseUserId(token);
        UserProfileEntity data = UserProfileEntity.builder()
                .major(request.getMajor())
                .education(request.getEducation())
                .educationBackground(request.getEducationBackground())
                .grade(request.getGrade())
                .baseCities(request.getBaseCities())
                .targetPosition(request.getTargetPosition())
                .gpa(request.getGpa())
                .politicalStatus(request.getPoliticalStatus())
                .honors(request.getHonors())
                .projects(request.getProjects())
                .campus(request.getCampus())
                .skills(request.getSkills())
                .selfEvaluation(request.getSelfEvaluation())
                .internships(request.getInternships())
                .build();
        userDomainService.updateProfile(userId, data);
        return Response.success();
    }

    /** 查询个人资料 */
    @GetMapping("/profile")
    public Response<UserProfileEntity> getProfile(@RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        return Response.success(userDomainService.getProfile(userId));
    }

    // ===== 偏好与测试 (user_preference) =====

    /** 更新偏好与测试 */
    @PutMapping("/preference")
    public Response<?> updatePreference(@RequestHeader("Authorization") String token,
            @RequestBody UpdatePreferenceRequestDTO request) {
        Long userId = parseUserId(token);
        UserPreferenceEntity data = UserPreferenceEntity.builder()
                .jobPreferences(request.getJobPreferences())
                .softSkills(request.getSoftSkills())
                .mbti(request.getMbti())
                .holland(request.getHolland())
                .bigFive(request.getBigFive())
                .build();
        userDomainService.updatePreference(userId, data);
        return Response.success();
    }

    /** 查询偏好与测试 */
    @GetMapping("/preference")
    public Response<UserPreferenceEntity> getPreference(@RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        return Response.success(userDomainService.getPreference(userId));
    }

    // ===== 职业生涯 (user_career) =====

    /** 更新职业生涯 */
    @PutMapping("/career")
    public Response<?> updateCareer(@RequestHeader("Authorization") String token,
            @RequestBody UpdateCareerRequestDTO request) {
        Long userId = parseUserId(token);
        UserCareerEntity data = UserCareerEntity.builder()
                .targetIndustry(request.getTargetIndustry())
                .targetPosition(request.getTargetPosition())
                .learningGoals(request.getLearningGoals())
                .build();
        userDomainService.updateCareer(userId, data);
        return Response.success();
    }

    /** 查询职业生涯 */
    @GetMapping("/career")
    public Response<UserCareerEntity> getCareer(@RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        return Response.success(userDomainService.getCareer(userId));
    }

    // ===== "我的"模块聚合 =====

    /** 个人资料卡：聚合 user + profile + preference */
    @GetMapping("/profile-card")
    public Response<ProfileCardAggregate> getProfileCard(@RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        ProfileCardAggregate card = ProfileCardAggregate.builder()
                .user(userDomainService.getUserBasicInfo(userId))
                .profile(userDomainService.getProfile(userId))
                .preference(userDomainService.getPreference(userId))
                .build();
        return Response.success(card);
    }

    /** 职业生涯卡：聚合 user + career */
    @GetMapping("/career-card")
    public Response<CareerCardAggregate> getCareerCard(@RequestHeader("Authorization") String token) {
        Long userId = parseUserId(token);
        CareerCardAggregate card = CareerCardAggregate.builder()
                .user(userDomainService.getUserBasicInfo(userId))
                .career(userDomainService.getCareer(userId))
                .build();
        return Response.success(card);
    }

    // ===== 工具方法 =====

    private Long parseUserId(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtSupport.parseUserId(token);
    }
}
