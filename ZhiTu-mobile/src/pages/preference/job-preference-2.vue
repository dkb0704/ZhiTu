<template>
	<view class="container">
		
		<!-- 顶部背景与标题 -->
		<view class="top-bg">
			<view class="bg-blur bg-blur-1"></view>
			<view class="bg-blur bg-blur-2"></view>
			<view class="title-wrapper">
				<text class="top-title">职途同行</text>
				<!-- 动态显示剩余点数 -->
				<view class="points-hint">
					<text class="hint-text">请按</text>
					<text class="hint-highlight">重视程度</text>
					<text class="hint-text">分配</text>
					<text class="points-number">{{ remainingPoints }}</text>
					<text class="hint-text">点数</text>
				</view>
			</view>
		</view>

		<!-- 居中核心区：维度卡片网格 -->
		<view class="center-content">
			<!-- 3行2列的网格布局 -->
			<view class="grid-container">
				
				<!-- 福利保障 -->
				<view class="dimension-card">
					<text class="dim-title">福利保障</text>
					<text class="dim-desc">高点数：奖金保险等充足</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="benefits > 0 ? '#999' : '#e0e0e0'" @click="decrease('benefits')"></u-icon>
						<text class="step-value" :class="{ 'active': benefits > 0 }">{{ benefits }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('benefits')"></u-icon>
					</view>
				</view>

				<!-- 生活成本 -->
				<view class="dimension-card">
					<text class="dim-title">生活成本</text>
					<text class="dim-desc">高点数：日常生活成本低廉</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="livingCost > 0 ? '#999' : '#e0e0e0'" @click="decrease('livingCost')"></u-icon>
						<text class="step-value" :class="{ 'active': livingCost > 0 }">{{ livingCost }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('livingCost')"></u-icon>
					</view>
				</view>

				<!-- 工作强度 -->
				<view class="dimension-card">
					<text class="dim-title">工作强度</text>
					<text class="dim-desc">高点数：少加班低工时</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="workIntensity > 0 ? '#999' : '#e0e0e0'" @click="decrease('workIntensity')"></u-icon>
						<text class="step-value" :class="{ 'active': workIntensity > 0 }">{{ workIntensity }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('workIntensity')"></u-icon>
					</view>
				</view>

				<!-- 涨薪空间 -->
				<view class="dimension-card">
					<text class="dim-title">涨薪空间</text>
					<text class="dim-desc">高点数：较大涨薪可能</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="salaryGrowth > 0 ? '#999' : '#e0e0e0'" @click="decrease('salaryGrowth')"></u-icon>
						<text class="step-value" :class="{ 'active': salaryGrowth > 0 }">{{ salaryGrowth }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('salaryGrowth')"></u-icon>
					</view>
				</view>

				<!-- 晋升空间 -->
				<view class="dimension-card">
					<text class="dim-title">晋升空间</text>
					<text class="dim-desc">高点数：易快速晋升</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="promotion > 0 ? '#999' : '#e0e0e0'" @click="decrease('promotion')"></u-icon>
						<text class="step-value" :class="{ 'active': promotion > 0 }">{{ promotion }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('promotion')"></u-icon>
					</view>
				</view>

				<!-- 技术门槛 -->
				<view class="dimension-card">
					<text class="dim-title">技术门槛</text>
					<text class="dim-desc">高点数：岗位行业壁垒高</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="techBarrier > 0 ? '#999' : '#e0e0e0'" @click="decrease('techBarrier')"></u-icon>
						<text class="step-value" :class="{ 'active': techBarrier > 0 }">{{ techBarrier }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('techBarrier')"></u-icon>
					</view>
				</view>

			</view>

			<!-- 底部操作区 (不对称设计: 左侧重置, 右侧提交大按钮) -->
			<view class="bottom-action">
				<text class="reset-text" @click="handleReset">重置</text>
				<button class="submit-btn" :class="{ 'disabled': remainingPoints > 0 }" @click="handleSubmit">
					<u-icon name="arrow-right" color="#fff" size="36" bold></u-icon>
				</button>
			</view>
		</view>

	</view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { updatePreferenceAPI, getPreferenceAPI } from '@/api/preference'

// 6个维度的点数状态
const benefits = ref(0)       // 福利保障
const livingCost = ref(0)     // 生活成本
const workIntensity = ref(0)  // 工作强度
const salaryGrowth = ref(0)   // 涨薪空间
const promotion = ref(0)      // 晋升空间
const techBarrier = ref(0)    // 技术门槛

// 总可用点数限制为 15
const MAX_POINTS = 15

// 计算已用点数
const usedPoints = computed(() => 
	benefits.value + livingCost.value + workIntensity.value + 
	salaryGrowth.value + promotion.value + techBarrier.value
)

// 计算剩余点数
const remainingPoints = computed(() => MAX_POINTS - usedPoints.value)

// 增加点数
const increase = (type: string) => {
	if (remainingPoints.value <= 0) {
		uni.showToast({ title: '点数已用完', icon: 'none' })
		return
	}
	if (type === 'benefits') benefits.value++
	else if (type === 'livingCost') livingCost.value++
	else if (type === 'workIntensity') workIntensity.value++
	else if (type === 'salaryGrowth') salaryGrowth.value++
	else if (type === 'promotion') promotion.value++
	else if (type === 'techBarrier') techBarrier.value++
}

// 减少点数
const decrease = (type: string) => {
	if (type === 'benefits' && benefits.value > 0) benefits.value--
	else if (type === 'livingCost' && livingCost.value > 0) livingCost.value--
	else if (type === 'workIntensity' && workIntensity.value > 0) workIntensity.value--
	else if (type === 'salaryGrowth' && salaryGrowth.value > 0) salaryGrowth.value--
	else if (type === 'promotion' && promotion.value > 0) promotion.value--
	else if (type === 'techBarrier' && techBarrier.value > 0) techBarrier.value--
}

// 重置所有点数
const handleReset = () => {
	benefits.value = 0
	livingCost.value = 0
	workIntensity.value = 0
	salaryGrowth.value = 0
	promotion.value = 0
	techBarrier.value = 0
}

// 提交保存
const handleSubmit = async () => {
	if (remainingPoints.value > 0) {
		uni.showToast({
			title: `请分配完剩下的 ${remainingPoints.value} 点数`,
			icon: 'none'
		})
		return
	}

	try {
		uni.showLoading({ title: '保存中...' })
		
		// 将这 6 个指标作为 jobPreferences 提交到后端
		// 因为第一页也使用了 jobPreferences，后端需要合并或者前端先拉取拼装
		// 为了不丢失第一页数据，这里先拉取已有的 jobPreferences 并合并
		const existingPref = await getPreferenceAPI()
		let existingJobPrefObj = {}
		if (existingPref && existingPref.jobPreferences) {
			try {
				existingJobPrefObj = JSON.parse(existingPref.jobPreferences)
			} catch(e) {}
		}

		const mergedJobPreferences = {
			...existingJobPrefObj,
			benefits: benefits.value,
			livingCost: livingCost.value,
			workIntensity: workIntensity.value,
			salaryGrowth: salaryGrowth.value,
			promotion: promotion.value,
			techBarrier: techBarrier.value
		}
		
		await updatePreferenceAPI({
			jobPreferences: JSON.stringify(mergedJobPreferences)
		})
		
		uni.hideLoading()
		// 第二步偏好设置完成后，直接流转到第三页进行大五人格简单测试
		uni.reLaunch({ url: '/pages/preference/job-preference-3' })
		
	} catch (e: any) {
		uni.hideLoading()
		uni.showToast({ title: e?.message || e?.info || '保存失败，请稍后重试', icon: 'none' })
	}
}
</script>

<style lang="scss" scoped>
.container {
	position: relative;
	width: 100vw;
	height: 100vh;           
	
	/* 页面内容增多，为了适应小屏可能需要允许滚动 */
	overflow-y: auto;
	overflow-x: hidden;
	display: flex;
	flex-direction: column;
}

.top-bg {
	position: absolute;
	top: 0; left: 0;
	width: 100%; height: 38vh;
	z-index: 1; pointer-events: none;
	overflow: hidden;
}
.bg-blur {
	position: absolute;
	border-radius: 50%;
	filter: blur(50px);
	z-index: 0;
}
.bg-blur-1 { width: 400rpx; height: 400rpx; background: rgba(129, 184, 152, 0.2); top: -50rpx; left: -50rpx; }
.bg-blur-2 { width: 500rpx; height: 500rpx; background: rgba(129, 184, 152, 0.15); bottom: -100rpx; right: -100rpx; }

.title-wrapper {
	position: absolute; top: 100rpx; left: 40rpx; z-index: 10; width: 100%;
}
.top-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', sans-serif;
	font-size: 64rpx; font-weight: 900; color: #2d3436; 
	display: block;
	margin-bottom: 20rpx;
}

.points-hint {
	display: flex;
	align-items: baseline;
	font-size: 47rpx; 
	font-weight: 600;
	color: #666;
	width: 100%; 
	letter-spacing: 2rpx; 
}
.hint-text {
	color: #666;
}
.hint-highlight {
	color: #81B898;
	margin: 0 10rpx;
	font-size: 52rpx; 
}
.points-number {
	font-size: 90rpx; 
	font-weight: 900;
	color: #4A5D4E;
	margin: 0 16rpx;
	line-height: 1;
}

.center-content {
	position: relative; flex: 1; z-index: 20;
	display: flex; flex-direction: column; align-items: center; 
	padding-top: 360rpx; /* 标题与卡片的间距，因行数增加略微上调以防越界 */
	padding-bottom: 60rpx;
}

.grid-container {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 30rpx;
	width: 670rpx;
}

.dimension-card {
	background: #ffffff;
	border-radius: 30rpx;
	padding: 40rpx 20rpx; /* 稍微紧凑一些以容纳3行 */
	display: flex;
	flex-direction: column;
	align-items: center;
	box-shadow: 0 16rpx 40rpx rgba(129, 184, 152, 0.15); 
}

.dim-title {
	font-size: 44rpx; 
	font-weight: 900;
	color: #2f4f45; 
	margin-bottom: 12rpx;
}

.dim-desc {
	font-size: 24rpx; 
	color: #999;
	margin-bottom: 30rpx; 
}

.stepper {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
}

.step-value {
	font-size: 52rpx; 
	font-weight: bold;
	color: #81B898;
	width: 80rpx;
	text-align: center;
	opacity: 0.5;
}
.step-value.active {
	opacity: 1;
}

/* 底部操作区 (不对称布局) */
.bottom-action {
	margin-top: 60rpx; 
	width: 670rpx;
	display: flex;
	flex-direction: row;       /* 横向排列 */
	justify-content: space-between; /* 两端对齐，重置在左，按钮在右 */
	align-items: center;
	padding: 0 20rpx;
}

.reset-text {
	font-size: 32rpx; 
	color: #81B898; 
	font-weight: 500; 
	letter-spacing: 4rpx;
	opacity: 0.8;
	margin-left: 100rpx; /* 将重置字眼向中部靠近一点 */
}
.reset-text:active { opacity: 0.5; }

.submit-btn {
	width: 320rpx; height: 90rpx; border-radius: 50rpx; 
	margin: 0;
	background-color: #6b6b6b; 
	display: flex; align-items: center; justify-content: center;
	box-shadow: 0 10rpx 20rpx rgba(0, 0, 0, 0.1); border: none; transition: all 0.3s;
	&::after { border: none; }
}
/* 填满点数后高亮变绿 */
.submit-btn:not(.disabled) {
	background-color: #81B898;
	box-shadow: 0 10rpx 20rpx rgba(129, 184, 152, 0.4);
}
.submit-btn:active { transform: scale(0.96); }

</style>
