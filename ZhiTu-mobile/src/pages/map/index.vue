<template>
	<view class="container" :style="mapTuningVars">
		<image src="/static/back.png" class="bg-img" mode="aspectFill" />

		<text class="top-title">职途脉络</text>

		<scroll-view scroll-x class="tab-bar" :show-scrollbar="false">
			<view
				v-for="t in CAREER_TRACKS"
				:key="t.id"
				class="tab-chip"
				:class="{ active: currentTrack?.id === t.id }"
				@click="switchTrack(t)"
			>
				<view class="tab-text-wrap">
					<text class="tab-chip-text">{{ t.nodes[0]?.title }}</text>
					<view v-if="currentTrack?.id === t.id" class="tab-indicator">
						<view class="tab-indicator-v"></view>
						<view class="tab-indicator-h"></view>
					</view>
				</view>
			</view>
		</scroll-view>

		<scroll-view scroll-y class="map-scroll" :show-scrollbar="false">
			<view class="map-canvas" :style="{ height: effectiveCanvasHeight + 'rpx' }">

				<!--
					动态生成的 SVG：直接用 rpx 坐标绘制贝塞尔曲线，
					保证渲染曲线与节点坐标系完全一致，不存在拉伸变形。
				-->
				<image :src="treeSvgDataUri" class="vine-img-full" mode="scaleToFill"
					:style="{ width: '750rpx', height: canvasHeight + 'rpx' }" />

				<view
					v-for="(node, idx) in currentTrack.nodes"
					:key="node.id"
					class="node-row"
					:style="{ top: getNodeTop(idx) + 'rpx' }"
				>
					<view
						v-if="node.hasTransfer"
						class="transfer-hit"
						:style="getTransferHitStyle(idx)"
						@click="openTransfer(node, idx)"
					></view>

					<view
						v-show="!(transferNode && transferIdx === idx)"
						class="node-card"
						:class="{ highlight: transferIdx === idx }"
						:style="getCardStyle(idx)"
					>
						<text class="node-label">{{ node.title }}</text>
					</view>
				</view>

				<template v-if="transferNode">
					<!-- 画布内遮罩：点击空白关闭，在 scroll-view 内故滑动仍可滚动 -->
					<view class="transfer-backdrop" @tap="closeTransfer"></view>
					<view
						class="highlight-card"
						:style="{ top: getNodeTop(transferIdx) + 'rpx', ...getHighlightPos(transferIdx) }"
					>
						<text class="highlight-label">{{ transferNode.title }}</text>
					</view>
					<view
						v-for="(opt, oi) in transferNode.transferOptions"
						:key="'tf-' + oi"
						class="float-card"
						:style="getFloatCardStyle(oi)"
					>
						<view class="fc-top">
							<text class="fc-title">{{ opt.title }}</text>
							<text class="fc-desc">{{ opt.desc }}</text>
						</view>
						<text class="fc-more">更多内容 ></text>
					</view>
				</template>

			</view>
		</scroll-view>

		<custom-tab-bar currentPath="/pages/map/index"></custom-tab-bar>
	</view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import CustomTabBar from '@/components/custom-tab-bar/custom-tab-bar.vue'
import { CAREER_TRACKS, type CareerNode, type CareerTrack } from './career-map-data'

// ========== 可调参数区（改这里即可，保存后需重新编译/刷新才能看到效果） ==========
const MAP_TUNING = {
	// 岗位信息卡片与叶子节点之间的间距（rpx），越大卡片离叶子越远
	cardLeafGap: 16,
	// 树枝粗细（SVG 描边宽度，单位：数字）
	vineStrokeWidth: 10,
	// 树枝水平中心位置（rpx，750 宽屏下 375 为正中）
	vineCenterX: 375,
	// 节点圆圈 + 叶子整体占位大小（rpx）
	branchNodeSize: 72,
	// 树枝上的圆点直径（rpx）
	branchDotSize: 28,
	// 悬挂叶子的容器宽高（rpx），影响叶子可摆放范围
	leafContainerW: 36,
	leafContainerH: 50,
	// 叶子本体（叶片）宽高（rpx）
	leafBodyW: 22,
	leafBodyH: 32,
	// 叶子相对圆点的偏移：紧贴圆点边缘为一整体（左侧叶 x / 右侧叶 x / 统一 y，rpx）
	leafOffsetXLeft: -30,
	leafOffsetXRight: 30,
	leafOffsetY: 0,
	// 岗位信息卡片：内边距(上下 左右)、字号（rpx）。圆角固定为胶囊形。
	cardPaddingV: 8,
	cardPaddingH: 20,
	cardFontSize: 30,
	// 转岗高亮卡片、悬浮卡片距离屏幕左右边缘的距离（rpx）
	cardMargin: 40,
}

