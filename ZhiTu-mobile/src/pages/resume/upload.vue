<template>
	<view class="container">
		
		<!-- 顶部背景区域 -->
		<view class="top-bg">
			<!-- 弥散模糊圆：营造页面整体淡雅氛围 -->
			<view class="bg-blur bg-blur-1"></view>
			<view class="bg-blur bg-blur-2"></view>
			
			<!-- 页面大标题 -->
			<view class="title-wrapper">
				<text class="top-title">职途同行</text>
			</view>
		</view>

		<!-- 居中核心内容区 -->
		<view class="center-content">
			
			<!-- 上传触发卡片 -->
			<view class="upload-card" @click="handleUpload">
				<!-- 卡片内部：绿色的上传图标(向上箭头组合) -->
				<view class="upload-icon-wrapper">
					<!-- 使用原生 uView 图标组件拼凑出向上的上传效果，或者使用自带的 push 图标 -->
					<!-- 这里我们自己用线条手绘一个极简的向上上传图标 -->
					<view class="custom-upload-icon">
						<view class="arrow-up"></view>
						<view class="arrow-line"></view>
						<view class="base-line"></view>
					</view>
				</view>
				
				<!-- 支持格式文本 -->
				<text class="format-text">PDF / WORD / JPG</text>
			</view>

			<!-- 底部描述文案 -->
			<view class="bottom-desc" @click="handleCreate">
				<text class="normal-text">上传或</text>
				<text class="highlight-text">制作一份简历</text>
			</view>
			
		</view>

	</view>
</template>

<script setup lang="ts">
/**
 * 简历上传页 (upload.vue)
 * 
 * 注册成功后的引导页面。
 * 当前功能为 UI 占位，预留了真实唤起文件选择或进入原生简历制作工具的方法槽。
 */

/** 
 * 点击中心卡片：上传本地简历
 * TODO: 接入 uni.chooseFile 或 uni.chooseImage 逻辑 
 */
const handleUpload = () => {
	console.log('触发：上传本地简历文件')
	uni.showToast({
		title: '即将打开文件选择器...',
		icon: 'none'
	})
}

/** 
 * 点击底部高亮文字：在线制作简历
 * 跳往真实制作表单页
 */
const handleCreate = () => {
	uni.navigateTo({
		url: '/pages/resume/create'
	})
}
</script>

<style lang="scss" scoped>
/* ================== 页面整体容器 ================== */
.container {
	position: relative;
	width: 100vw;
	height: 100vh;           /* 撑满全屏 */
	
	overflow: hidden;
	display: flex;
	flex-direction: column;
}

/* ================== 顶部背景与标题 ================== */
.top-bg {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100vh;            /* 让背景漫射整个屏幕 */
	z-index: 1;
}

/* 弥散模糊圆 */
.bg-blur {
	position: absolute;
	border-radius: 50%;
	filter: blur(80px);       /* 极度模糊，营造环境光 */
	z-index: 0;
}
/* 左上方偏绿 */
.bg-blur-1 { 
	width: 500rpx; 
	height: 500rpx; 
	background: rgba(129, 184, 152, 0.25); 
	top: -100rpx; 
	left: -100rpx; 
}
/* 右下方偏一点隐隐的光感 */
.bg-blur-2 { 
	width: 600rpx; 
	height: 600rpx; 
	background: rgba(129, 184, 152, 0.15); 
	bottom: 10% ; 
	right: -100rpx; 
}

/* 标题区域包裹，绝对定位在左上角 */
.title-wrapper {
	position: absolute;
	top: 42px;                /* 距离顶部 42px */
	left: 17px;               /* 距离左侧 17px */
	z-index: 10;
	display: flex;
	flex-direction: column;
	align-items: flex-start;
}

/* “职途同行”大标题 */
.top-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', 'Microsoft YaHei', sans-serif;
	font-size: 32px;
	font-weight: 900;
	color: #2d3436;
	letter-spacing: 2rpx;
	-webkit-text-stroke: 1px #2d3436;
}

/* 标题下方的蓝色横线标识 */
.title-underline {
	width: 100%;             /* 和文字等宽 (因为 wrapper 的 align-items 是 flex-start) */
	height: 8rpx;
	background-color: #1A73E8; /* 设计图中的亮蓝色 */
	margin-top: 4rpx;        /* 紧贴文字下方 */
	border-radius: 4rpx;
}

/* ================== 居中核心内容区 ================== */
.center-content {
	position: relative;
	flex: 1;
	z-index: 20;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center; /* 纯垂直居中 */
	padding-bottom: 50rpx;   /* 微调整体中心偏上一点点视觉更舒服 */
}

/* ================== 上传大卡片 ================== */
.upload-card {
	width: 420rpx;
	height: 420rpx;          /* 完美的正方形 */
	background-color: #ffffff;
	border-radius: 40rpx;    /* 圆润的倒角 */
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	/* 明显的发散型柔和阴影，让卡片悬浮感极强 */
	box-shadow: 0 20rpx 60rpx rgba(129, 184, 152, 0.2);
	transition: all 0.2s;    /* 增加交互动画过渡 */
}

/* 点击反馈效果 */
.upload-card:active {
	transform: scale(0.96);
	box-shadow: 0 10rpx 30rpx rgba(129, 184, 152, 0.1);
}

/* 包裹自绘图标的容器 */
.upload-icon-wrapper {
	margin-bottom: 40rpx;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 120rpx;
}

/* ====== 纯 CSS 手绘：极简上传图标 ====== */
.custom-upload-icon {
	position: relative;
	width: 80rpx;
	height: 80rpx;
}
/* 1. 向上箭头 (用 border 模拟) */
.custom-upload-icon .arrow-up {
	position: absolute;
	top: 0;
	left: 50%;
	transform: translateX(-50%) translateY(0) rotate(45deg);
	width: 25rpx;
	height: 25rpx;
	border-left: 6rpx solid #81B898;
	border-top: 6rpx solid #81B898;
}
/* 2. 箭头竖直线 */
.custom-upload-icon .arrow-line {
	position: absolute;
	top: 4rpx;
	left: 50%;
	transform: translateX(-50%);
	width: 6rpx;
	height: 48rpx;
	background-color: #81B898;
	border-radius: 3rpx;
}
/* 3. 底部托盘线 (U型) */
.custom-upload-icon .base-line {
	position: absolute;
	bottom: -10rpx;
	left: 50%;
	transform: translateX(-50%);
	width: 60rpx;
	height: 25rpx;
	border: 6rpx solid #81B898;
	border-top: none; /* 去掉上边框形成 U 字托底 */
	border-radius: 0 0 10rpx 10rpx;
}

/* 格式限定文字 */
.format-text {
	font-size: 32rpx;
	font-weight: 900;
	color: #81B898;
	letter-spacing: 2rpx;
}

/* ================== 卡片底部引导文字 ================== */
.bottom-desc {
	margin-top: 80rpx;       /* 距离卡片底部的距离 */
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 20rpx;          /* 增加点击热区 */
}

.normal-text {
	font-size: 36rpx;
	font-weight: 700;
	color: #7A8D84;          /* 设计图上这里是偏灰绿色的暗调 */
	letter-spacing: 2rpx;
}

/* 突出的“制作”两字 */
.highlight-text {
	font-size: 36rpx;
	font-weight: 900;
	color: #81B898;          /* 亮绿色表示这具有交互诱导性 */
	letter-spacing: 2rpx;
}

.bottom-desc:active {
	opacity: 0.7;            /* 点击反馈 */
}
</style>
