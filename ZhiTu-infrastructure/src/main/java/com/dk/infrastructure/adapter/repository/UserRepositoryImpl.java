package com.dk.infrastructure.adapter.repository;

import com.dk.domain.user.adapter.repository.IUserRepository;
import com.dk.domain.user.model.entity.UserEntity;
import com.dk.infrastructure.dao.IUserDao;
import com.dk.infrastructure.dao.po.UserPO;
import org.springframework.stereotype.Repository;

/**
 * 用户仓储实现（user 表）。
 */
@Repository
public class UserRepositoryImpl implements IUserRepository {

    private final IUserDao userDao;

    public UserRepositoryImpl(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void save(UserEntity user) {
        UserPO po = UserPO.builder()
                .phone(user.getPhone())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .nickname(user.getNickname())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .build();
        userDao.insert(po);
        user.setId(po.getId());
    }

    @Override
    public UserEntity findById(Long id) {
        if (id == null)
            return null;
        UserPO po = userDao.selectById(id);
        return po == null ? null : toEntity(po);
    }

    @Override
    public UserEntity findByPhone(String phone) {
        if (phone == null)
            return null;
        UserPO po = userDao.selectByPhone(phone);
        return po == null ? null : toEntity(po);
    }

    @Override
    public UserEntity findByEmail(String email) {
        if (email == null)
            return null;
        UserPO po = userDao.selectByEmail(email);
        return po == null ? null : toEntity(po);
    }

    private static UserEntity toEntity(UserPO po) {
        return UserEntity.builder()
                .id(po.getId())
                .phone(po.getPhone())
                .email(po.getEmail())
                .passwordHash(po.getPasswordHash())
                .nickname(po.getNickname())
                .createTime(po.getCreateTime())
                .updateTime(po.getUpdateTime())
                .build();
    }
}
