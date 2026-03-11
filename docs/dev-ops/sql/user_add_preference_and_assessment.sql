-- ============================================================
-- ZhiTu 用户表 - 界面三四五字段扩展
-- 设计说明：
--   job_preferences / soft_skills 使用 JSON 存储，维度可随时增删改，无需改表结构
--   mbti / holland / big_five 存储测试结果字符串
-- ============================================================
USE zhitu;

ALTER TABLE `user`
    ADD COLUMN `job_preferences` TEXT DEFAULT NULL COMMENT '求职偏好权重 JSON，如 {"salary":8,"stability":6}' AFTER `internships`,
    ADD COLUMN `soft_skills`     TEXT DEFAULT NULL COMMENT '软实力自评 JSON，如 {"expression":4,"communication":3}' AFTER `job_preferences`,
    ADD COLUMN `mbti`            VARCHAR(10)  DEFAULT NULL COMMENT 'MBTI 测试结果' AFTER `soft_skills`,
    ADD COLUMN `holland`         VARCHAR(10)  DEFAULT NULL COMMENT '霍兰德测试结果' AFTER `mbti`,
    ADD COLUMN `big_five`        VARCHAR(64)  DEFAULT NULL COMMENT 'BIG FIVE 测试结果' AFTER `holland`;
