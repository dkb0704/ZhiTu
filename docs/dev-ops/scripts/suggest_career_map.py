#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
岗位图谱 AI 建议脚本：
  1. 从 a13 开头的 xls 中抽取「岗位名称」去重；
  2. 可选：调用 OpenAI 根据岗位名建议「职业线 + 升岗顺序 + 转岗选项」；
  3. 输出 JSON（供人工审核）或写入 DB（需配置 pymysql）。

使用前：
  pip3 install xlrd openai pymysql

环境变量（可选）：
  OPENAI_API_KEY  调用 OpenAI 时必填
  OPENAI_BASE_URL 国内可设为代理地址

示例：
  python3 suggest_career_map.py                    # 只抽岗位名，打印 + 写 unique_titles.txt
  python3 suggest_career_map.py --ai               # 用 AI 建议图谱结构，输出 career_map_suggested.json
  python3 suggest_career_map.py --ai --write-db    # AI 建议后写入数据库（慎用，建议先 --ai 审阅）
"""

import argparse
import json
import os
import re
import sys
from pathlib import Path

# 脚本所在目录
SCRIPT_DIR = Path(__file__).resolve().parent
DOC_DIR = SCRIPT_DIR.parent / "doc"
XLS_GLOB = "a13*.xls"

# 默认 DB 配置（与 import_jobs 一致，可按需改）
DB_CONFIG = {
    "host": "127.0.0.1",
    "port": 3306,
    "user": "root",
    "password": os.environ.get("DB_PASSWORD", "dkb4sb!!"),
    "database": "zhitu",
    "charset": "utf8mb4",
}


def find_xls():
    """查找 a13 开头的 xls 文件。"""
    for f in DOC_DIR.glob(XLS_GLOB):
        return f
    return None


def extract_unique_titles(xls_path):
    """从 xls 第一列抽取岗位名称并去重。"""
    try:
        import xlrd
    except ImportError:
        print("请安装: pip3 install xlrd", file=sys.stderr)
        sys.exit(1)
    wb = xlrd.open_workbook(str(xls_path))
    sheet = wb.sheet_by_index(0)
    titles = set()
    for r in range(1, sheet.nrows):
        val = sheet.cell_value(r, 0)
        if val:
            t = str(val).strip()
            if t:
                titles.add(t)
    return sorted(titles)


def call_ai_suggest(titles, max_titles=200):
    """
    调用 OpenAI 根据岗位名列表，生成建议的 CareerTrack[] 结构（升岗顺序 + 转岗建议）。
    返回 list[dict]，与前端 CareerTrack 结构一致。
    """
    api_key = os.environ.get("OPENAI_API_KEY")
    if not api_key:
        print("未设置 OPENAI_API_KEY，跳过 AI 建议。", file=sys.stderr)
        return None
    try:
        import openai
    except ImportError:
        print("请安装: pip3 install openai", file=sys.stderr)
        return None
    client = openai.OpenAI(api_key=api_key, base_url=os.environ.get("OPENAI_BASE_URL"))
    sample = titles[:max_titles]
    prompt = f"""你是一个职业规划专家。下面是中国招聘数据中的岗位名称列表（已去重），请按「职业线」整理成岗位图谱结构。

要求：
1. 将岗位归纳为若干条「职业线」，例如：产品线、技术线、运营线、设计线、市场线等。
2. 每条线内按「升岗顺序」排列节点（从初级到高级）。
3. 对部分关键节点（如经理、高级、总监等）给出「可转岗」建议：2～4 个目标岗位 + 一句简短描述（如「从幕后到台前」）。
4. 只输出一个合法的 JSON 数组，不要 markdown 代码块包裹。格式与下面完全一致：

[
  {{
    "id": "product",
    "name": "产品线",
    "nodes": [
      {{ "id": "p1", "title": "产品实习生" }},
      {{ "id": "p2", "title": "产品助理" }},
      {{
        "id": "p3",
        "title": "产品经理",
        "hasTransfer": true,
        "transferOptions": [
          {{ "title": "运营经理", "desc": "从幕后到台前" }}
        ]
      }}
    ]
  }}
]

