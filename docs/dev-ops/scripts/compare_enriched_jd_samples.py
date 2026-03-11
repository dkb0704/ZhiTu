#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""对比 XLS 中原始 JD 与爬虫 CSV 中的新 JD，生成抽样对比文件。

输出: ../output/enriched_jd_compare_samples.csv
"""

import csv
import random
from pathlib import Path

import xlrd

XLS_PATH = Path('../doc/a13基于AI的大学生职业规划智能体-JD采样数据.xls')
CSV_PATH = Path('../output/enriched_jd_all_fast.csv')
OUT_PATH = Path('../output/enriched_jd_compare_samples.csv')
SAMPLE_SIZE = 80  # 抽样条数，可按需调整


def load_xls_descriptions():
    """返回 job_code -> 原始 JD 文本 的映射。"""
    wb = xlrd.open_workbook(str(XLS_PATH))
    sheet = wb.sheet_by_index(0)
    mapping = {}
    for row_idx in range(1, sheet.nrows):
        row = [sheet.cell_value(row_idx, c) for c in range(sheet.ncols)]
        job_code = str(row[7]).strip() if len(row) > 7 and row[7] else ''
        desc = str(row[8]).strip() if len(row) > 8 and row[8] else ''
        if job_code:
            mapping[job_code] = desc
    return mapping


def normalize_text(s: str) -> str:
    s = s.replace('\r', '\n')
    # 合并多余空行
    while '\n\n\n' in s:
        s = s.replace('\n\n\n', '\n\n')
    return s.strip()


def main():
    xls_desc = load_xls_descriptions()

    rows = []
    with CSV_PATH.open('r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for r in reader:
            try:
                new_len = int(r.get('description_len') or 0)
            except ValueError:
                new_len = 0
            if new_len <= 0:
                continue
            job_code = (r.get('job_code') or '').strip()
            if not job_code:
                continue
            old = xls_desc.get(job_code, '')
            rows.append({
                'id': r.get('id'),
                'job_code': job_code,
                'title': r.get('title', ''),
                'source_url': r.get('source_url', ''),
                'old_desc': normalize_text(old),
                'new_desc': normalize_text(r.get('description', '')),
            })

    total = len(rows)
    print(f'total_rows_with_new_desc: {total}')
    if not rows:
        return

    # 随机抽样，最多 SAMPLE_SIZE 条
    k = min(SAMPLE_SIZE, total)
    samples = random.sample(rows, k)

    # 计算长度与差异，并写出
    with OUT_PATH.open('w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow([
            'id', 'job_code', 'title', 'source_url',
            'old_len', 'new_len', 'delta_len',
            'old_desc_snippet', 'new_desc_snippet',
        ])
        for r in samples:
            old_desc = r['old_desc']
            new_desc = r['new_desc']
            old_len = len(old_desc)
            new_len = len(new_desc)
            delta = new_len - old_len
            writer.writerow([
                r['id'],
                r['job_code'],
                r['title'],
                r['source_url'],
                old_len,
                new_len,
                delta,
                (old_desc[:200] + ('...' if len(old_desc) > 200 else '')),
                (new_desc[:200] + ('...' if len(new_desc) > 200 else '')),
            ])

    print(f'written samples: {k} -> {OUT_PATH}')


if __name__ == '__main__':
    main()
