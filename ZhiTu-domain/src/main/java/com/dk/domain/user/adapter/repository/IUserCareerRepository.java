package com.dk.domain.user.adapter.repository;

import com.dk.domain.user.model.entity.UserCareerEntity;

/**
 * 用户职业生涯仓储端口（user_career 表）。
 */
public interface IUserCareerRepository {

    /** 保存或更新职业生涯 */
    void saveOrUpdate(UserCareerEntity career);

    /** 按 userId 查询职业生涯 */
    UserCareerEntity findByUserId(Long userId);
}