/*
 * 为什么改完参数后浏览器里不能直接看到效果？
 * 1. 本项目是 UniApp，运行时要先把源码编译成小程序或 H5。你改的是源码里的 MAP_TUNING，
 *    必须重新编译（如重新保存触发编译、或重新运行 npm run dev / 微信开发者工具编译）后，
 *    新数值才会打进运行时代码，页面才会用新值渲染。
 * 2. 若在浏览器里直接打开的是已编译好的 H5 包，没有和源码联动，所以改源码不会自动刷新页面。
 * 3. 建议：用 npm run dev 或 微信开发者工具 跑开发模式，改完 MAP_TUNING 后保存文件，
 *    等终端/工具提示编译完成，再在模拟器或真机上刷新页面查看效果。
 */

const mapTuningVars = computed(() => ({
	'--map-card-leaf-gap': MAP_TUNING.cardLeafGap + 'rpx',
	'--map-branch-node-size': MAP_TUNING.branchNodeSize + 'rpx',
	'--map-card-padding': MAP_TUNING.cardPaddingV + 'rpx ' + MAP_TUNING.cardPaddingH + 'rpx',
	'--map-card-font-size': MAP_TUNING.cardFontSize + 'rpx',
	'--map-card-margin': MAP_TUNING.cardMargin + 'rpx',
}))

const currentTrack = ref<CareerTrack>(CAREER_TRACKS[0])
const transferNode = ref<CareerNode | null>(null)
const transferIdx = ref<number>(-1)

const NODE_SPACING = 220
const VINE_CENTER_X = MAP_TUNING.vineCenterX

const canvasHeight = computed(() => {
	return (currentTrack.value.nodes.length - 1) * NODE_SPACING + 300
})

const FLOAT_CARD_GAP = 24
const HIGHLIGHT_TO_FIRST_GAP = 56
const FLOAT_CARD_EST_HEIGHT = 180
const BOTTOM_TAB_RESERVE = 140

const effectiveCanvasHeight = computed(() => {
	const base = canvasHeight.value
	if (!transferNode.value || transferIdx.value < 0) return base
	const n = transferNode.value.transferOptions?.length ?? 0
	if (n === 0) return base
	const baseTop = getNodeTop(transferIdx.value)
	const lastCardTop = baseTop + HIGHLIGHT_TO_FIRST_GAP + (n - 1) * (FLOAT_CARD_EST_HEIGHT + FLOAT_CARD_GAP)
	const minHeight = lastCardTop + FLOAT_CARD_EST_HEIGHT + 80 + BOTTOM_TAB_RESERVE
	return Math.max(base, minHeight)
})

const getNodeTop = (idx: number): number => {
	return 60 + idx * NODE_SPACING
}

const isRight = (idx: number): boolean => idx % 2 === 0

/*
 * 核心思路：
 * 原始 SVG 路径在 69×590 坐标系中定义了一条 S 形贝塞尔曲线。
 * 之前用 scaleToFill 拉伸 SVG 图片，导致位图渲染的曲线形变与
 * 贝塞尔数学采样不一致——这就是节点偏离树枝的根因。
 *
 * 新方案：把原始贝塞尔控制点直接映射到屏幕 rpx 坐标系，然后
 * 用 data URI 动态生成一条与画布尺寸完全一致的 SVG 路径。
 * 节点定位也使用同一组映射后的控制点做贝塞尔采样。
 * 渲染曲线 = 数学曲线，零误差。
 */

interface Pt { x: number; y: number }

const SVG_W = 69, SVG_H = 590
const VINE_RPX_W = 138

const ORIG_SEGS: [Pt, Pt, Pt, Pt][] = [
	[{x:2,y:2}, {x:18.02,y:28.52}, {x:16.16,y:24.46}, {x:47.54,y:45.54}],
	[{x:47.54,y:45.54}, {x:81.07,y:68.06}, {x:66.06,y:136.62}, {x:40.53,y:166.64}],
	[{x:40.53,y:166.64}, {x:7.63,y:205.35}, {x:11.54,y:269.73}, {x:30.53,y:296.25}],
	[{x:30.53,y:296.25}, {x:41.87,y:312.10}, {x:57.55,y:375.32}, {x:40.53,y:414.35}],
	[{x:40.53,y:414.35}, {x:29.26,y:440.22}, {x:10.01,y:510.43}, {x:47.54,y:588.00}],
]

