package com.dk.domain.user.model.aggregate;

import com.dk.domain.user.model.entity.UserCareerEntity;
import com.dk.domain.user.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 职业生涯卡聚合：user + career。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerCardAggregate {

    /** 用户基本信息 */
    private UserEntity user;
    /** 职业生涯数据 */
    private UserCareerEntity career;
}
