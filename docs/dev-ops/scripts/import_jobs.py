#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
岗位数据导入脚本：读取 XLS → 清洗 → 批量 INSERT 到 MySQL job_position 表。

使用方法:
    pip3 install xlrd pymysql
    python3 import_jobs.py

默认连接参数与 application-dev.yml 保持一致。
"""

import re
import xlrd
import pymysql
from datetime import datetime

# ===== 配置 =====
DB_CONFIG = {
    'host': '127.0.0.1',
    'port': 3306,
    'user': 'root',
    'password': 'dkb4sb!!',
    'database': 'zhitu',
    'charset': 'utf8mb4',
}

XLS_PATH = '../doc/a13基于AI的大学生职业规划智能体-JD采样数据.xls'
BATCH_SIZE = 500  # 每批插入条数


def parse_salary(salary_text):
    """解析薪资范围，返回 (min, max)，单位：元。"""
    if not salary_text:
        return None, None

    # 统一去除空格
    text = salary_text.strip()

    # 匹配模式：3000-4000元、3-5千/月、10-15K、1-2万/月 等
    # 模式一：X-Y元
    m = re.match(r'(\d+)-(\d+)\s*元', text)
    if m:
        return int(m.group(1)), int(m.group(2))

    # 模式二：X-Y千
    m = re.match(r'(\d+(?:\.\d+)?)-(\d+(?:\.\d+)?)\s*千', text)
    if m:
        return int(float(m.group(1)) * 1000), int(float(m.group(2)) * 1000)

    # 模式三：X-YK
    m = re.match(r'(\d+(?:\.\d+)?)-(\d+(?:\.\d+)?)\s*[kK]', text)
    if m:
        return int(float(m.group(1)) * 1000), int(float(m.group(2)) * 1000)

    # 模式四：X-Y万
    m = re.match(r'(\d+(?:\.\d+)?)-(\d+(?:\.\d+)?)\s*万', text)
    if m:
        return int(float(m.group(1)) * 10000), int(float(m.group(2)) * 10000)

    return None, None


def clean_html(text):
    """去掉简单 HTML 标签（<br> 等），保留纯文本。"""
    if not text:
        return text
    return re.sub(r'<[^>]+>', '\n', text).strip()


def main():
    # 读取 XLS
    wb = xlrd.open_workbook(XLS_PATH)
    sheet = wb.sheet_by_index(0)
    total = sheet.nrows - 1
    print(f'读取到 {total} 条岗位数据')

    # 连接数据库
    conn = pymysql.connect(**DB_CONFIG)
    cursor = conn.cursor()

    now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')

    insert_sql = """
        INSERT IGNORE INTO job_position
        (title, location, salary_range, salary_min, salary_max,
         company_name, industry, company_size, company_type,
         job_code, description, update_date, company_desc, source_url, create_time)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """

    batch = []
    inserted = 0

    for row_idx in range(1, sheet.nrows):
        row = [sheet.cell_value(row_idx, c) for c in range(sheet.ncols)]

        title = str(row[0]).strip()
        location = str(row[1]).strip() if row[1] else None
        salary_range = str(row[2]).strip() if row[2] else None
        salary_min, salary_max = parse_salary(salary_range)
        company_name = str(row[3]).strip() if row[3] else None
        industry = str(row[4]).strip() if row[4] else None
        company_size = str(row[5]).strip() if row[5] else None
        company_type = str(row[6]).strip() if row[6] else None
        job_code = str(row[7]).strip() if row[7] else None
        description = clean_html(str(row[8])) if row[8] else None
        update_date = str(row[9]).strip() if row[9] else None
        company_desc = clean_html(str(row[10])) if row[10] else None
        source_url = str(row[11]).strip() if row[11] else None

        batch.append((
            title, location, salary_range, salary_min, salary_max,
            company_name, industry, company_size, company_type,
            job_code, description, update_date, company_desc, source_url, now
        ))

        if len(batch) >= BATCH_SIZE:
            cursor.executemany(insert_sql, batch)
            conn.commit()
            inserted += len(batch)
            print(f'  已导入 {inserted}/{total}')
            batch = []

    # 处理剩余
    if batch:
        cursor.executemany(insert_sql, batch)
        conn.commit()
        inserted += len(batch)

    # 验证
    cursor.execute('SELECT COUNT(*) FROM job_position')
    count = cursor.fetchone()[0]
    print(f'\n导入完成！数据库中共 {count} 条岗位数据。')

    cursor.close()
    conn.close()


if __name__ == '__main__':
    main()
