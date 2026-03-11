<!--
  测试页面 (test/index.vue)
  功能：开发调试用的测试页面（尚未开发具体内容）
  目前只是一个占位页面，包含底部自定义导航栏
-->
<template>
	<view class="container">
		<text class="title">测试页面</text>
		<!-- 自定义悬浮底部导航栏，当前页面高亮"测试" Tab -->
		<custom-tab-bar currentPath="/pages/test/index"></custom-tab-bar>
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
