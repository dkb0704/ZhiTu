/**
 * 用户资料相关 API (profile.ts)
 *
 * 定义了个人简历/资料接口的：
 *   - 请求参数结构 (UpdateProfileRequest)
 *   - 响应数据结构 (UserProfile)
 *   - updateProfileAPI / getProfileAPI 函数
 */
import { request } from '@/utils/request'

/**
 * 更新个人资料请求参数（部分更新 — 只传需要更新的字段即可）
 */
export interface UpdateProfileRequest {
    major?: string            // 专业
    education?: string        // 学历（最高学历）
    educationBackground?: string  // 教育背景 JSON：[{degree,school},...]
    grade?: string            // 年级
    baseCities?: string       // 意向城市 JSON 数组
    targetPosition?: string   // 意向岗位
    gpa?: string              // 绩点（如 "3.5/5.0"）
    politicalStatus?: string  // 政治面貌
    honors?: string           // 荣誉及证书 JSON 数组
    projects?: string         // 项目经历 JSON 数组
    campus?: string           // 校园经历 JSON 数组
    skills?: string           // 技能 JSON 数组
    selfEvaluation?: string   // 自我评价
    internships?: string      // 实习经历 JSON 数组
}

/**
 * 个人资料完整结构（GET 返回）
 */
export interface UserProfile {
    id: number
    userId: number
    major: string
    education: string
    educationBackground?: string  // 教育背景 JSON，可选
    grade: string
    baseCities: string
    targetPosition: string
    gpa: string
    politicalStatus: string
    honors: string
    projects: string
    campus: string
    skills: string
    selfEvaluation: string
    internships: string
    createTime: string
    updateTime: string
}

/**
 * 更新个人资料
 * PUT /user/profile
 */
export const updateProfileAPI = (data: UpdateProfileRequest) => {
    return request<void>({
        url: '/user/profile',
        method: 'PUT',
        data
    })
}

/**
 * 获取个人资料
 * GET /user/profile
 */
export const getProfileAPI = () => {
    return request<UserProfile>({
        url: '/user/profile',
        method: 'GET'
    })
}
