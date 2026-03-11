-- 岗位图谱：职业线、节点、转岗关系（升岗顺序由 sort_order 保证）
USE zhitu;

-- 职业线（如产品线、技术线）
CREATE TABLE IF NOT EXISTS career_track (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    code        VARCHAR(32)  NOT NULL COMMENT '唯一编码，如 product/tech',
    name        VARCHAR(64)  NOT NULL COMMENT '展示名',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '线路排序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职业线';

-- 节点（一条线上的岗位，升岗顺序由 sort_order 保证）
CREATE TABLE IF NOT EXISTS career_node (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    track_id    BIGINT       NOT NULL COMMENT '所属职业线',
    code        VARCHAR(32)  NOT NULL COMMENT '节点编码，如 p1/t3',
    title       VARCHAR(128) NOT NULL COMMENT '岗位名称',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '节点顺序（升岗顺序）',
    has_transfer TINYINT     NOT NULL DEFAULT 0 COMMENT '是否可转岗 0否1是',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_track_code (track_id, code),
    KEY idx_track (track_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职业线节点';

-- 转岗建议（某节点可转去的岗位 + 简短描述）
CREATE TABLE IF NOT EXISTS career_transfer_option (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    node_id     BIGINT       NOT NULL COMMENT '来源节点',
    target_title VARCHAR(128) NOT NULL COMMENT '目标岗位名称',
    target_desc  VARCHAR(255) DEFAULT NULL COMMENT '简短描述，如「从幕后到台前」',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '展示顺序',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_node (node_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='转岗建议';
