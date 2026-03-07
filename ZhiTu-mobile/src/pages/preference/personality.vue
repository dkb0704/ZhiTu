<template>
	<view class="container" @click="closeAllDropdowns">
		<view class="top-bg">
			<view class="bg-blur bg-blur-1"></view>
			<view class="bg-blur bg-blur-2"></view>
			<text class="top-title">职途同行</text>
		</view>

		<view class="bottom-card" @click.stop>
			<view class="avatar-box">
				<img :src="snailUrl" class="logo-img" />
			</view>

			<view class="form-area">
				<view
					v-for="(item, idx) in fields"
					:key="idx"
					class="row-group"
				>
					<view
						class="dropdown-wrapper"
						:class="{ 'z-highest': activeDropdown === item.key }"
					>
						<view
							class="dropdown-input"
							:class="{ 'is-active': activeDropdown === item.key }"
							@click.stop="toggleDropdown(item.key)"
						>
							<text class="value-text" :class="{ 'has-val': item.value.value }">
								{{ item.value.value || item.placeholder }}
							</text>
							<u-icon
								name="arrow-right"
								color="#999"
								size="32rpx"
								class="arrow-icon"
								:class="{ 'rotate-down': activeDropdown === item.key }"
							></u-icon>
						</view>

						<view class="dropdown-menu" v-if="activeDropdown === item.key">
							<scroll-view scroll-y class="dropdown-scroll">
								<view
									class="dropdown-item"
									v-for="(opt, oi) in item.options"
									:key="oi"
									:class="{ 'selected': item.value.value === opt }"
									@click.stop="selectOption(item, opt)"
								>
									{{ opt }}
								</view>
							</scroll-view>
						</view>
					</view>

					<view class="search-btn" @click.stop="item.onTest">
						<u-icon name="search" color="#2F4F44" size="36rpx"></u-icon>
					</view>
				</view>
			</view>

			<button class="submit-btn" @click="handleSubmit">
				<u-icon name="arrow-right" color="#fff" size="48rpx" bold></u-icon>
			</button>

			<text class="skip-text" @click="handleSkip">暂时跳过</text>
		</view>
	</view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { updatePreferenceAPI } from '@/api/preference'
import snailUrl from '@/static/personality-snail.png'

const mbti = ref('')
const holland = ref('')
const bigFive = ref('')

const activeDropdown = ref<string | null>(null)

const MBTI_OPTIONS = [
	'INTJ', 'INTP', 'ENTJ', 'ENTP',
	'INFJ', 'INFP', 'ENFJ', 'ENFP',
	'ISTJ', 'ISFJ', 'ESTJ', 'ESFJ',
	'ISTP', 'ISFP', 'ESTP', 'ESFP'
]
const HOLLAND_OPTIONS = ['R 实用型', 'I 研究型', 'A 艺术型', 'S 社会型', 'E 企业型', 'C 常规型', 'RIASEC', '其他']
const BIG_FIVE_OPTIONS = ['高外向性', '高宜人性', '高责任心', '高神经质', '高开放性', '混合型', '暂未测评']

const toast = (msg: string) => uni.showToast({ title: msg, icon: 'none' })

const fields = computed(() => [
	{ key: 'mbti', value: mbti, placeholder: '请选择你的MBTI', options: MBTI_OPTIONS, onTest: () => toast('MBTI 测试功能暂未开通') },
	{ key: 'holland', value: holland, placeholder: '请选择你的霍兰德职业兴趣', options: HOLLAND_OPTIONS, onTest: () => toast('霍兰德测试功能暂未开通') },
	{ key: 'bigFive', value: bigFive, placeholder: '请选择你的Big Five人格', options: BIG_FIVE_OPTIONS, onTest: () => toast('Big Five 测试功能暂未开通') }
])

const toggleDropdown = (key: string) => {
	activeDropdown.value = activeDropdown.value === key ? null : key
}

const selectOption = (item: { value: ReturnType<typeof ref<string>> }, opt: string) => {
	item.value.value = opt
	activeDropdown.value = null
}

const closeAllDropdowns = () => {
	activeDropdown.value = null
}

/** 提交：至少选一项才允许提交，保存后跳转岗位页 */
const handleSubmit = async () => {
	const hasAny = mbti.value || holland.value || bigFive.value
	if (!hasAny) {
		uni.showToast({ title: '请至少选择一项性格类型', icon: 'none' })
		return
	}
	try {
		uni.showLoading({ title: '保存中...' })
		await updatePreferenceAPI({
			mbti: mbti.value || undefined,
			holland: holland.value || undefined,
			bigFive: bigFive.value || undefined
		})
		uni.hideLoading()
		uni.switchTab({ url: '/pages/job/index' })
	} catch (e: any) {
		uni.hideLoading()
		uni.showToast({ title: e?.message || e?.info || '保存失败，请稍后重试', icon: 'none' })
	}
}

/** 跳过：不保存，直接进入岗位浏览页 */
const handleSkip = () => {
	uni.switchTab({ url: '/pages/job/index' })
}
</script>

<style lang="scss" scoped>
.container {
	width: 100vw;
	height: 100vh;
	display: flex;
	flex-direction: column;
	overflow: hidden;
}

