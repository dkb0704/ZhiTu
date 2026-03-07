/**
 * 岗位图谱 - 预设数据（晋升链路 + 转岗关系）
 *
 * 当前为前端预设，后续可改为：
 * - 岗位节点来自后端 job 表/岗位库
 * - 晋升关系、转岗关系依赖对 job 数据的处理 + AI 分析生成
 */

export interface TransferOption {
  title: string
  desc: string
}

export interface CareerNode {
  id: string
  title: string
  hasTransfer?: boolean
  transferOptions?: TransferOption[]
}

export interface CareerTrack {
  id: string
  name: string
  nodes: CareerNode[]
}

export const CAREER_TRACKS: CareerTrack[] = [
  {
    id: 'product',
    name: '产品线',
    nodes: [
      { id: 'p1', title: '产品实习生' },
      { id: 'p2', title: '产品助理' },
      { id: 'p3', title: '产品经理', hasTransfer: true, transferOptions: [
        { title: '运营经理', desc: '从幕后到台前' },
        { title: '项目经理', desc: '侧重落地执行' },
        { title: '数据分析师', desc: '深耕数据驱动' }
      ]},
      { id: 'p4', title: '高级产品经理', hasTransfer: true, transferOptions: [
        { title: '产品总监', desc: '管理产品线' },
        { title: '创业合伙人', desc: '独立负责产品' }
      ]},
      { id: 'p5', title: '产品总监' },
      { id: 'p6', title: 'VP/CPO' }
    ]
  },
  {
    id: 'operation',
    name: '运营线',
    nodes: [
      { id: 'o1', title: '运营实习' },
      { id: 'o2', title: '运营专员' },
      { id: 'o3', title: '运营经理', hasTransfer: true, transferOptions: [
        { title: '产品经理', desc: '懂用户的产品人' },
        { title: '市场经理', desc: '从运营到市场' }
      ]},
      { id: 'o4', title: '运营负责人' },
      { id: 'o5', title: '运营总监', hasTransfer: true, transferOptions: [
        { title: '事业部负责人', desc: '负责业务线整体' },
        { title: '产品总监', desc: '从运营到产品' }
      ]},
      { id: 'o6', title: '事业部负责人' }
    ]
  },
  {
    id: 'tech',
    name: '技术线',
    nodes: [
      { id: 't1', title: '开发实习' },
      { id: 't2', title: '初级开发' },
      { id: 't3', title: '中级开发' },
      { id: 't4', title: '高级开发', hasTransfer: true, transferOptions: [
        { title: '技术经理', desc: '转管理路线' },
        { title: '架构师', desc: '走专家路线' },
        { title: '技术合伙人', desc: '创业技术核心' }
      ]},
      { id: 't5', title: '技术经理' },
      { id: 't6', title: '技术总监' },
      { id: 't7', title: 'CTO' }
    ]
  },
  {
    id: 'design',
    name: '设计线',
    nodes: [
      { id: 'd1', title: '设计实习' },
      { id: 'd2', title: 'UI 设计师' },
      { id: 'd3', title: '高级设计师', hasTransfer: true, transferOptions: [
        { title: '产品经理', desc: '从设计到产品' },
        { title: '设计主管', desc: '带设计团队' }
      ]},
      { id: 'd4', title: '设计主管' },
      { id: 'd5', title: '设计总监' }
    ]
  }
]

/**
 * 藤蔓 SVG 路径上的关键通过点（原始 viewBox: 69x590）
 * 从 Figma 导出的贝塞尔曲线在这些 y 值附近横穿：
 *   y≈2:  x≈2   (起点左侧)
 *   y≈46: x≈48  (右偏)
 *   y≈167: x≈41 (中偏)
 *   y≈296: x≈31 (左偏)
 *   y≈414: x≈41 (中偏)
 *   y≈588: x≈48 (右偏终点)
 */
export const VINE_POINTS = [
  { y: 2, x: 2 },
  { y: 46, x: 48 },
  { y: 167, x: 41 },
  { y: 296, x: 31 },
  { y: 414, x: 41 },
  { y: 588, x: 48 },
]
