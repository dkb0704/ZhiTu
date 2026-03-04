package com.dk.domain.user.adapter.repository;

import com.dk.domain.user.model.entity.UserPreferenceEntity;

/**
 * 用户偏好与测试仓储端口（user_preference 表）。
 */
public interface IUserPreferenceRepository {

    /** 保存或更新偏好与测试 */
    void saveOrUpdate(UserPreferenceEntity preference);

    /** 按 userId 查询偏好与测试 */
    UserPreferenceEntity findByUserId(Long userId);
}
