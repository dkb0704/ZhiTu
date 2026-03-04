package com.dk.infrastructure.dao;

import com.dk.infrastructure.dao.po.UserCareerPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户职业生涯 DAO。
 */
@Mapper
public interface IUserCareerDao {

    /** 新增职业生涯 */
    int insert(UserCareerPO po);

    /** 按 userId 更新职业生涯 */
    int updateByUserId(UserCareerPO po);

    /** 按 userId 查询职业生涯 */
    UserCareerPO selectByUserId(@Param("userId") Long userId);
}
