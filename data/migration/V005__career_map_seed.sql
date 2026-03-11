-- 岗位图谱种子数据（与前端 career-map-data.ts 预设一致，便于先跑通；后续可由 AI 建议脚本更新）
-- 依赖 V004__career_map_tables.sql 已执行

INSERT INTO career_track (code, name, sort_order) VALUES
('product', '产品线', 1),
('operation', '运营线', 2),
('tech', '技术线', 3),
('design', '设计线', 4);

SET @t_product = (SELECT id FROM career_track WHERE code = 'product' LIMIT 1);
SET @t_operation = (SELECT id FROM career_track WHERE code = 'operation' LIMIT 1);
SET @t_tech = (SELECT id FROM career_track WHERE code = 'tech' LIMIT 1);
SET @t_design = (SELECT id FROM career_track WHERE code = 'design' LIMIT 1);

INSERT INTO career_node (track_id, code, title, sort_order, has_transfer) VALUES
(@t_product, 'p1', '产品实习生', 1, 0),
(@t_product, 'p2', '产品助理', 2, 0),
(@t_product, 'p3', '产品经理', 3, 1),
(@t_product, 'p4', '高级产品经理', 4, 1),
(@t_product, 'p5', '产品总监', 5, 0),
(@t_product, 'p6', 'VP/CPO', 6, 0),
(@t_operation, 'o1', '运营实习', 1, 0),
(@t_operation, 'o2', '运营专员', 2, 0),
(@t_operation, 'o3', '运营经理', 3, 1),
(@t_operation, 'o4', '运营负责人', 4, 0),
(@t_operation, 'o5', '运营总监', 5, 1),
(@t_operation, 'o6', '事业部负责人', 6, 0),
(@t_tech, 't1', '开发实习', 1, 0),
(@t_tech, 't2', '初级开发', 2, 0),
(@t_tech, 't3', '中级开发', 3, 0),
(@t_tech, 't4', '高级开发', 4, 1),
(@t_tech, 't5', '技术经理', 5, 0),
(@t_tech, 't6', '技术总监', 6, 0),
(@t_tech, 't7', 'CTO', 7, 0),
(@t_design, 'd1', '设计实习', 1, 0),
(@t_design, 'd2', 'UI 设计师', 2, 0),
(@t_design, 'd3', '高级设计师', 3, 1),
(@t_design, 'd4', '设计主管', 4, 0),
(@t_design, 'd5', '设计总监', 5, 0);

-- 转岗建议（来源节点 code 对应上面）
INSERT INTO career_transfer_option (node_id, target_title, target_desc, sort_order)
SELECT n.id, '运营经理', '从幕后到台前', 1 FROM career_node n WHERE n.code = 'p3' AND n.track_id = @t_product
UNION ALL SELECT n.id, '项目经理', '侧重落地执行', 2 FROM career_node n WHERE n.code = 'p3' AND n.track_id = @t_product
UNION ALL SELECT n.id, '数据分析师', '深耕数据驱动', 3 FROM career_node n WHERE n.code = 'p3' AND n.track_id = @t_product;
INSERT INTO career_transfer_option (node_id, target_title, target_desc, sort_order)
SELECT n.id, '产品总监', '管理产品线', 1 FROM career_node n WHERE n.code = 'p4' AND n.track_id = @t_product
UNION ALL SELECT n.id, '创业合伙人', '独立负责产品', 2 FROM career_node n WHERE n.code = 'p4' AND n.track_id = @t_product;
INSERT INTO career_transfer_option (node_id, target_title, target_desc, sort_order)
SELECT n.id, '产品经理', '懂用户的产品人', 1 FROM career_node n WHERE n.code = 'o3' AND n.track_id = @t_operation
UNION ALL SELECT n.id, '市场经理', '从运营到市场', 2 FROM career_node n WHERE n.code = 'o3' AND n.track_id = @t_operation;
INSERT INTO career_transfer_option (node_id, target_title, target_desc, sort_order)
SELECT n.id, '事业部负责人', '负责业务线整体', 1 FROM career_node n WHERE n.code = 'o5' AND n.track_id = @t_operation
UNION ALL SELECT n.id, '产品总监', '从运营到产品', 2 FROM career_node n WHERE n.code = 'o5' AND n.track_id = @t_operation;
INSERT INTO career_transfer_option (node_id, target_title, target_desc, sort_order)
SELECT n.id, '技术经理', '转管理路线', 1 FROM career_node n WHERE n.code = 't4' AND n.track_id = @t_tech
UNION ALL SELECT n.id, '架构师', '走专家路线', 2 FROM career_node n WHERE n.code = 't4' AND n.track_id = @t_tech
UNION ALL SELECT n.id, '技术合伙人', '创业技术核心', 3 FROM career_node n WHERE n.code = 't4' AND n.track_id = @t_tech;
INSERT INTO career_transfer_option (node_id, target_title, target_desc, sort_order)
SELECT n.id, '产品经理', '从设计到产品', 1 FROM career_node n WHERE n.code = 'd3' AND n.track_id = @t_design
UNION ALL SELECT n.id, '设计主管', '带设计团队', 2 FROM career_node n WHERE n.code = 'd3' AND n.track_id = @t_design;
