<template>
	<view class="container">
		
		<!-- 顶部背景与标题 -->
		<view class="top-bg">
			<view class="bg-blur bg-blur-1"></view>
			<view class="bg-blur bg-blur-2"></view>
			<view class="title-wrapper">
				<text class="top-title">职途同行</text>
			</view>
		</view>

		<!-- 居中核心区：自评问题列表 -->
		<view class="center-content">
			<view class="question-list">
				
				<!-- 问题 1 -->
				<view class="question-item">
					<text class="q-text">我能够在较短时间内，把复杂的想法讲清楚，让别人听懂。</text>
					<view class="scale-container">
						<view 
							v-for="score in 5" 
							:key="'q1-' + score"
							class="scale-circle"
							:class="[ 'size-' + score, { 'selected': q1Score === score } ]"
							@click="q1Score = score"
						>
							<view v-if="q1Score === score" class="inner-dot"></view>
						</view>
					</view>
					<view class="divider"></view>
				</view>

				<!-- 问题 2 -->
				<view class="question-item">
					<text class="q-text">在新的团队或陌生的环境中，我通常能较快与别人建立信任关系。</text>
					<view class="scale-container">
						<view 
							v-for="score in 5" 
							:key="'q2-' + score"
							class="scale-circle"
							:class="[ 'size-' + score, { 'selected': q2Score === score } ]"
							@click="q2Score = score"
						>
							<view v-if="q2Score === score" class="inner-dot"></view>
						</view>
					</view>
					<view class="divider"></view>
				</view>

				<!-- 问题 3 -->
				<view class="question-item">
					<text class="q-text">面对迫在眉睫的任务，我总能有条不紊地进行工作。</text>
					<view class="scale-container">
						<view 
							v-for="score in 5" 
							:key="'q3-' + score"
							class="scale-circle"
							:class="[ 'size-' + score, { 'selected': q3Score === score } ]"
							@click="q3Score = score"
						>
							<view v-if="q3Score === score" class="inner-dot"></view>
						</view>
					</view>
					<view class="divider"></view>
				</view>

			</view>

			<!-- 底部操作区 (不对称设计: 左侧重置, 右侧提交大按钮) -->
			<view class="bottom-action">
				<text class="reset-text" @click="handleReset">重置</text>
				<button class="submit-btn" :class="{ 'disabled': !isAllAnswered }" @click="handleSubmit">
					<u-icon name="arrow-right" color="#fff" size="36" bold></u-icon>
				</button>
			</view>
		</view>

	</view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { updatePreferenceAPI } from '@/api/preference'

// 分数状态：0代表未回答，1-5代表选择的大小档位
const q1Score = ref(0)
const q2Score = ref(0)
const q3Score = ref(0)

// 校验是否全部作答
const isAllAnswered = computed(() => {
	return q1Score.value > 0 && q2Score.value > 0 && q3Score.value > 0
})

// 重置所有选项
const handleReset = () => {
	q1Score.value = 0
	q2Score.value = 0
	q3Score.value = 0
}

// 提交保存
const handleSubmit = async () => {
	if (!isAllAnswered.value) {
		uni.showToast({
			title: '请完成所有自评题目',
			icon: 'none'
		})
		return
	}

	try {
		uni.showLoading({ title: '保存中...' })
		
		// 我们将这三题的回答作为 softSkills (软实力自评) JSON 提交
		const softSkillsObj = {
			communication: q1Score.value,
			relationship: q2Score.value,
			pressure: q3Score.value
		}
		
		await updatePreferenceAPI({
			softSkills: JSON.stringify(softSkillsObj)
		})
		
		uni.hideLoading()
		// 完成软实力自评后，进入 MBTI / 霍兰德 / Big Five 选择页，作为注册流程的最后一步
		uni.reLaunch({ url: '/pages/preference/personality' })
		
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

.center-content {
	position: relative; flex: 1; z-index: 20;
	display: flex; flex-direction: column; align-items: center; 
	padding-top: 260rpx; 
	padding-bottom: 60rpx;
}

.question-list {
	width: 670rpx;
	display: flex;
	flex-direction: column;
	background: transparent;
}

.question-item {
	display: flex;
	flex-direction: column;
	margin-bottom: 40rpx;
}

.q-text {
	font-size: 34rpx;
	font-weight: bold;
	color: #2f4f45;
	line-height: 1.5;
	margin-bottom: 50rpx;
	padding: 0 10rpx;
}

/* 5 个从小到大圆圈的容器 */
.scale-container {
	display: flex;
	flex-direction: row;
	align-items: center;
	justify-content: space-between;
	padding: 0 30rpx;
	margin-bottom: 40rpx;
}

/* 基础空心圆 */
.scale-circle {
	border-radius: 50%;
	border: 3rpx solid #87bf9d;
	background-color: transparent;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.2s ease;
	overflow: hidden;
}

/* 渐变尺寸，从左到右递增 */
.size-1 { width: 44rpx; height: 44rpx; }
.size-2 { width: 56rpx; height: 56rpx; }
.size-3 { width: 68rpx; height: 68rpx; }
.size-4 { width: 80rpx; height: 80rpx; }
.size-5 { width: 92rpx; height: 92rpx; }

/* 选中态效果 */
.scale-circle.selected {
	border-color: #87bf9d; 
	background-color: transparent; 
	transform: scale(1.05); /* 稍微放大提示手感 */
}

/* 选中后的实心满铺内圆 */
.inner-dot {
	width: 100%;
	height: 100%;
	border-radius: 50%;
	background-color: #d7e3d2;
}

/* 分隔线 */
.divider {
	height: 2rpx;
	background-color: #e5ebe8;
	width: 85%;
	margin: 0 auto;
}

/* 底部操作区 (不对称布局) */
.bottom-action {
	margin-top: 60rpx; 
	width: 670rpx;
	display: flex;
	flex-direction: row;
	justify-content: space-between; 
	align-items: center;
	padding: 0 20rpx;
}

.reset-text {
	font-size: 32rpx; 
	color: #81B898; 
	font-weight: 500; 
	letter-spacing: 4rpx;
	opacity: 0.8;
	margin-left: 100rpx; 
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
/* 全部作答后点亮 */
.submit-btn:not(.disabled) {
	background-color: #81B898;
	box-shadow: 0 10rpx 20rpx rgba(129, 184, 152, 0.4);
}
.submit-btn:active { transform: scale(0.96); }

</style>
