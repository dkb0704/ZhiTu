package com.dk.infrastructure.adapter.port;

import com.dk.domain.user.adapter.port.IPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCrypt 密码编码实现，基于 spring-security-crypto。
 */
@Component
public class BCryptPasswordEncoderPort implements IPasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
