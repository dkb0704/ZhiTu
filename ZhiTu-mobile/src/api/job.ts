/**
 * 岗位相关 API (job.ts)
 * 
 * 定义了岗位列表接口的：
 *   - 查询参数结构 (JobQuery)
 *   - 岗位数据结构 (JobEntity)
 *   - getJobList 函数（调用后端 /job/list 分页接口）
 */
import { request } from '@/utils/request'

/**
 * 岗位查询参数
 * page     - 第几页（从 1 开始）
 * size     - 每页多少条
 * title    - 按岗位名称搜索（可选）
 * industry - 按行业搜索（可选）
 */
export interface JobQuery {
    page?: number
    size?: number
    title?: string
    industry?: string
}

/**
 * 单条岗位数据的完整结构
 * 字段与后端 JobPositionEntity 实体类一一对应
 */
export interface JobEntity {
    id: string              // 岗位唯一 ID
    title: string           // 岗位名称（如"前端开发工程师"）
    location: string        // 工作地点（如"北京"）
    salaryRange: string     // 薪资范围文本（如"15K-25K"）
    salaryMin: number       // 最低薪资（数值）
    salaryMax: number       // 最高薪资（数值）
    companyName: string     // 公司名称
    industry: string        // 所属行业
    companySize: string     // 公司规模（如"100-499人"）
    companyType: string     // 公司类型（如"民营"）
    jobCode: string         // 岗位编码
    description: string     // 岗位描述详情
    updateDate: string      // 岗位更新日期
    companyDesc: string     // 公司简介
    sourceUrl: string       // 原始招聘链接
    createTime: string      // 数据入库时间
}

/**
 * 获取岗位分页列表
 * 请求方式：GET /job/list
 * 查询参数：{ page, size, title?, industry? }
 * 返回值：{ list: 岗位数组, total: 总条数 }
 */
export const getJobList = (params: JobQuery) => {
    return request<{ list: JobEntity[], total: number }>({
        url: '/job/list',
        method: 'GET',
        data: params
    })
}