岗位名称列表（共 {len(sample)} 个）：
{json.dumps(sample, ensure_ascii=False)}
"""
    resp = client.chat.completions.create(
        model=os.environ.get("OPENAI_MODEL", "gpt-4o-mini"),
        messages=[{"role": "user", "content": prompt}],
        temperature=0.3,
    )
    text = resp.choices[0].message.content.strip()
    # 去掉可能的 markdown 代码块
    if text.startswith("```"):
        text = re.sub(r"^```\w*\n?", "", text)
        text = re.sub(r"\n?```\s*$", "", text)
    return json.loads(text)


def write_career_map_to_db(tracks):
    """将建议的 CareerTrack[] 写入 career_track / career_node / career_transfer_option。"""
    try:
        import pymysql
    except ImportError:
        print("请安装: pip3 install pymysql", file=sys.stderr)
        return False
    conn = pymysql.connect(**DB_CONFIG)
    cursor = conn.cursor()
    try:
        # 清空旧数据（可选，慎用）
        cursor.execute("DELETE FROM career_transfer_option")
        cursor.execute("DELETE FROM career_node")
        cursor.execute("DELETE FROM career_track")
        conn.commit()
        sort_track = 0
        for t in tracks:
            sort_track += 1
            cursor.execute(
                "INSERT INTO career_track (code, name, sort_order) VALUES (%s, %s, %s)",
                (t["id"], t["name"], sort_track),
            )
            track_id = cursor.lastrowid
            for i, n in enumerate(t["nodes"], start=1):
                has_transfer = 1 if n.get("hasTransfer") and n.get("transferOptions") else 0
                code = n.get("id") or f"n{i}"
                cursor.execute(
                    "INSERT INTO career_node (track_id, code, title, sort_order, has_transfer) VALUES (%s, %s, %s, %s, %s)",
                    (track_id, code, n["title"], i, has_transfer),
                )
                node_id = cursor.lastrowid
                for j, opt in enumerate(n.get("transferOptions") or [], start=1):
                    cursor.execute(
                        "INSERT INTO career_transfer_option (node_id, target_title, target_desc, sort_order) VALUES (%s, %s, %s, %s)",
                        (node_id, opt.get("title", ""), opt.get("desc") or "", j),
                    )
        conn.commit()
        print("已写入数据库。")
        return True
    except Exception as e:
        conn.rollback()
        print(f"写入失败: {e}", file=sys.stderr)
        return False
    finally:
        cursor.close()
        conn.close()


def main():
    parser = argparse.ArgumentParser(description="岗位图谱：从 xls 抽岗位名 + 可选 AI 建议")
    parser.add_argument("--ai", action="store_true", help="调用 OpenAI 建议职业线/升岗/转岗")
    parser.add_argument("--write-db", action="store_true", help="将结果写入数据库（建议先 --ai 审阅 JSON）")
    parser.add_argument("--out", default="career_map_suggested.json", help="AI 建议输出 JSON 路径")
    args = parser.parse_args()

    xls_path = find_xls()
    if not xls_path:
        print(f"未找到 {XLS_GLOB}，请将 xls 放在 {DOC_DIR}", file=sys.stderr)
        sys.exit(1)
    print(f"使用 xls: {xls_path}")
    titles = extract_unique_titles(xls_path)
    print(f"去重后岗位数: {len(titles)}")
    out_txt = SCRIPT_DIR / "unique_titles.txt"
    with open(out_txt, "w", encoding="utf-8") as f:
        f.write("\n".join(titles))
    print(f"已写入: {out_txt}")

    if args.ai:
        suggested = call_ai_suggest(titles)
        if suggested:
            out_json = SCRIPT_DIR / args.out
            with open(out_json, "w", encoding="utf-8") as f:
                json.dump(suggested, f, ensure_ascii=False, indent=2)
            print(f"AI 建议已写入: {out_json}")
            if args.write_db:
                write_career_map_to_db(suggested)
        else:
            sys.exit(2)
    elif args.write_db:
        print("未使用 --ai，无法仅执行 --write-db。请先运行 --ai 并审阅 JSON 后再决定是否写入。", file=sys.stderr)
        sys.exit(2)


if __name__ == "__main__":
    main()
