package com.dk.domain.user.model.aggregate;

import com.dk.domain.user.model.entity.UserEntity;
import com.dk.domain.user.model.entity.UserPreferenceEntity;
import com.dk.domain.user.model.entity.UserProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 个人资料卡聚合：user + profile + preference。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCardAggregate {

    /** 用户基本信息（昵称、手机号等） */
    private UserEntity user;
    /** 个人资料（专业、年级、经历等） */
    private UserProfileEntity profile;
    /** 偏好与测试（MBTI、权重等） */
    private UserPreferenceEntity preference;
}
