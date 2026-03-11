-- ============================================================
-- ZhiTu 用户表 - 业务字段扩展
-- ============================================================
USE zhitu;

ALTER TABLE `user`
    ADD COLUMN `major`           VARCHAR(64)  DEFAULT NULL COMMENT '专业' AFTER `nickname`,
    ADD COLUMN `grade`           VARCHAR(20)  DEFAULT NULL COMMENT '年级（如 大一/大二/研一）' AFTER `major`,
    ADD COLUMN `base_cities`     VARCHAR(255) DEFAULT NULL COMMENT '意向城市（JSON数组，最多3个）' AFTER `grade`,
    ADD COLUMN `target_position` VARCHAR(128) DEFAULT NULL COMMENT '意向岗位' AFTER `base_cities`,
    ADD COLUMN `gpa`             VARCHAR(10)  DEFAULT NULL COMMENT '绩点' AFTER `target_position`,
    ADD COLUMN `political_status` VARCHAR(20) DEFAULT NULL COMMENT '政治面貌' AFTER `gpa`,
    ADD COLUMN `honors`          TEXT         DEFAULT NULL COMMENT '荣誉及证书（JSON数组）' AFTER `political_status`,
    ADD COLUMN `projects`        TEXT         DEFAULT NULL COMMENT '项目经历（JSON数组）' AFTER `honors`,
    ADD COLUMN `campus`          TEXT         DEFAULT NULL COMMENT '校园经历（JSON数组）' AFTER `projects`,
    ADD COLUMN `skills`          TEXT         DEFAULT NULL COMMENT '技能（JSON数组）' AFTER `campus`,
    ADD COLUMN `languages`       VARCHAR(255) DEFAULT NULL COMMENT '语言能力（JSON数组）' AFTER `skills`,
    ADD COLUMN `self_evaluation` TEXT         DEFAULT NULL COMMENT '自我评价' AFTER `languages`,
    ADD COLUMN `internships`     TEXT         DEFAULT NULL COMMENT '实习经历（JSON数组）' AFTER `self_evaluation`;
