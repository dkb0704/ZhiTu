package com.dk.domain.user.adapter.port;

/**
 * 密码编码端口：领域层定义接口，基础设施层提供具体加密实现。
 */
public interface IPasswordEncoder {

    /**
     * 对明文密码进行编码（如 BCrypt）。
     */
    String encode(String rawPassword);

    /**
     * 校验明文密码与已编码密码是否匹配。
     */
    boolean matches(String rawPassword, String encodedPassword);
}
