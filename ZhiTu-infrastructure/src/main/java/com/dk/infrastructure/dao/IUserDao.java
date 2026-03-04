package com.dk.infrastructure.dao;

import com.dk.infrastructure.dao.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表 DAO（仅认证核心字段）。
 */
@Mapper
public interface IUserDao {

    /** 新增用户 */
    int insert(UserPO po);

    /** 按 ID 查询 */
    UserPO selectById(@Param("id") Long id);

    /** 按手机号查询 */
    UserPO selectByPhone(@Param("phone") String phone);

    /** 按邮箱查询 */
    UserPO selectByEmail(@Param("email") String email);
}
