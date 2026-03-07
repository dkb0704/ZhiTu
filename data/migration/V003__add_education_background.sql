-- 教育背景：本科/硕士/博士等各阶段学校，JSON 数组存储
use zhitu;
ALTER TABLE user_profile ADD COLUMN education_background TEXT DEFAULT NULL COMMENT '教育背景JSON：[{"degree":"本科","school":"XX大学"},...]' AFTER education;