function mapPt(p: Pt, h: number): Pt {
	return {
		x: VINE_CENTER_X + (p.x / SVG_W - 0.5) * VINE_RPX_W,
		y: (p.y / SVG_H) * h
	}
}

function mapSegs(h: number): [Pt, Pt, Pt, Pt][] {
	return ORIG_SEGS.map(seg => seg.map(p => mapPt(p, h)) as [Pt, Pt, Pt, Pt])
}

function evalBezier(p0: Pt, p1: Pt, p2: Pt, p3: Pt, t: number): Pt {
	const m = 1 - t, m2 = m * m, m3 = m2 * m
	const t2 = t * t, t3 = t2 * t
	return {
		x: m3*p0.x + 3*m2*t*p1.x + 3*m*t2*p2.x + t3*p3.x,
		y: m3*p0.y + 3*m2*t*p1.y + 3*m*t2*p2.y + t3*p3.y
	}
}

function buildSamples(segs: [Pt, Pt, Pt, Pt][]): Pt[] {
	const pts: Pt[] = []
	for (const [p0, p1, p2, p3] of segs) {
		for (let t = 0; t <= 1; t += 0.005) {
			pts.push(evalBezier(p0, p1, p2, p3, t))
		}
	}
	return pts
}

function getXAtY(samples: Pt[], targetY: number): number {
	let best = samples[0], minD = 1e9
	for (const pt of samples) {
		const d = Math.abs(pt.y - targetY)
		if (d < minD) { minD = d; best = pt }
	}
	return best.x
}

function getPointMetaAtY(samples: Pt[], targetY: number) {
	let bestIdx = 0
	let minD = 1e9
	for (let i = 0; i < samples.length; i++) {
		const d = Math.abs(samples[i].y - targetY)
		if (d < minD) {
			minD = d
			bestIdx = i
		}
	}
	const prev = samples[Math.max(0, bestIdx - 2)]
	const next = samples[Math.min(samples.length - 1, bestIdx + 2)]
	const current = samples[bestIdx]
	const angle = Math.atan2(next.y - prev.y, next.x - prev.x) * 180 / Math.PI
	return { x: current.x, y: current.y, angle }
}

const screenSegs = computed(() => mapSegs(canvasHeight.value))
const screenSamples = computed(() => buildSamples(screenSegs.value))

const buildNodeGroupSvg = (x: number, y: number, isGold: boolean, rightSide: boolean) => {
	const stroke = isGold ? '#C9A86A' : '#8AAA7B'
	const fill = isGold ? '#D4B884' : '#C5D4BE'
	const mirror = rightSide ? '' : ' scale(-1 1)'
	return `<g transform="translate(${x.toFixed(1)} ${y.toFixed(1)})${mirror}">
		<circle cx="0" cy="0" r="12.5" fill="${fill}" stroke="${stroke}" stroke-width="2.4"/>
		<path d="M8 8L18 18" stroke="${stroke}" stroke-width="4.2" stroke-linecap="round"/>
		<path d="M18 18C24 16 31 18 35 24C39 30 39 39 35 45C29 45 23 42 19 36C16 30 15 23 18 18Z" stroke="${stroke}" stroke-width="3.2" stroke-linejoin="round" fill="none"/>
		<path d="M24 22L27 39" stroke="${stroke}" stroke-width="2.4" stroke-linecap="round"/>
	</g>`
}

const treeSvgDataUri = computed(() => {
	const h = canvasHeight.value
	const segs = screenSegs.value
	let d = `M${segs[0][0].x.toFixed(1)} ${segs[0][0].y.toFixed(1)}`
	for (const [, c1, c2, end] of segs) {
		d += ` C${c1.x.toFixed(1)} ${c1.y.toFixed(1)} ${c2.x.toFixed(1)} ${c2.y.toFixed(1)} ${end.x.toFixed(1)} ${end.y.toFixed(1)}`
	}
	const nodeSvgs = currentTrack.value.nodes.map((node, idx) => {
		const dotCenterY = getNodeTop(idx) + 40
		const meta = getPointMetaAtY(screenSamples.value, dotCenterY)
		return buildNodeGroupSvg(meta.x, meta.y, Boolean(node.hasTransfer), isRight(idx))
	}).join('')
	const svg = `<svg xmlns="http://www.w3.org/2000/svg" width="750" height="${h}" viewBox="0 0 750 ${h}">
		<path d="${d}" stroke="#D7E3D2" stroke-width="${MAP_TUNING.vineStrokeWidth}" stroke-linecap="round" fill="none"/>
		${nodeSvgs}
	</svg>`
	return `data:image/svg+xml;utf8,${encodeURIComponent(svg)}`
})

