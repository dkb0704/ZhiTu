package com.dk.trigger.http;

import com.dk.api.dto.auth.LoginRequestDTO;
import com.dk.api.dto.auth.LoginResponseDTO;
import com.dk.api.dto.auth.RegisterRequestDTO;
import com.dk.api.dto.auth.RegisterResponseDTO;
import com.dk.api.response.Response;
import com.dk.domain.user.model.entity.UserEntity;
import com.dk.domain.user.service.UserDomainService;
import com.dk.trigger.http.auth.JwtSupport;
import com.dk.types.enums.ResponseCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 认证接口：注册、登录（手机号/邮箱 + 密码）。
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

        @Resource
        private UserDomainService userDomainService;

        @Resource
        private JwtSupport jwtSupport;

        @PostMapping("/register")
        public Response<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
                UserEntity user = userDomainService.register(
                                request.getPhone(),
                                request.getEmail(),
                                request.getPassword(),
                                request.getNickname());
                String token = jwtSupport.createToken(user.getId(), user.getNickname());
                RegisterResponseDTO data = RegisterResponseDTO.builder()
                                .userId(user.getId())
                                .token(token)
                                .nickname(user.getNickname())
                                .build();
                return Response.<RegisterResponseDTO>builder()
                                .code(ResponseCode.SUCCESS.getCode())
                                .info(ResponseCode.SUCCESS.getInfo())
                                .data(data)
                                .build();
        }

        @PostMapping("/login")
        public Response<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
                UserEntity user = userDomainService.login(request.getAccount(), request.getPassword());
                String token = jwtSupport.createToken(user.getId(), user.getNickname());
                LoginResponseDTO data = LoginResponseDTO.builder()
                                .token(token)
                                .userId(user.getId())
                                .nickname(user.getNickname())
                                .build();
                return Response.<LoginResponseDTO>builder()
                                .code(ResponseCode.SUCCESS.getCode())
                                .info(ResponseCode.SUCCESS.getInfo())
                                .data(data)
                                .build();
        }
}
