package com.dk.domain.user.adapter.repository;

import com.dk.domain.user.model.entity.UserProfileEntity;

/**
 * 用户个人资料仓储端口（user_profile 表）。
 */
public interface IUserProfileRepository {

    /** 保存或更新个人资料 */
    void saveOrUpdate(UserProfileEntity profile);

    /** 按 userId 查询个人资料 */
    UserProfileEntity findByUserId(Long userId);
}
