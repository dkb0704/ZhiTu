/**
 * 用户偏好相关 API (preference.ts)
 */
import { request } from '@/utils/request'

export interface UpdatePreferenceRequest {
    jobPreferences?: string            // 求职偏好权重 JSON
    softSkills?: string                // 软实力自评 JSON
    mbti?: string                      // MBTI 结果
    holland?: string                   // 霍兰德结果
    bigFive?: string                   // BIG FIVE 结果
}

/**
 * 更新用户偏好及测试结果
 * PUT /user/preference
 */
export const updatePreferenceAPI = (data: UpdatePreferenceRequest) => {
    return request<void>({
        url: '/user/preference',
        method: 'PUT',
        data
    })
}

/**
 * 查询用户偏好及测试结果
 * GET /user/preference
 */
export const getPreferenceAPI = () => {
    return request<any>({
        url: '/user/preference',
        method: 'GET'
    })
}
