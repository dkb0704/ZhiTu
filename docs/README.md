# docs 目录结构说明

## specs/
- 服创赛题与岗位图谱相关文档：
  - 服创赛题-A13.pdf
  - 岗位图谱与AI接入-当前可做事项.md
  - 岗位图谱与个性化推荐方案.md
  - 岗位图谱接入后端说明.md

## dev-ops/
面向部署、运维与数据脚本的资料。

- app/
  - start.sh / stop.sh：本地或服务器上启动、停止应用的脚本。

- docker/
  - docker-compose-app.yml：应用相关服务的 compose 配置。
  - docker-compose-environment.yml：本地开发环境依赖（如 MySQL、Redis 等）。
  - docker-compose-environment-aliyun.yml：阿里云环境的 compose 配置。

- sql/
  - job_position_create.sql：岗位表结构。
  - user.sql：用户基础表结构。
  - user_add_profile_columns.sql：用户画像相关字段变更。
  - user_add_preference_and_assessment.sql：偏好与测评相关字段变更。
  - user_split_refactor.sql：用户表拆分 / 重构相关 SQL。

- scripts/
  - import_jobs.py：从 JD xls 导入岗位数据到 job_position 表。
  - crawl_jd.py：根据 source_url 爬取完整 JD，更新 job_position.description 或导出 CSV。
  - suggest_career_map.py：基于岗位数据和大模型生成职业发展路径建议。
  - fetch_figma.py：从 Figma 拉取设计文件（配合 figma_design.json / job_browse_frame.json 使用）。

- output/
  - enriched_jd_sample*.csv：JD 爬虫脚本的中间结果与样本导出文件。

- design/
  - figma_design.json：从 Figma 导出的整体设计 JSON。
  - job_browse_frame.json：岗位浏览相关的 Figma frame 配置。

- front-images/mobile/
  - mobile_login_page.png / mobile_job_browse_page.png / mobile_home.png：移动端页面截图。
  - logo.jpg / back.png：移动端 Logo 与返回按钮图片。

