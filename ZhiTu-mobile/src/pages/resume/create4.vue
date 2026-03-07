<template>
	<!-- 简历制作最后一步：自我评价 -->
	<view class="container">
		
		<!-- 顶部背景与标题 -->
		<view class="top-bg">
			<view class="bg-blur bg-blur-1"></view>
			<view class="bg-blur bg-blur-2"></view>
			<view class="title-wrapper">
				<text class="top-title">职途同行</text>
			</view>
		</view>

		<!-- 居中核心内容区：白色表单卡片 -->
		<view class="center-content">
			<view class="form-card"> 
				
				<!-- 卡片标题 -->
				<text class="card-title">简 历</text>
				
				<!-- 自我评价标签 -->
				<text class="section-label">自我评价</text>

				<!-- 大面积多行文本输入框 -->
				<view class="textarea-box">
					<textarea 
						class="eval-textarea"
						v-model="selfEvaluation"
						placeholder="请填写你的自我评价"
						placeholder-style="color:#ccc; font-size:28rpx;"
						:maxlength="500"
						auto-height
					/>
				</view>

				<!-- 底部提交大按钮 -->
				<button class="submit-btn" @click="handleSubmit">
					<u-icon name="arrow-right" color="#fff" size="32" bold></u-icon>
				</button>
				
				<!-- 暂时跳过 -->
				<text class="skip-text" @click="handleSkip">暂时跳过</text>

			</view> <!-- 卡片结束 -->
		</view>

	</view>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { updateProfileAPI } from '@/api/profile'

const selfEvaluation = ref('')

const handleSubmit = async () => {
	if (!selfEvaluation.value.trim()) {
		uni.showToast({
			title: '请填写自我评价哦',
			icon: 'none'
		})
		return
	}

	try {
		uni.showLoading({ title: '生成中...' })
		await updateProfileAPI({
			selfEvaluation: selfEvaluation.value
		})
		uni.hideLoading()
		// 简历生成成功后，直接进入职业偏好设置页，不再额外弹出成功提示或延时等待
		uni.redirectTo({ url: '/pages/preference/job-preference' })
	} catch (e: any) {
		uni.hideLoading()
		uni.showToast({ title: e?.message || e?.info || '保存失败，请稍后重试', icon: 'none' })
	}
}

const handleSkip = () => {
	uni.reLaunch({
		url: '/pages/job/index'
	})
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
	position: absolute; top: 42px; left: 17px; z-index: 10;
}
.top-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', sans-serif;
	font-size: 32px; font-weight: 900; color: #2d3436; 
	letter-spacing: 2rpx; -webkit-text-stroke: 1px #2d3436;
}

.center-content {
	position: relative; flex: 1; z-index: 20;
	display: flex; flex-direction: column; align-items: center; justify-content: flex-start; 
	padding-top: 420rpx;         
}

.form-card {
	width: 630rpx; min-height: 800rpx; background-color: #ffffff;
	border-radius: 40rpx; display: flex; flex-direction: column; align-items: center;
	padding: 40rpx 40rpx 80rpx; box-shadow: 0 10rpx 40rpx rgba(129, 184, 152, 0.15);
}

.card-title {
	font-size: 44rpx; font-weight: 900; color: #81B898;
	letter-spacing: 20rpx; margin-bottom: 40rpx; transform: translateX(10rpx);
}

/* 自我评价标签 */
.section-label {
	align-self: flex-start;
	font-size: 32rpx;
	font-weight: 900;
	color: #333333;
	margin-bottom: 24rpx;
}

/* 大面积输入框外壳 */
.textarea-box {
	width: 100%;
	min-height: 420rpx;
	border: 2rpx solid #c3ddcc;
	border-radius: 20rpx;
	background-color: #FAFCFB;
	box-sizing: border-box;
	transition: border-color 0.3s;
}
.textarea-box:focus-within {
	border-color: #81B898;
}

.eval-textarea {
	width: 100%;
	min-height: 400rpx;
	padding: 24rpx;
	box-sizing: border-box;
	font-size: 28rpx;
	color: #333;
	line-height: 1.6;
}

.submit-btn {
	margin-top: 80rpx; width: 300rpx; height: 100rpx; border-radius: 50rpx;
	background-color: #81B898; display: flex; align-items: center; justify-content: center;
	box-shadow: 0 10rpx 20rpx rgba(129, 184, 152, 0.4); border: none; transition: all 0.2s;
	&::after { border: none; }
}
.submit-btn:active { transform: scale(0.96); box-shadow: 0 5rpx 10rpx rgba(129, 184, 152, 0.2); }

.skip-text {
	margin-top: 40rpx; font-size: 28rpx; color: #81B898; font-weight: 500; letter-spacing: 2rpx;
}
.skip-text:active { opacity: 0.6; }
</style>