const getDotLeft = (idx: number): number => {
	const dotCenterY = getNodeTop(idx) + 40
	return getXAtY(screenSamples.value, dotCenterY)
}

const getNodeMeta = (idx: number) => {
	const dotCenterY = getNodeTop(idx) + 40
	return getPointMetaAtY(screenSamples.value, dotCenterY)
}

const getTransferHitStyle = (idx: number): Record<string, string> => {
	const meta = getNodeMeta(idx)
	const localTop = meta.y - getNodeTop(idx)
	const size = MAP_TUNING.branchNodeSize
	return {
		left: `${meta.x - size / 2}rpx`,
		top: `${localTop - size / 2}rpx`,
		width: `${size}rpx`,
		height: `${size}rpx`,
	}
}

const getCardStyle = (idx: number): Record<string, string> => {
	const meta = getNodeMeta(idx)
	const localTop = meta.y - getNodeTop(idx)
	const forwardExtent = 40
	const gap = MAP_TUNING.cardLeafGap
	const base: Record<string, string> = {
		position: 'absolute',
		top: `${localTop}rpx`,
		transform: 'translateY(-50%)'
	}
	if (isRight(idx)) {
		base.left = `${meta.x + forwardExtent + gap}rpx`
	} else {
		base.right = `${750 - meta.x + forwardExtent + gap}rpx`
	}
	return base
}

const switchTrack = (t: CareerTrack) => {
	currentTrack.value = t
	closeTransfer()
}

const openTransfer = (node: CareerNode, idx: number) => {
	transferNode.value = node
	transferIdx.value = idx
}

const closeTransfer = () => {
	transferNode.value = null
	transferIdx.value = -1
}

const getHighlightPos = (idx: number) => {
	const m = MAP_TUNING.cardMargin + 'rpx'
	return isRight(idx)
		? { right: m, left: 'auto' }
		: { left: m, right: 'auto' }
}

const getFloatCardStyle = (oi: number) => {
	const baseTop = getNodeTop(transferIdx.value)
	const cardTop = baseTop + HIGHLIGHT_TO_FIRST_GAP + oi * (FLOAT_CARD_EST_HEIGHT + FLOAT_CARD_GAP)
	const onRight = isRight(transferIdx.value)
	const m = MAP_TUNING.cardMargin + 'rpx'
	return onRight
		? { top: cardTop + 'rpx', left: m, right: 'auto' }
		: { top: cardTop + 'rpx', right: m, left: 'auto' }
}

onMounted(() => {
	uni.hideTabBar()
})
</script>

<style lang="scss" scoped>
.container {
	position: relative;
	width: 100vw;
	height: 100vh;
	display: flex;
	flex-direction: column;
	overflow: hidden;
}

.bg-img {
	position: fixed;
	top: 0; left: 0;
	width: 100vw; height: 100vh;
	z-index: 0;
}

.top-title {
	position: relative;
	z-index: 10;
	margin-top: 84rpx;
	margin-left: 34rpx;
	margin-bottom: 48rpx;
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', sans-serif;
	font-size: 64rpx;
	font-weight: 900;
	color: #353C47;
}

/* ===== Tab 栏 ===== */
.tab-bar {
	position: relative;
	z-index: 10;
	white-space: nowrap;
	padding: 0 40rpx 40rpx;
	flex-shrink: 0;
}
.tab-chip {
	display: inline-block;
	position: relative;
	margin-right: 48rpx;
}
.tab-text-wrap {
	position: relative;
	display: inline-block;
	padding-right: 8rpx;
	padding-bottom: 6rpx;
}
.tab-chip-text {
	font-family: 'FZLanTingHeiS-B-GB', 'PingFang SC', sans-serif;
	font-size: 32rpx;
	color: #757575;
	transition: all 0.2s;
	position: relative;
	z-index: 2;
}
.tab-chip.active .tab-chip-text {
	color: #353C47;
	font-weight: 900;
	font-size: 40rpx;
}

