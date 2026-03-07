<!--
  欢迎页 / 首屏引导页 (index/index.vue)
  功能：App 打开后的第一个页面，展示品牌 Logo 和标语
  包含"登录"和"注册"两个入口按钮
-->
<template>
	<view class="container">
		<!-- 弥散模糊背景圆：多个半透明圆形叠加，营造柔和的设计感 -->
		<view class="bg-blur bg-blur-1"></view>
		<view class="bg-blur bg-blur-2"></view>
		<view class="bg-blur bg-blur-3"></view>
		<view class="bg-blur bg-blur-4"></view>

		<!-- 中间内容区域：Logo + 标题 + 副标题 -->
		<view class="content">
			<!-- 圆形 Logo -->
			<view class="icon-wrap">
				<img :src="logoUrl" class="logo-img" />
			</view>
			<!-- 品牌名称 -->
			<text class="title">职途 · ProTrek</text>
			<!-- 副标题 / 品牌标语 -->
			<text class="subtitle">不妨先试试前路</text>
		</view>

		<!-- 底部按钮区域：登录 + 注册 -->
		<view class="action-footer">
			<!-- 主按钮：跳转到登录页 -->
			<button class="btn btn-primary" hover-class="btn-hover-primary" @tap="navToLogin">
				<text class="btn-text">登 录</text>
			</button>
			<!-- 次按钮：跳转到注册页（尚未实现） -->
			<button class="btn btn-secondary" hover-class="btn-hover-secondary" @tap="navToRegister">
				<text class="btn-text">注 册</text>
			</button>
		</view>
	</view>
</template>

<script setup lang="ts">
/**
 * 欢迎页逻辑
 * 目前只实现了"跳转到登录页"的功能
 */
import logoUrl from '@/static/logo.png'

/**
 * 点击"登录"按钮 → 跳转到登录页面
 */
const navToLogin = () => {
	uni.navigateTo({ url: '/pages/login/login' })
}

/**
 * 点击"注册"按钮
 */
const navToRegister = () => {
	uni.navigateTo({ url: '/pages/register/register' })
}
</script>

<style lang="scss" scoped>
/* ================== 页面容器 ================== */
.container {
	position: relative;
	width: 100vw;
	height: 100vh;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-between;
	overflow: hidden;
	
}

/* 弥散模糊背景圆（纯装饰，在手机上性能友好） */
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

/* ================== 中间内容区域 ================== */
.content {
	flex: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	z-index: 10;
	width: 100%;
	margin-top: 100rpx;   /* 稍微偏下，留出呼吸感 */
}
/* Logo 容器 */
.icon-wrap {
	width: 240rpx;
	height: 240rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 70rpx;
}
.logo-img {
	width: 100%;
	height: 100%;
	mix-blend-mode: multiply; /* 让白色背景变为透明 */
}
.icon-text {
	font-weight: 900;
	font-size: 40rpx;
	color: #000;
	letter-spacing: 2rpx;
}
/* 品牌名称 */
.title {
	font-size: 68rpx;
	font-weight: 900;
	color: #2d3436;
	margin-bottom: 24rpx;
	letter-spacing: 2rpx;
	text-shadow: 0 4rpx 8rpx rgba(47, 82, 67, 0.1);
}
/* 副标题 */
.subtitle {
	font-size: 32rpx;
	font-weight: 700;
	color: #D4BA9F;
	letter-spacing: 4rpx;
}

/* ================== 底部按钮区域 ================== */
.action-footer {
	width: 100%;
	padding: 0 60rpx 100rpx 60rpx;
	box-sizing: border-box;
	z-index: 10;
	display: flex;
	flex-direction: column;
	gap: 32rpx;           /* 两个按钮之间的间距 */
}
/* 按钮通用样式 */
.btn {
	width: 100%;
	height: 100rpx;
	border-radius: 50rpx;  /* 药丸形 */
	display: flex;
	align-items: center;
	justify-content: center;
	border: none;
	margin: 0;
	padding: 0;
}
/* 去除 Uni-app 按钮默认边框 */
.btn::after {
	border: none;
}
/* 按钮文字 */
.btn-text {
	font-size: 36rpx;
	font-weight: bold;
	letter-spacing: 30rpx;   /* 大字间距，匹配设计稿 */
	text-indent: 30rpx;      /* 配合 letter-spacing 使文字视觉居中 */
}
/* 主按钮（登录）：绿色实心 */
.btn-primary {
	background-color: #81B898;
	box-shadow: 0 10rpx 24rpx rgba(129, 184, 152, 0.35);
	.btn-text { color: #ffffff; }
}
.btn-hover-primary {
	background-color: #73aa8b;
	transform: scale(0.98);
}
/* 次按钮（注册）：白色描纹 */
.btn-secondary {
	background-color: #ffffff;
	box-shadow: 0 6rpx 20rpx rgba(0,0,0,0.06);
	.btn-text { color: #81B898; }
}
.btn-hover-secondary {
	background-color: #fafafa;
	transform: scale(0.98);
}
</style>
