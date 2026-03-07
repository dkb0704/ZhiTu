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
					<text class="hint-highlight">在意程度</text>
					<text class="hint-text">分配</text>
					<text class="points-number">{{ remainingPoints }}</text>
					<text class="hint-text">点数</text>
				</view>
			</view>
		</view>

		<!-- 居中核心区：维度卡片网格 -->
		<view class="center-content">
			<view class="grid-container">
				
				<!-- 薪资水平 -->
				<view class="dimension-card">
					<text class="dim-title">薪资水平</text>
					<text class="dim-desc">高点数：在业内相对高薪</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="salary > 0 ? '#999' : '#e0e0e0'" @click="decrease('salary')"></u-icon>
						<text class="step-value" :class="{ 'active': salary > 0 }">{{ salary }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('salary')"></u-icon>
					</view>
				</view>

				<!-- 稳定性 -->
				<view class="dimension-card">
					<text class="dim-title">稳定性</text>
					<text class="dim-desc">高点数：抗风险能力强</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="stability > 0 ? '#999' : '#e0e0e0'" @click="decrease('stability')"></u-icon>
						<text class="step-value" :class="{ 'active': stability > 0 }">{{ stability }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('stability')"></u-icon>
					</view>
				</view>

				<!-- 成长空间 -->
				<view class="dimension-card">
					<text class="dim-title">成长空间</text>
					<text class="dim-desc">高点数：学习成长机会多</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="growth > 0 ? '#999' : '#e0e0e0'" @click="decrease('growth')"></u-icon>
						<text class="step-value" :class="{ 'active': growth > 0 }">{{ growth }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('growth')"></u-icon>
					</view>
				</view>

				<!-- 工作兴趣 -->
				<view class="dimension-card">
					<text class="dim-title">工作兴趣</text>
					<text class="dim-desc">高点数：感兴趣工作内容</text>
					<view class="stepper">
						<u-icon name="minus-circle" size="36" :color="interest > 0 ? '#999' : '#e0e0e0'" @click="decrease('interest')"></u-icon>
						<text class="step-value" :class="{ 'active': interest > 0 }">{{ interest }}</text>
						<u-icon name="plus-circle" size="36" :color="remainingPoints > 0 ? '#999' : '#e0e0e0'" @click="increase('interest')"></u-icon>
					</view>
				</view>

			</view>

			<!-- 底部操作区 -->
			<view class="bottom-action">
				<button class="submit-btn" :class="{ 'disabled': remainingPoints > 0 }" @click="handleSubmit">
					<u-icon name="arrow-right" color="#fff" size="36" bold></u-icon>
				</button>
				<text class="reset-text" @click="handleReset">重置</text>
			</view>
		</view>

	</view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { updatePreferenceAPI } from '@/api/preference'

// 四个维度的点数状态
const salary = ref(0)
const stability = ref(0)
const growth = ref(0)
const interest = ref(0)

// 总可用点数限制为 10
const MAX_POINTS = 10

// 计算已用点数
const usedPoints = computed(() => salary.value + stability.value + growth.value + interest.value)

// 计算剩余点数
const remainingPoints = computed(() => MAX_POINTS - usedPoints.value)

// 增加点数
const increase = (type: string) => {
	if (remainingPoints.value <= 0) {
		uni.showToast({ title: '点数已用完', icon: 'none' })
		return
	}
	if (type === 'salary') salary.value++
	else if (type === 'stability') stability.value++
	else if (type === 'growth') growth.value++
	else if (type === 'interest') interest.value++
}

// 减少点数
const decrease = (type: string) => {
	if (type === 'salary' && salary.value > 0) salary.value--
	else if (type === 'stability' && stability.value > 0) stability.value--
	else if (type === 'growth' && growth.value > 0) growth.value--
	else if (type === 'interest' && interest.value > 0) interest.value--
}

// 重置所有点数
const handleReset = () => {
	salary.value = 0
	stability.value = 0
	growth.value = 0
	interest.value = 0
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
		
		// 组装成 JSON 字符串
		const jobPreferencesObj = {
			salary: salary.value,
			stability: stability.value,
			growth: growth.value,
			interest: interest.value
		}
		
		await updatePreferenceAPI({
			jobPreferences: JSON.stringify(jobPreferencesObj)
		})
		
		uni.hideLoading()
		// 点数分配第一页完成后，直接跳转到第二页，不再额外弹出成功提示或延时等待
		uni.reLaunch({ url: '/pages/preference/job-preference-2' })
		
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
	
	overflow: hidden;
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
	position: absolute; top: 120rpx; left: 40rpx; z-index: 10; width: 100%;
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
	font-size: 47rpx; /* Increased from 40rpx */
	font-weight: 600;
	color: #666;
	width: 100%; /* Ensure it spans the full width */
	letter-spacing: 2rpx; /* Optional: adds a bit of breath to the line */
}
.hint-text {
	color: #666;
}
.hint-highlight {
	color: #81B898;
	margin: 0 10rpx;
	font-size: 52rpx; /* Make the '在意程度' text slightly larger too */
}
.points-number {
	font-size: 90rpx; /* Increased from 80rpx */
	font-weight: 900;
	color: #4A5D4E;
	margin: 0 16rpx;
	line-height: 1;
}

.center-content {
	position: relative; flex: 1; z-index: 20;
	display: flex; flex-direction: column; align-items: center; 
	padding-top: 430rpx; /* Increased from 400rpx to move everything down */
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
	padding: 60rpx 20rpx; /* Taller/more slender */
	display: flex;
	flex-direction: column;
	align-items: center;
	box-shadow: 0 16rpx 40rpx rgba(129, 184, 152, 0.15);
}

.dim-title {
	font-size: 48rpx; /* Larger card title */
	font-weight: 900;
	color: #2f4f45; /* Darker green color */
	margin-bottom: 16rpx;
}

.dim-desc {
	font-size: 24rpx; /* Slightly larger description */
	color: #999;
	margin-bottom: 50rpx; /* More space before stepper */
}

.stepper {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 100%;
}

.step-value {
	font-size: 52rpx; /* Larger value number */
	font-weight: bold;
	color: #81B898;
	width: 80rpx;
	text-align: center;
	opacity: 0.5;
}
.step-value.active {
	opacity: 1;
}

/* 底部操作区 */
.bottom-action {
	margin-top: 200rpx; /* Even larger distance from cards */
	display: flex;
	flex-direction: column;
	align-items: center;
}

.submit-btn {
	width: 320rpx; height: 90rpx; border-radius: 50rpx; /* Wider but slightly shorter */
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

.reset-text {
	margin-top: 40rpx; font-size: 28rpx; color: #81B898; font-weight: 500; letter-spacing: 4rpx;
	opacity: 0.8;
}
.reset-text:active { opacity: 0.5; }

</style>
