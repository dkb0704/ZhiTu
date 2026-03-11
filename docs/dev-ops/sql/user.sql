-- ============================================================
-- ZhiTu 用户表
-- ============================================================
CREATE DATABASE IF NOT EXISTS zhitu DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE zhitu;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `phone`         VARCHAR(20)  DEFAULT NULL             COMMENT '手机号',
    `email`         VARCHAR(128) DEFAULT NULL             COMMENT '邮箱',
    `password_hash` VARCHAR(255) NOT NULL                 COMMENT '密码哈希（BCrypt）',
    `nickname`      VARCHAR(64)  DEFAULT NULL             COMMENT '昵称',
    `create_time`   DATETIME     NOT NULL                 COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL                 COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
