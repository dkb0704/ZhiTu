package com.dk.infrastructure.adapter.repository;

import com.dk.domain.user.adapter.repository.IUserProfileRepository;
import com.dk.domain.user.model.entity.UserProfileEntity;
import com.dk.infrastructure.dao.IUserProfileDao;
import com.dk.infrastructure.dao.po.UserProfilePO;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 用户个人资料仓储实现（user_profile 表）。
 */
@Repository
public class UserProfileRepositoryImpl implements IUserProfileRepository {

    private final IUserProfileDao profileDao;

    public UserProfileRepositoryImpl(IUserProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @Override
    public void saveOrUpdate(UserProfileEntity profile) {
        UserProfilePO existing = profileDao.selectByUserId(profile.getUserId());
        UserProfilePO po = toPO(profile);
        if (existing == null) {
            po.setCreateTime(new Date());
            po.setUpdateTime(new Date());
            profileDao.insert(po);
            profile.setId(po.getId());
        } else {
            // 局部增量更新：仅覆盖本次传入了值的字段，未传的保留数据库已有值
            po.setId(existing.getId());
            if (po.getMajor() == null)
                po.setMajor(existing.getMajor());
            if (po.getEducation() == null)
                po.setEducation(existing.getEducation());
            if (po.getEducationBackground() == null)
                po.setEducationBackground(existing.getEducationBackground());
            if (po.getGrade() == null)
                po.setGrade(existing.getGrade());
            if (po.getBaseCities() == null)
                po.setBaseCities(existing.getBaseCities());
            if (po.getTargetPosition() == null)
                po.setTargetPosition(existing.getTargetPosition());
            if (po.getGpa() == null)
                po.setGpa(existing.getGpa());
            if (po.getPoliticalStatus() == null)
                po.setPoliticalStatus(existing.getPoliticalStatus());
            if (po.getHonors() == null)
                po.setHonors(existing.getHonors());
            if (po.getProjects() == null)
                po.setProjects(existing.getProjects());
            if (po.getCampus() == null)
                po.setCampus(existing.getCampus());
            if (po.getSkills() == null)
                po.setSkills(existing.getSkills());
            if (po.getSelfEvaluation() == null)
                po.setSelfEvaluation(existing.getSelfEvaluation());
            if (po.getInternships() == null)
                po.setInternships(existing.getInternships());
            po.setCreateTime(existing.getCreateTime());
            po.setUpdateTime(new Date());
            profileDao.updateByUserId(po);
        }
    }

    @Override
    public UserProfileEntity findByUserId(Long userId) {
        if (userId == null)
            return null;
        UserProfilePO po = profileDao.selectByUserId(userId);
        return po == null ? null : toEntity(po);
    }

    private static UserProfilePO toPO(UserProfileEntity e) {
        return UserProfilePO.builder()
                .userId(e.getUserId()).major(e.getMajor()).education(e.getEducation()).educationBackground(e.getEducationBackground()).grade(e.getGrade())
                .baseCities(e.getBaseCities()).targetPosition(e.getTargetPosition())
                .gpa(e.getGpa()).politicalStatus(e.getPoliticalStatus())
                .honors(e.getHonors()).projects(e.getProjects()).campus(e.getCampus())
                .skills(e.getSkills())
                .selfEvaluation(e.getSelfEvaluation()).internships(e.getInternships())
                .build();
    }

    private static UserProfileEntity toEntity(UserProfilePO po) {
        return UserProfileEntity.builder()
                .id(po.getId()).userId(po.getUserId())
                .major(po.getMajor()).education(po.getEducation()).educationBackground(po.getEducationBackground()).grade(po.getGrade())
                .baseCities(po.getBaseCities()).targetPosition(po.getTargetPosition())
                .gpa(po.getGpa()).politicalStatus(po.getPoliticalStatus())
                .honors(po.getHonors()).projects(po.getProjects()).campus(po.getCampus())
                .skills(po.getSkills())
                .selfEvaluation(po.getSelfEvaluation()).internships(po.getInternships())
                .createTime(po.getCreateTime()).updateTime(po.getUpdateTime())
                .build();
    }
}
