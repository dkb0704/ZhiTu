-- ============================================================
-- 岗位数据表
-- ============================================================
USE zhitu;

DROP TABLE IF EXISTS `job_position`;
CREATE TABLE `job_position` (
    `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`         VARCHAR(128)  NOT NULL COMMENT '岗位名称',
    `location`      VARCHAR(128)  DEFAULT NULL COMMENT '工作地点',
    `salary_range`  VARCHAR(64)   DEFAULT NULL COMMENT '薪资范围原始文本',
    `salary_min`    INT           DEFAULT NULL COMMENT '薪资下限（元）',
    `salary_max`    INT           DEFAULT NULL COMMENT '薪资上限（元）',
    `company_name`  VARCHAR(255)  DEFAULT NULL COMMENT '公司名称',
    `industry`      VARCHAR(255)  DEFAULT NULL COMMENT '所属行业',
    `company_size`  VARCHAR(64)   DEFAULT NULL COMMENT '公司规模',
    `company_type`  VARCHAR(64)   DEFAULT NULL COMMENT '公司类型/融资阶段',
    `job_code`      VARCHAR(128)  DEFAULT NULL COMMENT '岗位编码',
    `description`   TEXT          DEFAULT NULL COMMENT '岗位详情 JD',
    `update_date`   VARCHAR(32)   DEFAULT NULL COMMENT '原始更新日期',
    `company_desc`  TEXT          DEFAULT NULL COMMENT '公司详情',
    `source_url`    VARCHAR(512)  DEFAULT NULL COMMENT '来源地址',
    `create_time`   DATETIME      NOT NULL COMMENT '入库时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_job_code` (`job_code`),
    INDEX `idx_title` (`title`),
    INDEX `idx_location` (`location`),
    INDEX `idx_salary_min` (`salary_min`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位数据';
