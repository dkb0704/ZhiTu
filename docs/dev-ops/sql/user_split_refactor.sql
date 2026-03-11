-- ============================================================
-- ZhiTu 用户表拆分重构
-- user → user + user_profile + user_preference + user_career
-- ============================================================
USE zhitu;

-- 1. 创建 user_profile 表
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`          BIGINT       NOT NULL COMMENT '关联用户ID',
    `major`            VARCHAR(64)  DEFAULT NULL COMMENT '专业',
    `grade`            VARCHAR(20)  DEFAULT NULL COMMENT '年级',
    `base_cities`      VARCHAR(255) DEFAULT NULL COMMENT '意向城市 JSON',
    `target_position`  VARCHAR(128) DEFAULT NULL COMMENT '意向岗位',
    `gpa`              VARCHAR(10)  DEFAULT NULL COMMENT '绩点',
    `political_status` VARCHAR(20)  DEFAULT NULL COMMENT '政治面貌',
    `honors`           TEXT         DEFAULT NULL COMMENT '荣誉及证书 JSON',
    `projects`         TEXT         DEFAULT NULL COMMENT '项目经历 JSON',
    `campus`           TEXT         DEFAULT NULL COMMENT '校园经历 JSON',
    `skills`           TEXT         DEFAULT NULL COMMENT '技能 JSON',
    `languages`        VARCHAR(255) DEFAULT NULL COMMENT '语言能力 JSON',
    `self_evaluation`  TEXT         DEFAULT NULL COMMENT '自我评价',
    `internships`      TEXT         DEFAULT NULL COMMENT '实习经历 JSON',
    `create_time`      DATETIME     NOT NULL COMMENT '创建时间',
    `update_time`      DATETIME     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户个人资料';

-- 2. 创建 user_preference 表
DROP TABLE IF EXISTS `user_preference`;
CREATE TABLE `user_preference` (
    `id`              BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`         BIGINT      NOT NULL COMMENT '关联用户ID',
    `job_preferences` TEXT        DEFAULT NULL COMMENT '求职偏好权重 JSON',
    `soft_skills`     TEXT        DEFAULT NULL COMMENT '软实力自评 JSON',
    `mbti`            VARCHAR(10) DEFAULT NULL COMMENT 'MBTI 结果',
    `holland`         VARCHAR(10) DEFAULT NULL COMMENT '霍兰德结果',
    `big_five`        VARCHAR(64) DEFAULT NULL COMMENT 'BIG FIVE 结果',
    `create_time`     DATETIME    NOT NULL COMMENT '创建时间',
    `update_time`     DATETIME    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户偏好与测试';

-- 3. 创建 user_career 表
DROP TABLE IF EXISTS `user_career`;
CREATE TABLE `user_career` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`         BIGINT       NOT NULL COMMENT '关联用户ID',
    `target_industry` VARCHAR(64)  DEFAULT NULL COMMENT '目标行业',
    `target_position` VARCHAR(128) DEFAULT NULL COMMENT '目标岗位',
    `learning_goals`  TEXT         DEFAULT NULL COMMENT '学习目标及进度 JSON',
    `create_time`     DATETIME     NOT NULL COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户职业生涯';

-- 4. 迁移旧数据到子表（如果 user 表已有数据）
INSERT INTO user_profile (user_id, major, grade, base_cities, target_position, gpa, political_status,
    honors, projects, campus, skills, languages, self_evaluation, internships, create_time, update_time)
SELECT id, major, grade, base_cities, target_position, gpa, political_status,
    honors, projects, campus, skills, languages, self_evaluation, internships, create_time, update_time
FROM user WHERE major IS NOT NULL OR grade IS NOT NULL;

INSERT INTO user_preference (user_id, job_preferences, soft_skills, mbti, holland, big_five, create_time, update_time)
SELECT id, job_preferences, soft_skills, mbti, holland, big_five, create_time, update_time
FROM user WHERE job_preferences IS NOT NULL OR mbti IS NOT NULL;

-- 5. 删除 user 表中已迁移的列
ALTER TABLE `user`
    DROP COLUMN `major`,
    DROP COLUMN `grade`,
    DROP COLUMN `base_cities`,
    DROP COLUMN `target_position`,
    DROP COLUMN `gpa`,
    DROP COLUMN `political_status`,
    DROP COLUMN `honors`,
    DROP COLUMN `projects`,
    DROP COLUMN `campus`,
    DROP COLUMN `skills`,
    DROP COLUMN `languages`,
    DROP COLUMN `self_evaluation`,
    DROP COLUMN `internships`,
    DROP COLUMN `job_preferences`,
    DROP COLUMN `soft_skills`,
    DROP COLUMN `mbti`,
    DROP COLUMN `holland`,
    DROP COLUMN `big_five`;
