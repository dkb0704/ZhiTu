package com.dk.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录请求 DTO：account 为手机号或邮箱。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    /** 手机号或邮箱 */
    private String account;
    /** 密码 */
    private String password;
}
