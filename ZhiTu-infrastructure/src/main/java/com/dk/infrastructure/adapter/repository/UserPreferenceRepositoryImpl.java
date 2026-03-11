package com.dk.infrastructure.adapter.repository;

import com.dk.domain.user.adapter.repository.IUserPreferenceRepository;
import com.dk.domain.user.model.entity.UserPreferenceEntity;
import com.dk.infrastructure.dao.IUserPreferenceDao;
import com.dk.infrastructure.dao.po.UserPreferencePO;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 用户偏好与测试仓储实现（user_preference 表）。
 */
@Repository
public class UserPreferenceRepositoryImpl implements IUserPreferenceRepository {

    private final IUserPreferenceDao preferenceDao;

    public UserPreferenceRepositoryImpl(IUserPreferenceDao preferenceDao) {
        this.preferenceDao = preferenceDao;
    }

    @Override
    public void saveOrUpdate(UserPreferenceEntity preference) {
        UserPreferencePO existing = preferenceDao.selectByUserId(preference.getUserId());
        UserPreferencePO po = toPO(preference);
        if (existing == null) {
            po.setCreateTime(new Date());
            po.setUpdateTime(new Date());
            preferenceDao.insert(po);
            preference.setId(po.getId());
        } else {
            po.setId(existing.getId());
            if (po.getJobPreferences() == null)
                po.setJobPreferences(existing.getJobPreferences());
            if (po.getSoftSkills() == null)
                po.setSoftSkills(existing.getSoftSkills());
            if (po.getMbti() == null)
                po.setMbti(existing.getMbti());
            if (po.getHolland() == null)
                po.setHolland(existing.getHolland());
            if (po.getBigFive() == null)
                po.setBigFive(existing.getBigFive());
            po.setCreateTime(existing.getCreateTime());
            po.setUpdateTime(new Date());
            preferenceDao.updateByUserId(po);
        }
    }

    @Override
    public UserPreferenceEntity findByUserId(Long userId) {
        if (userId == null)
            return null;
        UserPreferencePO po = preferenceDao.selectByUserId(userId);
        return po == null ? null : toEntity(po);
    }

    private static UserPreferencePO toPO(UserPreferenceEntity e) {
        return UserPreferencePO.builder()
                .userId(e.getUserId())
                .jobPreferences(e.getJobPreferences()).softSkills(e.getSoftSkills())
                .mbti(e.getMbti()).holland(e.getHolland()).bigFive(e.getBigFive())
                .build();
    }

    private static UserPreferenceEntity toEntity(UserPreferencePO po) {
        return UserPreferenceEntity.builder()
                .id(po.getId()).userId(po.getUserId())
                .jobPreferences(po.getJobPreferences()).softSkills(po.getSoftSkills())
                .mbti(po.getMbti()).holland(po.getHolland()).bigFive(po.getBigFive())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime())
                .build();
    }
}