/* L 形指示线：贴近文字、加粗、颜色 #2f4f45 */
.tab-indicator {
	position: absolute;
	right: 0;
	bottom: 0;
	width: 55%;
	height: 50%;
	z-index: 1;
}
.tab-indicator-v,
.tab-indicator-h {
	position: absolute;
	background-color: #2f4f45;
	border-radius: 999rpx;
}
.tab-indicator-v {
	top: 0;
	right: 0;
	width: 10rpx;
	height: 100%;
}
.tab-indicator-h {
	right: 0;
	bottom: 0;
	width: 50%;
	height: 10rpx;
}

/* ===== 图谱滚动区 ===== */
.map-scroll {
	position: relative;
	z-index: 5;
	flex: 1;
	height: 0;
}
.map-canvas {
	position: relative;
	width: 100%;
}

.vine-img-full {
	position: absolute;
	top: 0;
	left: 0;
	z-index: 1;
}

/* ===== 节点行 ===== */
.node-row {
	position: absolute;
	left: 0; right: 0;
	height: 80rpx;
	z-index: 10;
}

/* 转岗节点点击热区：节点本身已画进同一张 SVG 里 */
.transfer-hit {
	position: absolute;
	z-index: 12;
	border-radius: 50%;
	background-color: transparent;
}

/* 节点卡片：胶囊形 */
.node-card {
	padding: var(--map-card-padding);
	background-color: #d7e3d2;
	border-radius: 999rpx;
	z-index: 10;
}
.node-card.highlight {
	background-color: #eef3ec;
	box-shadow: 0 4rpx 12rpx 4rpx rgba(0, 0, 0, 0.12), 0 2rpx 4rpx 0 rgba(0, 0, 0, 0.18);
}
.node-label {
	font-family: 'FZLanTingHeiS-B-GB', 'PingFang SC', sans-serif;
	font-size: var(--map-card-font-size);
	color: #555;
	font-weight: 600;
	white-space: nowrap;
}
.node-card.highlight .node-label {
	color: #2F4F44;
	font-weight: 700;
}

/* ===== 转岗悬浮层：画布内遮罩点击关闭，在 scroll-view 内滑动可照常滚动 ===== */
.transfer-backdrop {
	position: absolute;
	top: 0; left: 0; right: 0; bottom: 0;
	z-index: 55;
	background-color: transparent;
}

.highlight-card {
	position: absolute;
	z-index: 60;
	padding: 14rpx 24rpx;
	max-width: 420rpx;
	background-color: #F4F7F3;
	border-radius: 20rpx;
	box-shadow: 0 4rpx 12rpx 4rpx rgba(0, 0, 0, 0.15), 0 2rpx 4rpx 0 rgba(0, 0, 0, 0.3);
	display: flex;
	align-items: center;
}
.highlight-label {
	font-family: 'FZLanTingHeiS-B-GB', 'PingFang SC', sans-serif;
	font-size: 36rpx;
	color: #2F4F44;
	font-weight: 600;
	line-height: 1.3;
	word-break: break-all;
}

.float-card {
	position: absolute;
	z-index: 60;
	width: 320rpx;
	min-height: 0;
	background-color: #F4F7F3;
	border-radius: 40rpx;
	box-shadow: 0 4rpx 12rpx 4rpx rgba(0, 0, 0, 0.15), 0 2rpx 4rpx 0 rgba(0, 0, 0, 0.3);
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	padding: 24rpx 28rpx;
	animation: floatIn 0.25s ease;
}
@keyframes floatIn {
	from { opacity: 0; transform: scale(0.92); }
	to   { opacity: 1; transform: scale(1); }
}
.fc-top {
	display: flex;
	flex-direction: column;
	gap: 8rpx;
}
.fc-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', sans-serif;
	font-size: 36rpx;
	color: #2F4F44;
	font-weight: 600;
	line-height: 1.3;
	word-break: break-all;
}
.fc-desc {
	font-size: 24rpx;
	color: #757575;
	line-height: 1.45;
	word-break: break-all;
}
.fc-more {
	font-size: 22rpx;
	color: #757575;
	text-align: right;
	margin-top: 12rpx;
	flex-shrink: 0;
}
.float-card:active { opacity: 0.9; }
</style>
