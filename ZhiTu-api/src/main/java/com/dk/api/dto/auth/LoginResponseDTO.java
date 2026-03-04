package com.dk.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 DTO：登录成功后返回 JWT 令牌、用户 ID 及昵称。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private Long userId;
    private String nickname;
}
