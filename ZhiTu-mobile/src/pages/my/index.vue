<!--
  个人中心页面 (my/index.vue)
  功能：展示用户个人信息、设置等（尚未开发具体内容）
  目前只是一个占位页面，包含底部自定义导航栏
-->
<template>
	<view class="container">
		<text class="title">我的页面</text>
		<!-- 自定义悬浮底部导航栏，当前页面高亮"我的" Tab -->
		<custom-tab-bar currentPath="/pages/my/index"></custom-tab-bar>
	</view>
</template>

<script setup lang="ts">
/** 引入自定义底部导航栏组件 */
import CustomTabBar from '@/components/custom-tab-bar/custom-tab-bar.vue'
import { onMounted } from 'vue'

onMounted(() => {
	uni.hideTabBar()
})
</script>

<style scoped>
/* 页面容器：居中显示标题 */
.container {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	
}
.title {
	font-size: 40rpx;
	color: #333;
}
</style>
