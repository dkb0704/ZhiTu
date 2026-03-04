package com.dk.infrastructure.dao;

import com.dk.infrastructure.dao.po.UserProfilePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户个人资料 DAO。
 */
@Mapper
public interface IUserProfileDao {

    /** 新增资料 */
    int insert(UserProfilePO po);

    /** 按 userId 更新资料 */
    int updateByUserId(UserProfilePO po);

    /** 按 userId 查询资料 */
    UserProfilePO selectByUserId(@Param("userId") Long userId);
}
