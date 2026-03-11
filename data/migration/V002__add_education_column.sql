-- 为 user_profile 表添加 education（学历）字段
ALTER TABLE user_profile ADD COLUMN education VARCHAR(50) DEFAULT NULL COMMENT '学历' AFTER major;
