package com.dk.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册响应 DTO：注册成功后返回用户 ID、JWT 令牌及昵称。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDTO {

    private Long userId;
    private String token;
    private String nickname;
}
