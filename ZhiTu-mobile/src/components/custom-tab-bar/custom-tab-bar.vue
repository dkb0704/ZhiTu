<!--
  自定义悬浮底部导航栏组件 (custom-tab-bar.vue)
  功能：替代 Uni-app 自带的原生底部 TabBar，实现"悬浮药丸"造型
  用法：在每个主页面的 <template> 中放入 <custom-tab-bar currentPath="/pages/xxx/index" />
  参数：currentPath - 当前页面的路径，用于高亮对应的 Tab 图标
-->
<template>
	<view class="custom-tab-bar">
		<!-- 图谱 Tab -->
		<view class="tab-item" :class="{ 'active': currentPath === '/pages/map/index' }" @tap="switchTab('/pages/map/index')">
			<u-icon name="grid" :size="currentPath === '/pages/map/index' ? '56rpx' : '48rpx'" :color="currentPath === '/pages/map/index' ? '#81B898' : '#666'"></u-icon>
			<text class="tab-text">图谱</text>
		</view>
		<!-- 岗位 Tab -->
		<view class="tab-item" :class="{ 'active': currentPath === '/pages/job/index' }" @tap="switchTab('/pages/job/index')">
			<u-icon name="bag" :size="currentPath === '/pages/job/index' ? '56rpx' : '48rpx'" :color="currentPath === '/pages/job/index' ? '#81B898' : '#666'"></u-icon>
			<text class="tab-text">岗位</text>
		</view>
		<!-- 测试 Tab -->
		<view class="tab-item" :class="{ 'active': currentPath === '/pages/test/index' }" @tap="switchTab('/pages/test/index')">
			<u-icon name="file-text" :size="currentPath === '/pages/test/index' ? '56rpx' : '48rpx'" :color="currentPath === '/pages/test/index' ? '#81B898' : '#666'"></u-icon>
			<text class="tab-text">测试</text>
		</view>
		<!-- 我的 Tab -->
		<view class="tab-item" :class="{ 'active': currentPath === '/pages/my/index' }" @tap="switchTab('/pages/my/index')">
			<u-icon name="account" :size="currentPath === '/pages/my/index' ? '56rpx' : '48rpx'" :color="currentPath === '/pages/my/index' ? '#81B898' : '#666'"></u-icon>
			<text class="tab-text">我的</text>
		</view>
	</view>
</template>

<script setup lang="ts">
/**
 * 自定义 TabBar 组件逻辑
 * - 接收父组件传入的 currentPath（当前页面路径）
 * - 点击不同的 Tab 时，调用 uni.switchTab 进行页面切换
 */

// 定义组件接收的参数（Props）
const props = defineProps({
	currentPath: {
		type: String,
		required: true  // 必传：父组件必须告诉我"当前是哪个页面"
	}
})

/**
 * 切换到目标页面
 * 如果点击的 Tab 就是当前页面，则不做任何操作（避免重复跳转）
 */
const switchTab = (path: string) => {
	if (props.currentPath !== path) {
		uni.switchTab({ url: path })
	}
}
</script>

<style lang="scss" scoped>
/* 悬浮导航栏容器：固定在屏幕底部，药丸形状 */
.custom-tab-bar {
	position: fixed;
	bottom: 40rpx;
	left: 40rpx;
	right: 40rpx;
	height: 120rpx;
	background-color: #ffffff;
	border-radius: 60rpx;                            /* 大圆角 = 药丸形状 */
	box-shadow: 0 10rpx 30rpx rgba(47, 82, 67, 0.08); /* 淡绿色弥散阴影 */
	display: flex;
	justify-content: space-around;
	align-items: center;
	z-index: 999;       /* 确保在页面最上层 */
	padding: 0 20rpx;
}

/* 每个 Tab 项：垂直排列（图标在上，文字在下） */
.tab-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	gap: 6rpx;
	transition: all 0.3s ease;  /* 点击时的平滑过渡动画 */
}

/* Tab 文字默认样式 */
.tab-text {
	font-size: 24rpx;
	color: #666;
	font-weight: bold;
	transition: color 0.3s ease;
}

/* Tab 激活态：文字变为主题绿色 */
.tab-item.active .tab-text {
	color: #81B898;
}
</style>
