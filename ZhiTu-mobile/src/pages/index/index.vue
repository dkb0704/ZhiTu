<template>
	<view class="container">
		<!-- Mesh Gradient Background simulation using stacked radial views -->
		<view class="bg-blur bg-blur-1"></view>
		<view class="bg-blur bg-blur-2"></view>
		<view class="bg-blur bg-blur-3"></view>
		<view class="bg-blur bg-blur-4"></view>

		<view class="content">
			<!-- Icon Logo -->
			<view class="icon-wrap">
				<text class="icon-text">ICON</text>
			</view>

			<!-- Typography -->
			<text class="title">职途 · ProTrek</text>
			<text class="subtitle">不妨先试试前路</text>
		</view>

		<!-- Action Buttons -->
		<view class="action-footer">
			<button class="btn btn-primary" hover-class="btn-hover-primary" @tap="navToLogin">
				<text class="btn-text">登 录</text>
			</button>
			<button class="btn btn-secondary" hover-class="btn-hover-secondary" @tap="navToRegister">
				<text class="btn-text">注 册</text>
			</button>
		</view>
	</view>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const navToLogin = () => {
	// uni.navigateTo({ url: '/pages/login/login' })
	console.log('Login clicked')
}

const navToRegister = () => {
	// uni.navigateTo({ url: '/pages/register/register' })
	console.log('Register clicked')
}
</script>

<style lang="scss" scoped>
.container {
	position: relative;
	width: 100vw;
	height: 100vh;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-between;
	overflow: hidden;
	background-color: #F7FBFA;
}

/* Background Mesh Gradients (Performance friendly on mobile) */
.bg-blur {
	position: absolute;
	border-radius: 50%;
	filter: blur(60px);
	z-index: 0;
}
.bg-blur-1 { width: 600rpx; height: 600rpx; background: rgba(129, 184, 152, 0.15); top: -100rpx; left: -100rpx; }
.bg-blur-2 { width: 500rpx; height: 500rpx; background: rgba(129, 184, 152, 0.2); bottom: 100rpx; right: -100rpx; }
.bg-blur-3 { width: 400rpx; height: 400rpx; background: rgba(129, 184, 152, 0.1); top: 30%; right: -50rpx; }
.bg-blur-4 { width: 450rpx; height: 450rpx; background: rgba(129, 184, 152, 0.15); bottom: -50rpx; left: -50rpx; }

/* Content Area */
.content {
	flex: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	z-index: 10;
	width: 100%;
	margin-top: 100rpx; /* Push down slightly from absolute center */
}

.icon-wrap {
	width: 240rpx;
	height: 240rpx;
	background-color: #DDE0DF;
	border-radius: 120rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 70rpx;
	box-shadow: inset 0 2px 4px rgba(255,255,255,0.7), 0 4px 10px rgba(0,0,0,0.03);
}

.icon-text {
	font-weight: 900;
	font-size: 40rpx;
	color: #000;
	letter-spacing: 2rpx;
}

.title {
	font-size: 68rpx;
	font-weight: 900;
	color: #2F5243;
	margin-bottom: 24rpx;
	letter-spacing: 2rpx;
	text-shadow: 0 4rpx 8rpx rgba(47, 82, 67, 0.1);
}

.subtitle {
	font-size: 32rpx;
	font-weight: 700;
	color: #D4BA9F;
	letter-spacing: 4rpx;
}

/* Footer Action Buttons */
.action-footer {
	width: 100%;
	padding: 0 60rpx 100rpx 60rpx;
	box-sizing: border-box;
	z-index: 10;
	display: flex;
	flex-direction: column;
	gap: 32rpx;
}

.btn {
	width: 100%;
	height: 100rpx;
	border-radius: 50rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	border: none;
	margin: 0;
	padding: 0;
}

.btn::after {
	border: none; /* Remove uni-app default button borders */
}

.btn-text {
	font-size: 36rpx;
	font-weight: bold;
	letter-spacing: 30rpx; /* High tracking per design */
	text-indent: 30rpx; /* Visual centering compensation for tracking */
}

.btn-primary {
	background-color: #81B898;
	box-shadow: 0 10rpx 24rpx rgba(129, 184, 152, 0.35);
	
	.btn-text {
		color: #ffffff;
	}
}

.btn-hover-primary {
	background-color: #73aa8b;
	transform: scale(0.98);
}

.btn-secondary {
	background-color: #ffffff;
	box-shadow: 0 6rpx 20rpx rgba(0,0,0,0.06);
	
	.btn-text {
		color: #81B898;
	}
}

.btn-hover-secondary {
	background-color: #fafafa;
	transform: scale(0.98);
}
</style>
