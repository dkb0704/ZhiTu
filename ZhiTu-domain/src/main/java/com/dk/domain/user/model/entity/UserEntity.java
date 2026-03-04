package com.dk.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户实体：仅包含认证核心字段。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    /** 主键 */
    private Long id;
    /** 手机号 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** BCrypt 密码哈希 */
    private String passwordHash;
    /** 昵称 */
    private String nickname;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
}
