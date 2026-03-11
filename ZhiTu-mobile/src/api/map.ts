/**
 * 岗位图谱相关 API
 * 后端接入：GET /job/career-map 返回 CareerTrack[] 即可，结构见 docs/岗位图谱接入后端说明.md
 */
import { request } from '@/utils/request'
import type { CareerTrack } from '@/pages/map/career-map-data'

/**
 * 获取岗位图谱数据（职业线 + 节点 + 转岗关系）
 * 若后端未实现或返回空，前端会使用本地预设数据。
 */
export const getCareerMapAPI = () => {
  return request<CareerTrack[]>({
    url: '/job/career-map',
    method: 'GET'
  })
}
