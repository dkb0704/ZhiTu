#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
从岗位来源链接爬取完整 JD，更新数据库或导出文件。

数据源：MySQL job_position 表（需先运行 import_jobs.py 导入 xls）。
对每条 source_url 非空的记录请求页面，抽取正文作为完整 description，写回 DB 或导出 CSV。

使用:
    pip3 install requests beautifulsoup4 pymysql
    # 仅抓取不写库，限制 5 条
    python3 crawl_jd.py --dry-run --limit 5
    # 抓取并更新数据库（默认每批间隔 2 秒）
    python3 crawl_jd.py
    # 导出为 CSV 不更新 DB
    python3 crawl_jd.py --output enriched_jd.csv --limit 100
    # 只处理尚未完善的（当前 description 长度 < 500 的）
    python3 crawl_jd.py --skip-enriched 500

说明：若目标站点 JD 由前端 JS 渲染，requests 拿到的 HTML 可能为空或很短，需改用 Selenium/Playwright 或分析接口请求。
"""

import argparse
import re
import sys
import time
from urllib.parse import urlparse

import pymysql
import requests
from bs4 import BeautifulSoup

# ===== 配置（与 import_jobs 一致）=====
DB_CONFIG = {
    'host': '127.0.0.1',
    'port': 3306,
    'user': 'root',
    'password': 'dkb4sb!!',
    'database': 'zhitu',
    'charset': 'utf8mb4',
}

REQUEST_DELAY_SEC = 2
REQUEST_TIMEOUT = 15
USER_AGENT = (
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) '
    'AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36'
)

# 常见招聘站点的 JD 容器选择器（按优先级尝试）
JOB_BODY_SELECTORS = [
    # 通用
    '[class*="job-detail"]', '[class*="jobDetail"]', '[class*="position-detail"]',
    '[class*="job_desc"]', '[class*="jobDesc"]', '[id*="job-detail"]', '[id*="jobDetail"]',
    '[class*="detail-content"]', '[class*="content-detail"]', '[class*="job-content"]',
    'article', '.content', '.detail', '.description', '.job-description',
    # 51job、智联等
    '.tBorderTop_box', '.bmsg.job_msg', '.tCompany_main',
]
# 页面中若包含这些关键词，其父容器往往就是 JD 正文
JD_KEYWORDS = ['岗位职责', '职位描述', '工作内容', '任职要求', '岗位要求', '工作职责']

def fetch_html(url: str):
    """请求 URL，返回解码后的 HTML 文本；失败返回 None。"""
    headers = {
        'User-Agent': USER_AGENT,
        'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8',
    }
    try:
        r = requests.get(
            url,
            headers=headers,
            timeout=REQUEST_TIMEOUT,
            allow_redirects=True,
        )
        r.raise_for_status()
        # 优先用响应声明编码，否则用 apparent
        r.encoding = r.apparent_encoding or 'utf-8'
        return r.text
    except Exception as e:
        print(f"  [FETCH_ERROR] {url[:60]}... -> {e}", file=sys.stderr)
        return None


def extract_jd_zhaopin(soup: BeautifulSoup) -> str:
    """智联招聘 (zhaopin.com) 职位详情页：从「职位描述」节截取到「工作地点」等下一节前。"""
    # 情况一：职位彻底走丢（页面只剩推荐位等），不再尝试解析 JD
    # 例如：https://www.zhaopin.com/jobdetail/CC640419780J40814291403.htm?...
    full_text = soup.get_text(separator='\n', strip=True)
    if '非常抱歉，该职位信息已走丢' in full_text:
        return '__INVALID_ZHAOPIN_JOB__'
    # 情况二：“该职位已失效，看看其他机会吧”——但页面仍然保留完整的职位描述
    # 例如：https://www.zhaopin.com/jobdetail/CC120776610J40817210808.htm?...
    # 这类页面仍然要继续往下解析 JD，因此不提前返回。

    # 找包含「职位描述」的节点，取其所在区块的全文，再按下一节标题截断
    for tag in soup.find_all(string=re.compile(r'职位描述\s*[：:]?')):
        parent = tag.parent
        for _ in range(8):  # 向上几层找足够大的容器
            if parent is None:
                break
            text = parent.get_text(separator='\n', strip=True)
            if '职位描述' not in text or len(text) < 50:
                parent = getattr(parent, 'parent', None)
                continue
            # 从「职位描述」之后开始，截到真正的下一节（如页面下方的大标题「工作地点」等）
            try:
                idx = text.index('职位描述')
                start = idx + len('职位描述')
                rest = text[start:].strip()
                # 去掉可能紧跟的冒号、换行等
                rest = re.sub(r'^[：:\s\n]+', '', rest)
                # 若文中出现“查看全部”提示，只删除该提示本身，保留其前后的有效内容
                rest = rest.replace('查看全部', '')
                # 按行扫描，遇到“真正的下一节标题”再截断：
                # 规则：该行本身就是「工作地点/认证资质/相似职位/职位发布者/公司介绍/联系我们」等，不以序号起头
                section_titles = {'工作地点', '认证资质', '相似职位', '职位发布者', '公司介绍', '联系我们'}
                lines = rest.splitlines()
                kept_lines = []
                for line in lines:
                    stripped = line.strip()
                    # 判定是否是章节标题行（避免把“6、工作地点：东京”误判为章节）
                    is_numbered = bool(re.match(r'^[0-9０-９一二三四五六七八九十]+[、.\s]', stripped))
                    is_section = stripped in section_titles
                    if is_section and not is_numbered:
                        break
                    kept_lines.append(line)
                rest = '\n'.join(kept_lines).strip()
                if len(rest) > 100:
                    return _normalize_text(rest)
            except (ValueError, AttributeError):
                pass
            parent = getattr(parent, 'parent', None)
    return ''


def extract_jd_from_html(html: str, url: str) -> str:
    """从 HTML 中抽取 JD 正文，尽量去掉导航、广告、脚本。"""
    soup = BeautifulSoup(html, 'html.parser')

    # 去掉 script / style
    for tag in soup(['script', 'style']):
        tag.decompose()

    # 0) 智联招聘：优先用站点专用解析
    if 'zhaopin.com' in url:
        jd = extract_jd_zhaopin(soup)
        if jd == '__INVALID_ZHAOPIN_JOB__':
            # 失效职位页：直接返回空，不再走通用解析
            return ''
        if jd:
            return jd

    # 1) 尝试已知选择器
    for sel in JOB_BODY_SELECTORS:
        try:
            nodes = soup.select(sel)
            for node in nodes:
                text = node.get_text(separator='\n', strip=True)
                # 要求像正文：足够长且包含常见 JD 关键词
                if len(text) > 200 and any(kw in text for kw in JD_KEYWORDS):
                    return _normalize_text(text)
        except Exception:
            continue

    # 2) 找包含 JD 关键词的节点，取其中文本最长的
    best = ''
    for kw in JD_KEYWORDS:
        for tag in soup.find_all(string=re.compile(re.escape(kw))):
            parent = tag.parent
            while parent and parent.name in ('div', 'section', 'article', 'main', 'td'):
                text = parent.get_text(separator='\n', strip=True)
                if len(text) > len(best) and len(text) > 100:
                    best = text
                parent = parent.parent
    if best:
        return _normalize_text(best)

    # 3) 回退：取 body 中连续段落最长的块
    body = soup.find('body')
    if body:
        text = body.get_text(separator='\n', strip=True)
        if len(text) > 100:
            return _normalize_text(text)

    return _normalize_text(soup.get_text(separator='\n', strip=True))


def _normalize_text(s: str) -> str:
    """合并多余空行、去掉首尾空白。"""
    if not s:
        return ''
    s = re.sub(r'\n{3,}', '\n\n', s)
    return s.strip()


def get_rows_from_db(limit, skip_enriched_min_len):
    """从 DB 读取 (id, job_code, source_url, title)。只取 source_url 非空。"""
    conn = pymysql.connect(**DB_CONFIG)
    cursor = conn.cursor()
    sql = """
        SELECT id, job_code, source_url, title
        FROM job_position
        WHERE source_url IS NOT NULL AND source_url != ''
    """
    if skip_enriched_min_len is not None:
        sql += " AND (description IS NULL OR CHAR_LENGTH(description) < %s)"
    sql += " ORDER BY id"
    if limit is not None:
        sql += f" LIMIT {limit}"
    if skip_enriched_min_len is not None:
        cursor.execute(sql, (skip_enriched_min_len,))
    else:
        cursor.execute(sql)
    rows = cursor.fetchall()
    cursor.close()
    conn.close()
    return rows


def update_description(rid: int, description: str) -> None:
    """更新 job_position.description。"""
    conn = pymysql.connect(**DB_CONFIG)
    cursor = conn.cursor()
    cursor.execute(
        'UPDATE job_position SET description = %s WHERE id = %s',
        (description, rid),
    )
    conn.commit()
    cursor.close()
    conn.close()


def main():
    parser = argparse.ArgumentParser(description='从来源链接爬取完整 JD 并更新库或导出')
    parser.add_argument('--dry-run', action='store_true', help='只抓取并打印长度，不写库')
    parser.add_argument('--limit', type=int, default=None, help='最多处理条数（默认全部）')
    parser.add_argument('--output', type=str, default=None, help='导出 CSV 路径（不写库）')
    parser.add_argument('--delay', type=float, default=REQUEST_DELAY_SEC, help='请求间隔秒数')
    parser.add_argument('--skip-enriched', type=int, default=None, metavar='N',
                        help='仅处理 description 长度 < N 的记录（相当于“只完善未完善的”）')
    args = parser.parse_args()

    rows = get_rows_from_db(args.limit, args.skip_enriched)
    total = len(rows)
    print(f'待处理 {total} 条（source_url 非空）')

    if args.output:
        import csv
        out_f = open(args.output, 'w', newline='', encoding='utf-8')
        writer = csv.writer(out_f)
        writer.writerow(['id', 'job_code', 'title', 'source_url', 'description_len', 'description'])

    updated = 0
    for i, (rid, job_code, source_url, title) in enumerate(rows, 1):
        print(f'[{i}/{total}] {title[:40]}...')
        html = fetch_html(source_url)
        if not html:
            if args.output:
                writer.writerow([rid, job_code, title, source_url, 0, ''])
            continue

        jd = extract_jd_from_html(html, source_url)
        if args.dry_run:
            print(f'  抽取长度: {len(jd)} 字符')
        elif args.output:
            writer.writerow([rid, job_code, title, source_url, len(jd), jd])
        else:
            update_description(rid, jd)
            updated += 1
            print(f'  已更新 description 长度: {len(jd)}')

        time.sleep(args.delay)

    if args.output:
        out_f.close()
        print(f'\n已导出到 {args.output}')
    elif not args.dry_run:
        print(f'\n已更新 {updated} 条记录')

    return 0


if __name__ == '__main__':
    sys.exit(main() or 0)
