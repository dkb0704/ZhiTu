/**
 * 网络请求工具函数 (request.ts)
 * 
 * 作用：对 Uni-app 原生的 uni.request 进行封装
 * 所有 API 请求都经过这个函数统一发出，它会自动：
 *   1. 根据运行环境（H5 浏览器 / 手机 App）设置不同的后端地址
 *   2. 从本地存储中取出登录 Token，自动加到请求头（Authorization）
 *   3. 统一处理成功/失败的响应，弹出错误提示
 * 
 * 泛型 <T> 表示"期望后端返回的数据类型"，调用时指定即可
 */
export const request = <T>(options: UniApp.RequestOptions): Promise<T> => {
    // ========== 1. 确定后端接口的基础地址 ==========
    // H5（浏览器）环境：走 Vite 代理（/api → localhost:8091）
    // App / 小程序环境：直接使用真实后端地址
    let baseURL = '/api'
    // @ts-ignore
    if (typeof plus !== 'undefined' || process.env.UNI_PLATFORM !== 'h5') {
        // 非 H5 环境，需要替换为电脑的局域网 IP 或服务器真实域名
        baseURL = 'http://localhost:8091'
    }

    return new Promise((resolve, reject) => {
        // ========== 2. 自动附加 Token 到请求头 ==========
        const token = uni.getStorageSync('token')  // 从手机本地存储读取 Token
        const customHeader: Record<string, string> = {
            'Content-Type': 'application/json',
            ...options.header as Record<string, string>
        }

        // 如果 Token 存在，就加到 Authorization 头（Bearer 格式）
        if (token) {
            customHeader['Authorization'] = `Bearer ${token}`
        }

        // ========== 3. 发起实际的 HTTP 请求 ==========
        uni.request({
            url: baseURL + options.url,      // 拼接完整 URL
            method: options.method || 'GET', // 默认 GET 请求
            data: options.data,              // 请求体 / 查询参数
            header: customHeader,
            success: (res: any) => {
                const data = res.data
                // 后端约定：code === '0000' 表示业务成功
                if (res.statusCode === 200 && data.code === '0000') {
                    resolve(data.data as T)  // 返回业务数据
                } else {
                    // 业务失败：reject 时带上 message，由调用方统一弹 toast，避免重复或覆盖为「请求失败」
                    const errMsg = data.info || data.message || '请求失败'
                    reject(Object.assign(data, { message: errMsg }))
                }
            },
            fail: (err) => {
                // 网络层失败（断网、超时等）
                const netErr = Object.assign(err || {}, { message: '网络异常' })
                uni.showToast({ title: netErr.message, icon: 'none' })
                reject(netErr)
            }
        })
    })
}