.top-bg {
	position: relative;
	width: 100%;
	height: 38vh;
	background-color: transparent;
	overflow: hidden;
}

.top-title {
	position: absolute;
	top: 42px;
	left: 17px;
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', 'Microsoft YaHei', sans-serif;
	font-size: 32px;
	font-weight: 900;
	color: #2d3436;
	letter-spacing: 2rpx;
	-webkit-text-stroke: 1px #2d3436;
	white-space: nowrap;
	z-index: 10;
}

.bg-blur {
	position: absolute;
	border-radius: 50%;
	filter: blur(50px);
	z-index: 0;
}
.bg-blur-1 { width: 400rpx; height: 400rpx; background: rgba(129, 184, 152, 0.2); top: -50rpx; left: -50rpx; }
.bg-blur-2 { width: 500rpx; height: 500rpx; background: rgba(129, 184, 152, 0.15); bottom: -100rpx; right: -100rpx; }

.bottom-card {
	position: relative;
	flex: 1;
	background-color: #ffffff;
	border-top-left-radius: 60rpx;
	border-top-right-radius: 60rpx;
	margin-top: -80rpx;
	z-index: 20;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 0 60rpx;
	box-shadow: 0 -10rpx 30rpx rgba(0, 0, 0, 0.02);
}

.avatar-box {
	position: absolute;
	top: -200rpx;
	right: 40rpx;
	left: auto;
	width: 286rpx;
	height: 268rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}
.logo-img {
	width: 100%;
	height: 100%;
	mix-blend-mode: multiply;
}

/* ======== 选择框区域 ======== */
/* 要调整选择框整体上下位置，改这里的 padding-top（如 200rpx、240rpx、280rpx） */
.form-area {
	width: 100%;
	display: flex;
	flex-direction: column;
	gap: 30rpx;
	padding-top: 240rpx;
}

.row-group {
	display: flex;
	flex-direction: row;
	align-items: flex-start;
	gap: 20rpx;
}

/* ======== 斑马纹自定义下拉（照搬 create.vue） ======== */
.dropdown-wrapper {
	position: relative;
	flex: 1;
	z-index: 1;
}

.dropdown-input {
	display: flex;
	align-items: center;
	justify-content: space-between;
	width: 100%;
	height: 100rpx;
	background-color: #ffffff;
	border: 2rpx solid #D7E3D2;
	border-radius: 40rpx;
	padding: 0 36rpx;
	box-sizing: border-box;
	transition: all 0.3s;
}

.dropdown-input.is-active {
	border-color: #81B898;
	box-shadow: 0 4rpx 12rpx rgba(129, 184, 152, 0.1);
}

.value-text {
	font-size: 28rpx;
	color: #999999;
	transition: color 0.3s;
	flex: 1;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}
.value-text.has-val {
	color: #333333;
	font-weight: 500;
}

.arrow-icon {
	transition: transform 0.3s ease;
}
.arrow-icon.rotate-down {
	transform: rotate(90deg);
}

.dropdown-menu {
	position: absolute;
	top: 108rpx;
	left: 0;
	width: 100%;
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8;
	border-radius: 30rpx;
	box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.06);
	overflow: hidden;
	z-index: 999;
	animation: slideDown 0.2s cubic-bezier(0.25, 0.8, 0.25, 1);
}

@keyframes slideDown {
	from { opacity: 0; transform: translateY(-10rpx); }
	to   { opacity: 1; transform: translateY(0); }
}

.dropdown-scroll {
	max-height: 440rpx;
	width: 100%;
}

.dropdown-item {
	padding: 24rpx 40rpx;
	font-size: 28rpx;
	color: #666666;
	background-color: #f5f7f3;
	transition: all 0.2s;
}

.dropdown-item:nth-child(even) {
	background-color: #d7e3d2;
}

.dropdown-item.selected {
	background-color: #dce8e0 !important;
	color: #2F5243;
	font-weight: bold;
}
.dropdown-item:active {
	opacity: 0.8;
}

.dropdown-wrapper.z-highest {
	z-index: 100 !important;
}

/* ======== 搜索按钮 ======== */
.search-btn {
	width: 80rpx;
	height: 80rpx;
	margin-top: 10rpx;
	border-radius: 40rpx;
	background-color: #D7E3D2;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-shrink: 0;
}
.search-btn:active {
	opacity: 0.8;
}

/* ======== 提交按钮 ======== */
.submit-btn {
	margin-top: 100rpx;
	width: 286rpx;
	height: 102rpx;
	border-radius: 50rpx;
	background-color: #87BE9D;
	box-shadow: 0rpx 4rpx 12rpx 4rpx rgba(0, 0, 0, 0.15), 0rpx 2rpx 4rpx 0rpx rgba(0, 0, 0, 0.3);
	border: none;
	display: flex;
	align-items: center;
	justify-content: center;
	&::after { border: none; }
}
.submit-btn:active {
	transform: scale(0.96);
	opacity: 0.9;
}

.skip-text {
	margin-top: 40rpx;
	font-family: 'FZLanTingHeiS-R-GB', 'PingFang SC', sans-serif;
	font-size: 28rpx;
	color: #87BE9D;
}
.skip-text:active {
	opacity: 0.7;
}
</style>
