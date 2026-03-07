<template>
	<!-- 点击外围任何地方时，尝试关闭当前打开的下拉菜单 -->
	<view class="container" @click="closeAllDropdowns">
		
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
			<view class="form-card" @click.stop> <!-- 阻止卡片本身的点击冒泡，避免误关 -->
				
				<!-- 卡片标题 -->
				<text class="card-title">简 历</text>
				
				<!-- ================= 表单项列表 ================= -->
				<view class="form-list">
					
					<!-- 1. 专业选择 -->
					<!-- @click.stop 防止点击选择框本身时触发最外层的 closeAll -->
					<view class="dropdown-wrapper" :class="{ 'z-highest': activeDropdown === 'major' }" @click.stop="toggleDropdown('major')">
						<view class="dropdown-input" :class="{ 'is-active': activeDropdown === 'major' }">
							<!-- 如果有值显示选中的值，否则显示 placeholder -->
							<text class="value-text" :class="{ 'has-val': formData.major }">
								{{ formData.major || '请选择你的专业' }}
							</text>
							<!-- 右侧箭头动画 -->
							<u-icon 
								name="arrow-right" 
								color="#999" 
								size="16"
								class="arrow-icon"
								:class="{ 'rotate-down': activeDropdown === 'major' }"
							></u-icon>
						</view>
						
						<!-- 下拉浮层 (仅在该项 active 时显示) -->
						<view class="dropdown-menu" v-if="activeDropdown === 'major'">
							<scroll-view scroll-y class="dropdown-scroll">
								<view 
									class="dropdown-item" 
									v-for="(item, index) in majorOptions" 
									:key="index"
									:class="{ 'selected': formData.major === item }"
									@click.stop="selectOption('major', item)"
								>
									{{ item }}
								</view>
							</scroll-view>
						</view>
					</view> <!-- 专业结束 -->

					<!-- 2. 最高学历（如博士/硕士/本科） -->
					<view class="dropdown-wrapper" :class="{ 'z-highest': activeDropdown === 'degree' }" @click.stop="toggleDropdown('degree')">
						<view class="dropdown-input" :class="{ 'is-active': activeDropdown === 'degree' }">
							<text class="value-text" :class="{ 'has-val': formData.degree }">
								{{ formData.degree || '请选择最高学历' }}
							</text>
							<!-- 此处顺便展示当已选时箭头颜色也可以随需求变化 -->
							<u-icon 
								name="arrow-right" 
								color="#555" 
								size="16"
								bold
								class="arrow-icon"
								:class="{ 'rotate-down': activeDropdown === 'degree' }"
							></u-icon>
						</view>
						
						<!-- 下拉浮层 -->
						<view class="dropdown-menu" v-if="activeDropdown === 'degree'">
							<scroll-view scroll-y class="dropdown-scroll">
								<view 
									class="dropdown-item" 
									v-for="(item, index) in degreeOptions" 
									:key="index"
									:class="{ 'selected': formData.degree === item }"
									@click.stop="selectOption('degree', item)"
								>
									{{ item }}
								</view>
							</scroll-view>
						</view>
					</view> <!-- 学历结束 -->

					<!-- 3. 年级选择 -->
					<view class="dropdown-wrapper" :class="{ 'z-highest': activeDropdown === 'grade' }" @click.stop="toggleDropdown('grade')">
						<view class="dropdown-input" :class="{ 'is-active': activeDropdown === 'grade' }">
							<text class="value-text" :class="{ 'has-val': formData.grade }">
								{{ formData.grade || '请选择你的年级' }}
							</text>
							<u-icon 
								name="arrow-right" 
								color="#999" 
								size="16"
								class="arrow-icon"
								:class="{ 'rotate-down': activeDropdown === 'grade' }"
							></u-icon>
						</view>
						
						<!-- 下拉浮层 -->
						<view class="dropdown-menu" v-if="activeDropdown === 'grade'">
							<scroll-view scroll-y class="dropdown-scroll">
								<view 
									class="dropdown-item" 
									v-for="(item, index) in gradeOptions" 
									:key="index"
									:class="{ 'selected': formData.grade === item }"
									@click.stop="selectOption('grade', item)"
								>
									{{ item }}
								</view>
							</scroll-view>
						</view>
					</view> <!-- 年级结束 -->

					<!-- 4. 工作地点选择 -->
					<view class="dropdown-wrapper" :class="{ 'z-highest': activeDropdown === 'location' }" @click.stop="toggleDropdown('location')">
						<view class="dropdown-input" :class="{ 'is-active': activeDropdown === 'location' }">
							<text class="value-text" :class="{ 'has-val': formData.location }">
								{{ formData.location || '请选择你心仪的工作地点' }}
							</text>
							<u-icon 
								name="arrow-right" 
								color="#999" 
								size="16"
								class="arrow-icon"
								:class="{ 'rotate-down': activeDropdown === 'location' }"
							></u-icon>
						</view>
						
						<!-- 下拉浮层 -->
						<view class="dropdown-menu" v-if="activeDropdown === 'location'">
							<scroll-view scroll-y class="dropdown-scroll">
								<view 
									class="dropdown-item" 
									v-for="(item, index) in locationOptions" 
									:key="index"
									:class="{ 'selected': formData.location === item }"
									@click.stop="selectOption('location', item)"
								>
									{{ item }}
								</view>
							</scroll-view>
						</view>
					</view> <!-- 工作地点结束 -->

				</view> <!-- 表单项列表结束 -->

				<!-- 底部提交大按钮 -->
				<button class="submit-btn" @click="handleSubmit">
					<u-icon name="arrow-right" color="#fff" size="32" bold></u-icon>
				</button>
				
			</view> <!-- 卡片结束 -->
		</view>

	</view>
