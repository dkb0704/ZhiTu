package com.dk.domain.user.service;

import com.dk.domain.user.adapter.port.IPasswordEncoder;
import com.dk.domain.user.adapter.repository.*;
import com.dk.domain.user.model.entity.*;
import com.dk.domain.user.model.valobj.AuthErrorCode;
import com.dk.types.enums.ResponseCode;
import com.dk.types.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 用户领域服务：注册、登录、资料管理、偏好管理、职业生涯管理。
 */
@Service
public class UserDomainService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");

    private final IUserRepository userRepository;
    private final IUserProfileRepository profileRepository;
    private final IUserPreferenceRepository preferenceRepository;
    private final IUserCareerRepository careerRepository;
    private final IPasswordEncoder passwordEncoder;

    public UserDomainService(IUserRepository userRepository,
            IUserProfileRepository profileRepository,
            IUserPreferenceRepository preferenceRepository,
            IUserCareerRepository careerRepository,
            IPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.preferenceRepository = preferenceRepository;
        this.careerRepository = careerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== 注册 =====

    /**
     * 注册：至少 phone 或 email 其一，密码必填。
     */
    public UserEntity register(String phone, String email, String password, String nickname) {
        if (StringUtils.isBlank(password)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "密码不能为空");
        }
        boolean hasPhone = StringUtils.isNotBlank(phone);
        boolean hasEmail = StringUtils.isNotBlank(email);
        if (!hasPhone && !hasEmail) {
            throw new AppException(AuthErrorCode.INVALID_ACCOUNT.getCode(), AuthErrorCode.INVALID_ACCOUNT.getInfo());
        }
        if (hasPhone && !PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new AppException(AuthErrorCode.INVALID_ACCOUNT.getCode(), "手机号格式不正确");
        }
        if (hasEmail && !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new AppException(AuthErrorCode.INVALID_ACCOUNT.getCode(), "邮箱格式不正确");
        }
        if (hasPhone && userRepository.findByPhone(phone.trim()) != null) {
            throw new AppException(AuthErrorCode.USER_ALREADY_EXISTS.getCode(), "该手机号已注册");
        }
        if (hasEmail && userRepository.findByEmail(email.trim()) != null) {
            throw new AppException(AuthErrorCode.USER_ALREADY_EXISTS.getCode(), "该邮箱已注册");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Date now = new Date();
        UserEntity user = UserEntity.builder()
                .phone(hasPhone ? phone.trim() : null)
                .email(hasEmail ? email.trim() : null)
                .passwordHash(encodedPassword)
                .nickname(StringUtils.isNotBlank(nickname) ? nickname.trim() : null)
                .createTime(now)
                .updateTime(now)
                .build();
        userRepository.save(user);
        return user;
    }

    // ===== 登录 =====

    /**
     * 登录：account 为手机号或邮箱，校验密码后返回用户实体。
     */
    public UserEntity login(String account, String password) {
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            throw new AppException(AuthErrorCode.LOGIN_FAIL.getCode(), AuthErrorCode.LOGIN_FAIL.getInfo());
        }
        String trimAccount = account.trim();
        UserEntity user = null;
        if (PHONE_PATTERN.matcher(trimAccount).matches()) {
            user = userRepository.findByPhone(trimAccount);
        }
        if (user == null && EMAIL_PATTERN.matcher(trimAccount).matches()) {
            user = userRepository.findByEmail(trimAccount);
        }
        if (user == null) {
            throw new AppException(AuthErrorCode.LOGIN_FAIL.getCode(), AuthErrorCode.LOGIN_FAIL.getInfo());
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new AppException(AuthErrorCode.LOGIN_FAIL.getCode(), AuthErrorCode.LOGIN_FAIL.getInfo());
        }
        return user;
    }

    // ===== 个人资料 =====

    /** 更新个人资料（user_profile 表） */
    public void updateProfile(Long userId, UserProfileEntity profileData) {
        checkUserExists(userId);
        profileData.setUserId(userId);
        profileRepository.saveOrUpdate(profileData);
    }

    /** 查询个人资料 */
    public UserProfileEntity getProfile(Long userId) {
        checkUserExists(userId);
        return profileRepository.findByUserId(userId);
    }

    // ===== 偏好与测试 =====

    /** 更新偏好与测试（user_preference 表） */
    public void updatePreference(Long userId, UserPreferenceEntity preferenceData) {
        checkUserExists(userId);
        preferenceData.setUserId(userId);
        preferenceRepository.saveOrUpdate(preferenceData);
    }

    /** 查询偏好与测试 */
    public UserPreferenceEntity getPreference(Long userId) {
        return preferenceRepository.findByUserId(userId);
    }

    // ===== 职业生涯 =====

    /** 更新职业生涯（user_career 表） */
    public void updateCareer(Long userId, UserCareerEntity careerData) {
        checkUserExists(userId);
        careerData.setUserId(userId);
        careerRepository.saveOrUpdate(careerData);
    }

    /** 查询职业生涯 */
    public UserCareerEntity getCareer(Long userId) {
        return careerRepository.findByUserId(userId);
    }

    // ===== "我的"模块 =====

    /** 获取用户基本信息（隐藏密码） */
    public UserEntity getUserBasicInfo(Long userId) {
        UserEntity user = userRepository.findById(userId);
        if (user == null) {
            throw new AppException(AuthErrorCode.USER_NOT_FOUND.getCode(), AuthErrorCode.USER_NOT_FOUND.getInfo());
        }
        user.setPasswordHash(null);
        return user;
    }

    // ===== 私有方法 =====

    private void checkUserExists(Long userId) {
        if (userRepository.findById(userId) == null) {
            throw new AppException(AuthErrorCode.USER_NOT_FOUND.getCode(), AuthErrorCode.USER_NOT_FOUND.getInfo());
        }
    }
}
