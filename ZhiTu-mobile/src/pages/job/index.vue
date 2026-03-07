<!--
  岗位浏览页面 (job/index.vue)
  功能：展示来自后端数据库的真实岗位列表，支持触底自动加载下一页
  数据来源：后端 /job/list 分页接口（共约 9955 条岗位数据）
-->
<template>
	<view class="container">
		<!-- 弥散模糊背景圆：纯装饰，营造柔和的浅绿色氛围 -->
		<view class="bg-blur bg-blur-1"></view>
		<view class="bg-blur bg-blur-2"></view>

		<!-- 顶部标题区 -->
		<view class="header">
			<text class="page-title">职途万象</text>
		</view>

		<!-- 筛选分类区：包含分类 Tab 和筛选/添加按钮 -->
		<view class="filter-section">
			<!-- 分类标签栏（目标岗位 / 意向岗位 / 手动） -->
			<view class="tabs-scroll">
				<u-tabs :list="tabList" @click="clickTab" lineColor="transparent" activeStyle="color: #ffffff; background-color: #81B898; border-radius: 30rpx; padding: 6rpx 20rpx; font-weight: bold;" inactiveStyle="color: #666; font-weight: bold;"></u-tabs>
			</view>
			<view class="filter-actions">
				<u-icon name="list-dot" size="44rpx" color="#666" @click="openFilter"></u-icon>
				<u-icon name="plus-circle" size="44rpx" color="#666" @click="addPost"></u-icon>
			</view>
		</view>

		<!-- 岗位列表区：可滚动，触底自动加载更多 -->
		<scroll-view scroll-y class="job-list" @scrolltolower="loadMore">
			<view class="list-content">
				<!-- 循环渲染每个岗位卡片，数据来自后端真实接口 -->
				<job-card v-for="(item, index) in jobList" :key="item.id || index" :jobData="item"></job-card>
				
				<!-- 加载状态提示 -->
				<view class="loading-state">
					<text v-if="loading">加载中...</text>
					<text v-else-if="jobList.length >= total && total > 0">没有更多了</text>
				</view>
			</view>
			<!-- 底部占位：防止最后一张卡片被悬浮导航栏遮挡 -->
			<view class="bottom-padding"></view>
		</scroll-view>

		<!-- 自定义悬浮底部导航栏 -->
		<custom-tab-bar currentPath="/pages/job/index"></custom-tab-bar>
	</view>
</template>

<script setup lang="ts">
/**
 * 岗位浏览页的业务逻辑
 * 核心功能：
 *   1. 页面加载时自动请求第 1 页岗位数据
 *   2. 用户滑到底部时，自动请求下一页（无限滚动/懒加载）
 *   3. 岗位数据通过 job-card 组件逐条渲染
 */
import { ref, onMounted } from 'vue'
import JobCard from '@/components/job-card/job-card.vue'
import CustomTabBar from '@/components/custom-tab-bar/custom-tab-bar.vue'
import { getJobList, type JobEntity } from '@/api/job'

// ========== 分类 Tab 数据 ==========
const tabList = ref([
	{ name: '目标岗位' },
	{ name: '意向岗位' },
	{ name: '手动' }
])

// ========== 分页 & 数据状态 ==========
const jobList = ref<JobEntity[]>([])  // 当前已加载的所有岗位数据
const page = ref(1)                   // 当前请求的页码
const size = 10                       // 每页请求的数量
const total = ref(0)                  // 后端返回的岗位总数
const loading = ref(false)            // 是否正在加载数据

/**
 * 核心函数：从后端获取岗位数据
 * - 第 1 页时替换整个列表
 * - 后续页码时追加到列表末尾
 */
const fetchJobs = async () => {
	// 防重：如果正在加载，或者已经全部加载完毕，直接返回
	if (loading.value || (jobList.value.length >= total.value && total.value > 0)) return
	loading.value = true
	try {
		const res = await getJobList({ page: page.value, size })
		if (page.value === 1) {
			// 第 1 页：用新数据替换旧数据
			jobList.value = res.list || []
		} else {
			// 后续页：追加到现有列表末尾
			jobList.value = [...jobList.value, ...(res.list || [])]
		}
		total.value = res.total || 0
	} catch (e: any) {
		uni.showToast({ title: e?.message || e?.info || '加载失败，请稍后重试', icon: 'none' })
	} finally {
		loading.value = false
	}
}

/**
 * 触底加载更多：当用户滑到列表底部时自动触发
 */
const loadMore = () => {
	if (jobList.value.length < total.value) {
		page.value += 1
		fetchJobs()
	}
}

/**
 * 生命周期：页面首次加载时自动获取第 1 页数据
 */
onMounted(() => {
	uni.hideTabBar()
	fetchJobs()
})

/** 分类 Tab 点击事件（尚未实现具体筛选逻辑） */
const clickTab = (item: any) => {
	console.log('点击了分类:', item)
}

/** 筛选按钮点击（尚未实现） */
const openFilter = () => {
	console.log('打开筛选')
}

/** 添加按钮点击（尚未实现） */
const addPost = () => {
	console.log('添加岗位')
}
</script>

<style lang="scss" scoped>
/* ================== 页面容器 ================== */
.container {
	position: relative;
	display: flex;
	flex-direction: column;
	height: 100vh;
	
	overflow: hidden;
}

/* 弥散模糊背景圆（纯装饰） */
.bg-blur {
	position: absolute;
	border-radius: 50%;
	filter: blur(60px);
	z-index: 0;
}
.bg-blur-1 { width: 500rpx; height: 500rpx; background: rgba(129, 184, 152, 0.25); top: -100rpx; left: -100rpx; }
.bg-blur-2 { width: 400rpx; height: 400rpx; background: rgba(129, 184, 152, 0.15); top: 10%; right: -50rpx; }

/* ================== 顶部标题 ================== */
.header {
	padding: 100rpx 40rpx 20rpx;
	z-index: 10;
}
.page-title {
	font-size: 64rpx;
	font-weight: 900;
	color: #2F5243;
	letter-spacing: 4rpx;
}

/* ================== 筛选分类区 ================== */
.filter-section {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 40rpx 20rpx 40rpx;
	z-index: 10;
}
.tabs-scroll {
	flex: 1;
	overflow: hidden;
}
:deep(.u-tabs__wrapper__nav) {
	align-items: center;
}
.filter-actions {
	display: flex;
	align-items: center;
	gap: 20rpx;
	margin-left: 10rpx;
}

/* ================== 岗位列表 ================== */
.job-list {
	flex: 1;
	overflow-y: scroll;
	z-index: 10;
}
.list-content {
	padding: 10rpx 40rpx;
	display: flex;
	flex-direction: column;
	gap: 30rpx;
}
/* 底部占位高度：防止最后一张卡被悬浮 TabBar 遮住 */
.bottom-padding {
	height: 120rpx;
}
</style>