</template>

<script setup lang="ts">
/**
 * 简历制作页 (create.vue)
 * 
 * 使用自定义的基于绝对定位的 Dropdown 浮层组件构建。
 * 提供四个选择项：专业、学历、年级、工作地点。
 */
import { ref, reactive } from 'vue'
import { updateProfileAPI } from '@/api/profile'

// ================ 页面状态 ================
// 当前哪个下拉菜单处于展开状态。值为 'major' | 'degree' | 'grade' | 'location' | null
const activeDropdown = ref<string | null>(null)

// 收集到的表单数据
const formData = reactive({
	major: '',
	degree: '',
	grade: '',
	location: ''
})

// ================ 数据字典配置 ================
const majorOptions = ['计算机科学与技术', '软件工程', '电子信息工程', '人工智能', '工商管理', '其他专业']
const degreeOptions = ['专科', '本科', '硕士', '博士']
const gradeOptions = ['大一', '大二', '大三', '大四', '研一', '研二', '研三', '应届毕业生']
const locationOptions = ['北京', '上海', '广州', '深圳', '杭州', '成都', '武汉', '其他城市']

// ================ 交互方法 ================
/**
 * 切换下拉菜单的展开/收起状态
 * @param key 需要操作的字段名
 */
const toggleDropdown = (key: string) => {
	// 如果点击的是当前已展开的，就关掉；如果点击的是其他的，则切换至目标
	if (activeDropdown.value === key) {
		activeDropdown.value = null
	} else {
		activeDropdown.value = key
	}
}

/**
 * 选中某一个选项（核心重点逻辑）
 * @param key   归属字段 
 * @param value 选中的文本
 */
const selectOption = (key: keyof typeof formData, value: string) => {
	// 1. 数据双向绑定赋值
	formData[key] = value
	// 2. 自动关闭所有的下拉框
	activeDropdown.value = null
}

/**
 * 关闭所有下拉菜单 (点击背景遮罩时调用)
 */
const closeAllDropdowns = () => {
	activeDropdown.value = null
}

/**
 * 点击绿色大按钮提交
 */
const handleSubmit = async () => {
	// 校验必填项
	if (!formData.major || !formData.degree || !formData.grade || !formData.location) {
		uni.showToast({
			title: '请完善所有简历信息哦',
			icon: 'none'
		})
		return
	}

	try {
		uni.showLoading({ title: '保存中...' })
		await updateProfileAPI({
			major: formData.major,
			education: formData.degree,
			grade: formData.grade,
			baseCities: JSON.stringify([formData.location])
		})
		uni.hideLoading()
		uni.navigateTo({ url: '/pages/resume/create2' })
	} catch (e: any) {
		uni.hideLoading()
		uni.showToast({ title: e?.message || e?.info || '保存失败，请稍后重试', icon: 'none' })
	}
}
</script>

<style lang="scss" scoped>
/* ================== 页面整体容器 ================== */
.container {
	position: relative;
	width: 100vw;
	height: 100vh;           /* 撑满全屏 */
	/* background removed to use global */
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
	height: 38vh;
	z-index: 1;
	pointer-events: none;
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

/* 标题区域包裹，绝对定位在左上角 */
.title-wrapper {
	position: absolute;
	top: 42px;                /* 距离顶部 42px */
	left: 17px;               /* 距离左侧 17px */
	z-index: 10;
}

/* “职途同行”大标题 (无下划线纯享版) */
.top-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', 'Microsoft YaHei', sans-serif;
	font-size: 32px;
	font-weight: 900;
	color: #2d3436;           /* 接近纯黑的深色，视觉更有冲击力 */
	letter-spacing: 2rpx;
	-webkit-text-stroke: 1px #2d3436;
}

/* ================== 居中核心表单区 ================== */
.center-content {
	position: relative;
	flex: 1;
	z-index: 20;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: flex-start; /* 从上往下排布 */
	padding-top: 420rpx;         /* 继续增加顶部距离，把整个白色卡片再往下推 */
}

/* 白色表单大卡片 */
.form-card {
	width: 630rpx;           /* 宽度尽量充满屏幕 */
	/* 卡片高度不再固定正方形，而是自适应内容 */
	min-height: 800rpx;      
	background-color: #ffffff;
	border-radius: 40rpx;    /* 较大的圆角 */
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 40rpx 40rpx 80rpx; /* 缩小顶部留白，让“简 历”更贴近卡片顶端 */
	box-shadow: 0 10rpx 40rpx rgba(129, 184, 152, 0.15);
	/* 注意由于下拉菜单超出卡片，切不可在此使用 overflow: hidden */
}

