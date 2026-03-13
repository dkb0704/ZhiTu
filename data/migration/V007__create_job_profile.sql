CREATE TABLE IF NOT EXISTS `job_profile` (
    `id`                BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `job_title`         VARCHAR(128)  NOT NULL COMMENT '标准岗位名称',
    `job_family`        VARCHAR(64)   DEFAULT NULL COMMENT '岗位家族编码',
    `description`       TEXT          DEFAULT NULL COMMENT '岗位综合描述',
    `hard_skills`       TEXT          DEFAULT NULL COMMENT '专业技能要求 JSON 数组',
    `soft_skills`       TEXT          DEFAULT NULL COMMENT '软技能要求 JSON 数组',
    `cert_requirements` TEXT          DEFAULT NULL COMMENT '证书要求 JSON 数组',
    `ability_model`     TEXT          DEFAULT NULL COMMENT '能力模型 JSON',
    `salary_range`      VARCHAR(128)  DEFAULT NULL COMMENT '综合薪资范围',
    `career_path`       TEXT          DEFAULT NULL COMMENT '典型晋升路径描述',
    `source_job_ids`    TEXT          DEFAULT NULL COMMENT '依据的 job_position id 列表',
    `create_time`       DATETIME      NOT NULL COMMENT '创建时间',
    `update_time`       DATETIME      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_job_title` (`job_title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位画像';
