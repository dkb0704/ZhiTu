package com.dk.infrastructure.adapter.repository;

import com.dk.domain.user.adapter.repository.IUserCareerRepository;
import com.dk.domain.user.model.entity.UserCareerEntity;
import com.dk.infrastructure.dao.IUserCareerDao;
import com.dk.infrastructure.dao.po.UserCareerPO;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 用户职业生涯仓储实现（user_career 表）。
 */
@Repository
public class UserCareerRepositoryImpl implements IUserCareerRepository {

    private final IUserCareerDao careerDao;

    public UserCareerRepositoryImpl(IUserCareerDao careerDao) {
        this.careerDao = careerDao;
    }

    @Override
    public void saveOrUpdate(UserCareerEntity career) {
        UserCareerPO existing = careerDao.selectByUserId(career.getUserId());
        UserCareerPO po = toPO(career);
        if (existing == null) {
            po.setCreateTime(new Date());
            po.setUpdateTime(new Date());
            careerDao.insert(po);
            career.setId(po.getId());
        } else {
            po.setUpdateTime(new Date());
            careerDao.updateByUserId(po);
        }
    }

    @Override
    public UserCareerEntity findByUserId(Long userId) {
        if (userId == null)
            return null;
        UserCareerPO po = careerDao.selectByUserId(userId);
        return po == null ? null : toEntity(po);
    }

    private static UserCareerPO toPO(UserCareerEntity e) {
        return UserCareerPO.builder()
                .userId(e.getUserId())
                .targetIndustry(e.getTargetIndustry()).targetPosition(e.getTargetPosition())
                .learningGoals(e.getLearningGoals())
                .build();
    }

    private static UserCareerEntity toEntity(UserCareerPO po) {
        return UserCareerEntity.builder()
                .id(po.getId()).userId(po.getUserId())
                .targetIndustry(po.getTargetIndustry()).targetPosition(po.getTargetPosition())
                .learningGoals(po.getLearningGoals())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime())
                .build();
    }
}
