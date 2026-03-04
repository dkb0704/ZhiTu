package com.dk.infrastructure.dao;

import com.dk.infrastructure.dao.po.UserPreferencePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户偏好与测试 DAO。
 */
@Mapper
public interface IUserPreferenceDao {

    /** 新增偏好 */
    int insert(UserPreferencePO po);

    /** 按 userId 更新偏好 */
    int updateByUserId(UserPreferencePO po);

    /** 按 userId 查询偏好 */
    UserPreferencePO selectByUserId(@Param("userId") Long userId);
}
