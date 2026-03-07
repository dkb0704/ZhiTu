/**
 * 认证相关 API (auth.ts)
 * 
 * 定义了登录接口的：
 *   - 请求参数结构 (LoginRequest)
 *   - 响应数据结构 (LoginResponse)
 *   - loginAPI 函数（调用后端 /auth/login 接口）
 */
import { request } from '@/utils/request'

/**
 * 登录请求参数
 * account - 账号（手机号 或 邮箱）
 * password - 密码
 */
export interface LoginRequest {
    account?: string
    password?: string
}

/**
 * 登录成功后，后端返回的数据
 * userId   - 用户唯一 ID
 * token    - JWT 登录令牌（后续所有请求都要带上）
 * nickname - 用户昵称
 */
export interface LoginResponse {
    userId: string
    token: string
    nickname: string
}

/**
 * 调用后端登录接口
 * 请求方式：POST /auth/login
 * 请求体：{ account, password }
 * 返回值：{ userId, token, nickname }
 */
export const loginAPI = (data: LoginRequest) => {
    return request<LoginResponse>({
        url: '/auth/login',
        method: 'POST',
        data
    })
}

/**
 * 注册请求参数
 * nickname - 用户昵称（前端界面的账号/用户名对应后端昵称）
 * password - 密码
 * phone - 手机号
 * email - 邮箱
 */
export interface RegisterRequest {
    nickname?: string
    password?: string
    phone?: string
    email?: string
}

/**
 * 调用后端注册接口
 * 请求方式：POST /auth/register
 */
export const registerAPI = (data: RegisterRequest) => {
    return request<LoginResponse>({
        url: '/auth/register',
        method: 'POST',
        data
    })
}
