<!--
  岗位卡片组件 (job-card.vue)
  功能：展示单条岗位信息（岗位名、薪资、公司、标签等）
  用法：<job-card :jobData="岗位数据对象" />
  数据字段来自后端 JobPositionEntity 实体类
-->
<template>
	<view class="job-card">
		<!-- 卡片头部：岗位名称（左） + 薪资范围（右） -->
		<view class="card-header">
			<text class="job-title">{{ jobData.title || jobData.jobTitle || '未知岗位' }}</text>
			<text class="job-salary">{{ jobData.salaryRange || '面议' }}</text>
		</view>
		<!-- 公司信息：行业 · 公司名 · 公司规模 -->
		<view class="card-meta">
			<text class="company-text">{{ jobData.industry || '未知行业' }} · {{ jobData.companyName || '未知公司' }} · {{ jobData.companySize || '规模不详' }}</text>
		</view>
		<!-- 标签区域：仅当数据存在时才显示对应标签 -->
		<view class="card-tags">
			<view class="tag" v-if="jobData.location">{{ jobData.location }}</view>
			<view class="tag" v-if="jobData.education">{{ jobData.education }}</view>
			<view class="tag" v-if="jobData.companyType">{{ jobData.companyType }}</view>
			<view class="tag" v-if="jobData.experience">{{ jobData.experience }}</view>
		</view>
	</view>
</template>

<script setup lang="ts">
/**
 * 岗位卡片组件
 * 接收一个 jobData 对象（包含岗位名称、薪资、公司等字段）
 * 组件内部只负责展示，不做任何数据请求
 */
import type { PropType } from 'vue'

defineProps({
	jobData: {
		type: Object as PropType<any>,
		default: () => ({})  // 默认空对象，避免渲染报错
	}
})
</script>

<style lang="scss" scoped>
/* 卡片容器：白底圆角 + 轻微绿色阴影 */
.job-card {
	background-color: #ffffff;
	border-radius: 30rpx;       /* 大圆角，匹配 Figma 设计 */
	padding: 36rpx;
	box-shadow: 0 4rpx 24rpx rgba(47, 82, 67, 0.04); /* 淡绿色弥散阴影 */
	display: flex;
	flex-direction: column;
}

/* 头部：岗位名 + 薪资左右排列 */
.card-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8rpx;
}
/* 岗位名称：深绿粗体 */
.job-title {
	font-size: 38rpx;
	font-weight: 900;
	color: #2F5243;
	letter-spacing: 2rpx;
}
/* 薪资：主题绿色粗体 */
.job-salary {
	font-size: 38rpx;
	font-weight: bold;
	color: #81B898;
}

/* 公司信息行 */
.card-meta {
	margin-bottom: 24rpx;
}
.company-text {
	font-size: 26rpx;
	color: #888888;
}

/* 标签区域：药丸形淡绿色标签 */
.card-tags {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}
.tag {
	padding: 6rpx 16rpx;
	background-color: #EAF1EB;  /* 极淡绿色底 */
	color: #6C947D;             /* 绿色文字 */
	font-size: 24rpx;
	font-weight: 500;
	border-radius: 12rpx;       /* 药丸形圆角 */
}
</style>
