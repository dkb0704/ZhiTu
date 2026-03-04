package com.dk.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册请求 DTO：至少提供 phone 或 email 其一，密码必填。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    /** 手机号（与 email 至少填一个） */
    private String phone;
    /** 邮箱（与 phone 至少填一个） */
    private String email;
    /** 密码（必填） */
    private String password;
    /** 昵称（可选） */
    private String nickname;
}