/* 绿色标题“简 历” */
.card-title {
	font-size: 44rpx;
	font-weight: 900;
	color: #81B898;
	letter-spacing: 20rpx; /* 故意拉开字距，营造留白极简感 */
	margin-bottom: 60rpx;
	// 修正由于 letter-spacing 导致最后多出的偏移，使视觉绝对居中
	transform: translateX(10rpx);
}

/* 盛放所有选择器的垂直列表 */
.form-list {
	width: 100%;
	display: flex;
	flex-direction: column;
	gap: 40rpx; /* 控制四个下拉框之间的垂直间距 */
}

/* ================== 自定义绝对定位 Dropdown 选择器核心代码 ================== */
.dropdown-wrapper {
	position: relative; /* 子元素的 absolute 定位基座，至关重要！ */
	width: 100%;
	z-index: 1; /* 默认 z-index */
}

/* 假装自己是个原生 input 框 */
.dropdown-input {
	display: flex;
	align-items: center;
	justify-content: space-between; /* 左右排开: 文字靠左，图标靠右 */
	width: 100%;
	height: 100rpx;
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8; /* 浅绿色边框 */
	border-radius: 40rpx;       /* 跑道形大胶囊圆角 */
	padding: 0 40rpx;
	box-sizing: border-box;
	transition: all 0.3s;
}

/* 当该下拉框打开时，边框变得深一些以示强调 */
.dropdown-input.is-active {
	border-color: #81B898;
	box-shadow: 0 4rpx 12rpx rgba(129, 184, 152, 0.1);
}

/* 文本域 */
.value-text {
	font-size: 28rpx;
	color: #999999; /* 默认 placeholder 浅灰色 */
	transition: color 0.3s;
	flex: 1;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
/* 如果选择了值，变成深色 */
.value-text.has-val {
	color: #333333;
	font-weight: 500;
}

/* 右侧箭头动画容器 */
.arrow-icon {
	transition: transform 0.3s ease;
}
/* 下降箭头：点击后顺时针旋转90度成为向下的 v */
.arrow-icon.rotate-down {
	transform: rotate(90deg);
}

/* ------ 悬浮下拉气泡菜单 ------ */
.dropdown-menu {
	position: absolute;
	top: 108rpx; /* 在输入框高度(100)+间隙(8)的下方悬浮 */
	left: 0;
	width: 100%;
	/* 必须给出底色和阴影区分层次 */
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8;
	border-radius: 30rpx; /* 菜单气泡的圆角 */
	box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.06);
	overflow: hidden;
	/* 提高层级，保证盖住下面的其他选择器。这里用了很大的值 */
	z-index: 999; 
	animation: slideDown 0.2s cubic-bezier(0.25, 0.8, 0.25, 1);
}

@keyframes slideDown {
	from { opacity: 0; transform: translateY(-10rpx); }
	to   { opacity: 1; transform: translateY(0); }
}

/* 滚动区域限定最高度 */
.dropdown-scroll {
	max-height: 440rpx; /* 可显示大约 5 6 行，其余滚动 */
	width: 100%;
}

/* 菜单项基础（奇数行） */
.dropdown-item {
	padding: 24rpx 40rpx;
	font-size: 28rpx;
	color: #666666;
	background-color: #f5f7f3; /* 指定的奇数行底色(极浅) */
	transition: all 0.2s;
}

/* 白绿交替斑马纹：偶数行 */
.dropdown-item:nth-child(even) {
	background-color: #d7e3d2; /* 指定的偶数行浅绿色 */
}

/* **重点：选中项 / Hover 绿色浅背景高亮** */
/* 使用 !important 保证即使是偶数行也能在选中时展示出明确的高亮状态 */
.dropdown-item.selected {
	background-color: #dce8e0 !important; 
	color: #2F5243;
	font-weight: bold;
}
.dropdown-item:active {
	opacity: 0.8;
}

/* 当 dropdown 处于激活态(z-highest)的父 wrapper 层级最高，避免气泡被下方兄弟遮挡 */
.dropdown-wrapper.z-highest {
	z-index: 100 !important;
}

/* ================== 原创提交按钮 (与注册页保持一致) ================== */
.submit-btn {
	margin-top: 150rpx; /* 大幅增加 marginTop，让按钮和输入框拉开距离，产生留白美 */
	width: 300rpx;
	height: 100rpx;
	border-radius: 50rpx;
	background-color: #81B898;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 10rpx 20rpx rgba(129, 184, 152, 0.4);
	border: none;
	&::after { border: none; }
	transition: all 0.2s;
}
.submit-btn:active {
	transform: scale(0.96);
	box-shadow: 0 5rpx 10rpx rgba(129, 184, 152, 0.2);
}
</style>
