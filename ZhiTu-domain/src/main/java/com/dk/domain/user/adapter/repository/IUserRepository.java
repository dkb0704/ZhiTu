package com.dk.domain.user.adapter.repository;

import com.dk.domain.user.model.entity.UserEntity;

/**
 * 用户仓储端口（user 表）：认证核心。
 */
public interface IUserRepository {

    /** 保存新用户 */
    void save(UserEntity user);

    /** 按 ID 查找用户 */
    UserEntity findById(Long id);

    /** 按手机号查找用户 */
    UserEntity findByPhone(String phone);

    /** 按邮箱查找用户 */
    UserEntity findByEmail(String email);
}
