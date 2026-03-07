<template>
	<!-- 简历制作第二步 -->
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
			<view class="form-card" @click.stop> 
				
				<!-- 卡片标题 -->
				<text class="card-title">简 历</text>
				
				<!-- ================= 表单项列表 ================= -->
				<view class="form-list">
					
					<!-- 1. 意向岗位 -->
					<view class="dropdown-wrapper" :class="{ 'z-highest': activeDropdown === 'position' }" @click.stop="toggleDropdown('position')">
						<view class="dropdown-input" :class="{ 'is-active': activeDropdown === 'position' }">
							<text class="value-text" :class="{ 'has-val': formData.position }">
								{{ formData.position || '请选择你的意向岗位' }}
							</text>
							<u-icon 
								name="arrow-right" 
								color="#999" 
								size="16"
								class="arrow-icon"
								:class="{ 'rotate-down': activeDropdown === 'position' }"
							></u-icon>
						</view>
						
						<view class="dropdown-menu" v-if="activeDropdown === 'position'">
							<scroll-view scroll-y class="dropdown-scroll">
								<view 
									class="dropdown-item" 
									v-for="(item, index) in positionOptions" 
									:key="index"
									:class="{ 'selected': formData.position === item }"
									@click.stop="selectOption('position', item)"
								>
									{{ item }}
								</view>
							</scroll-view>
						</view>
					</view> <!-- 意向岗位结束 -->

					<!-- 2. 教育背景（与第三步填写经历同款：点击进入弹窗编辑） -->
					<view class="field-item" @click="openEduPopup">
						<text class="field-label" :class="{ 'has-data': formData.educationBackground.length > 0 && hasAnyEduContent }">
							{{ eduSummaryText }}
						</text>
						<u-icon name="plus-circle" color="#666" size="20"></u-icon>
					</view>

					<!-- 教育背景弹窗 -->
					<view class="popup-mask" v-if="isEduPopupOpen" @click.stop="closeEduPopup">
						<view class="popup-container edu-popup" @click.stop>
							<view class="popup-header">
								<text class="popup-title">教育背景</text>
								<view class="add-icon-btn" @click="addEduEntryInPopup">
									<u-icon name="plus" color="#81B898" size="24" bold></u-icon>
								</view>
							</view>
							<scroll-view scroll-y class="popup-scroll-area">
								<view
									v-for="(item, idx) in tempEduList"
									:key="idx"
									class="edu-popup-row"
								>
									<view
										class="edu-degree-picker"
										:class="{ 'is-active': eduPopupActiveDropdown === idx }"
										@click.stop="toggleEduDegreeDropdown(idx)"
									>
										<text class="edu-degree-text" :class="{ 'has-val': item.degree }">
											{{ item.degree || '学历层次' }}
										</text>
										<u-icon name="arrow-down" color="#999" size="14"
											:class="{ 'rotate-down': eduPopupActiveDropdown === idx }"></u-icon>
									</view>
									<input
										class="edu-school-input"
										v-model="item.school"
										placeholder="学校名称"
										placeholder-class="edu-placeholder"
									/>
									<view class="delete-btn" v-if="tempEduList.length > 1" @click.stop="removeEduEntryInPopup(idx)">
										<u-icon name="minus-circle" color="#e74c3c" size="18"></u-icon>
									</view>
								</view>
							</scroll-view>
							<!-- 学历选项放在 scroll-view 外，避免被裁剪或无法响应点击 -->
							<view class="edu-degree-panel" v-if="eduPopupActiveDropdown !== null">
								<text class="edu-degree-panel-title">选择学历层次</text>
								<view class="edu-degree-options">
									<view
										class="edu-degree-opt"
										v-for="(opt, oi) in degreeLevelOptions"
										:key="oi"
										:class="{ 'selected': tempEduList[eduPopupActiveDropdown]?.degree === opt }"
										@click.stop="selectEduDegreeInPopup(eduPopupActiveDropdown, opt)"
									>{{ opt }}</view>
								</view>
							</view>
							<view class="popup-footer">
								<text class="btn-cancel" @click="closeEduPopup">取消</text>
								<text class="btn-save" @click="saveEduPopup">保存</text>
							</view>
						</view>
					</view>

					<!-- 3. 绩点输入 (根据原型图这里是输入框 / 输入框 格式) -->
					<!-- 虽然设计图上看似普通行，但中间有 "/" 分隔两个小输入框 -->
					<view class="gpa-wrapper">
						<text class="gpa-label">请填写你的绩点</text>
						<view class="gpa-inputs">
							<!-- type="digit" 调起带小数点的数字键盘 -->
							<input 
								type="digit" 
								class="gpa-input-box" 
								placeholder="" 
								v-model="formData.gpaScore" 
								placeholder-style="color:#ccc" 
								@blur="validateGpa('score')"
							/>
							<text class="gpa-slash">/</text>
							<input 
								type="digit" 
								class="gpa-input-box" 
								placeholder="" 
								v-model="formData.gpaTotal" 
								placeholder-style="color:#ccc" 
								@blur="validateGpa('total')"
							/>
						</view>
					</view>

					<!-- 4. 政治面貌 -->
					<view class="dropdown-wrapper" :class="{ 'z-highest': activeDropdown === 'political' }" @click.stop="toggleDropdown('political')">
						<view class="dropdown-input" :class="{ 'is-active': activeDropdown === 'political' }">
							<text class="value-text" :class="{ 'has-val': formData.political }">
								{{ formData.political || '请选择你的政治面貌' }}
							</text>
							<u-icon 
								name="arrow-right" 
								color="#999" 
								size="16"
								class="arrow-icon"
								:class="{ 'rotate-down': activeDropdown === 'political' }"
							></u-icon>
						</view>
						
						<view class="dropdown-menu" v-if="activeDropdown === 'political'">
							<scroll-view scroll-y class="dropdown-scroll">
								<view 
									class="dropdown-item" 
									v-for="(item, index) in politicalOptions" 
									:key="index"
									:class="{ 'selected': formData.political === item }"
									@click.stop="selectOption('political', item)"
								>
									{{ item }}
								</view>
							</scroll-view>
						</view>
					</view> <!-- 政治面貌结束 -->

				</view> <!-- 表单项列表结束 -->

				<!-- 底部提交大按钮 -->
				<button class="submit-btn" @click="handleSubmit">
					<u-icon name="arrow-right" color="#fff" size="32" bold></u-icon>
				</button>
				
				<!-- 暂时跳过 -->
				<text class="skip-text" @click="handleSkip">暂时跳过</text>

			</view> <!-- 卡片结束 -->
		</view>

	</view>
</template>

<script setup lang="ts">
/**
 * 简历制作第二步 (create2.vue)
 * 意向岗位、教育背景（各阶段学校：本科/硕士/博士在哪）、绩点、政治面貌
 * 教育背景与第三步「填写经历」同款：主列表一行，点击弹窗内编辑多条。
 */
import { ref, reactive, computed } from 'vue'
import { updateProfileAPI } from '@/api/profile'

const activeDropdown = ref<string | null>(null)

const formData = reactive({
	position: '',
	educationBackground: [] as { degree: string; school: string }[],
	gpaScore: '',
	gpaTotal: '',
	political: ''
})

// 数据字典配置
const positionOptions = ['前端开发工程师', '后端开发工程师', '产品经理', 'UI设计师', '数据分析师', '算法工程师']
const degreeLevelOptions = ['专科', '本科', '硕士', '博士']
const politicalOptions = ['中共党员', '中共预备党员', '共青团员', '群众', '其他党派']

// 教育背景弹窗状态
const isEduPopupOpen = ref(false)
const tempEduList = ref<{ degree: string; school: string }[]>([])
const eduPopupActiveDropdown = ref<number | null>(null)

const hasAnyEduContent = computed(() =>
	formData.educationBackground.some((e) => e.degree?.trim() || e.school?.trim())
)
const eduSummaryText = computed(() => {
	if (!hasAnyEduContent.value) return '请填写你的教育背景（如本科、硕士所在学校）'
	const n = formData.educationBackground.filter((e) => e.degree || e.school).length
	return n > 0 ? `已填写 ${n} 段教育背景` : '请填写你的教育背景（如本科、硕士所在学校）'
})

const openEduPopup = () => {
	closeAllDropdowns()
	if (formData.educationBackground.length === 0) {
		tempEduList.value = [{ degree: '', school: '' }]
	} else {
		tempEduList.value = formData.educationBackground.map((e) => ({ ...e }))
	}
	eduPopupActiveDropdown.value = null
	isEduPopupOpen.value = true
}

const closeEduPopup = () => {
	isEduPopupOpen.value = false
	eduPopupActiveDropdown.value = null
}

const addEduEntryInPopup = () => {
	tempEduList.value.push({ degree: '', school: '' })
}

const removeEduEntryInPopup = (idx: number) => {
	tempEduList.value.splice(idx, 1)
	if (eduPopupActiveDropdown.value === idx) eduPopupActiveDropdown.value = null
	else if (eduPopupActiveDropdown.value !== null && eduPopupActiveDropdown.value > idx) {
		eduPopupActiveDropdown.value = eduPopupActiveDropdown.value - 1
	}
}

const toggleEduDegreeDropdown = (idx: number) => {
	eduPopupActiveDropdown.value = eduPopupActiveDropdown.value === idx ? null : idx
}

const selectEduDegreeInPopup = (idx: number, value: string) => {
	tempEduList.value[idx].degree = value
	eduPopupActiveDropdown.value = null
}

const saveEduPopup = () => {
	formData.educationBackground = tempEduList.value.filter((e) => e.degree?.trim() || e.school?.trim())
	if (formData.educationBackground.length === 0) formData.educationBackground = []
	isEduPopupOpen.value = false
	eduPopupActiveDropdown.value = null
}

const toggleDropdown = (key: string) => {
	if (activeDropdown.value === key) {
		activeDropdown.value = null
	} else {
		activeDropdown.value = key
	}
}

const selectOption = (key: 'position' | 'political', value: string) => {
	formData[key] = value
	activeDropdown.value = null
}

const closeAllDropdowns = () => {
	activeDropdown.value = null
}

/**
 * 绩点输入框失去焦点时的校验逻辑
 * @param field 当前校验的字段 ('score' 或 'total')
 */
const validateGpa = (field: 'score' | 'total') => {
	const scoreStr = formData.gpaScore
	const totalStr = formData.gpaTotal

	if (!scoreStr || !totalStr) return // 如果有一边没填完，先不强制阻拦，等最后提交统一拦

	const score = parseFloat(scoreStr)
	const total = parseFloat(totalStr)

	// 如果输入的不是合法数字
	if (isNaN(score) || isNaN(total)) {
		return
	}

	// 核心校验：分子不能大于分母
	if (score > total) {
		uni.showToast({
			title: '绩点不能大于总绩点喔',
			icon: 'none',
			duration: 2000
		})
		// 校验失败时清空刚刚填写的那个错误字段
		if (field === 'score') {
			formData.gpaScore = ''
		} else {
			formData.gpaTotal = ''
		}
	}
}

const handleSubmit = async () => {
	try {
		uni.showLoading({ title: '保存中...' })
		const educationBackgroundJson =
			formData.educationBackground.length > 0
				? JSON.stringify(
						formData.educationBackground
							.filter((e) => e.degree || e.school)
							.map((e) => ({ degree: e.degree, school: e.school || '' }))
					)
				: undefined
		await updateProfileAPI({
			targetPosition: formData.position,
			educationBackground: educationBackgroundJson,
			gpa: formData.gpaScore && formData.gpaTotal ? `${formData.gpaScore}/${formData.gpaTotal}` : undefined,
			politicalStatus: formData.political
		})
		uni.hideLoading()
		uni.navigateTo({ url: '/pages/resume/create3' })
	} catch (e: any) {
		uni.hideLoading()
		uni.showToast({ title: e?.message || e?.info || '保存失败，请稍后重试', icon: 'none' })
	}
}

const handleSkip = () => {
	uni.reLaunch({
		url: '/pages/job/index'
	})
}
</script>

<style lang="scss" scoped>
/* ================== 页面整体容器 ================== */
.container {
	position: relative;
	width: 100vw;
	height: 100vh;           
	
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

.title-wrapper {
	position: absolute;
	top: 42px;                
	left: 17px;               
	z-index: 10;
}

.top-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', 'Microsoft YaHei', sans-serif;
	font-size: 32px;
	font-weight: 900;
	color: #2d3436;           
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
	justify-content: flex-start; 
	padding-top: 420rpx;         
}

.form-card {
	width: 630rpx;           
	min-height: 800rpx;      
	background-color: #ffffff;
	border-radius: 40rpx;    
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 40rpx 40rpx 80rpx; 
	box-shadow: 0 10rpx 40rpx rgba(129, 184, 152, 0.15);
}

.card-title {
	font-size: 44rpx;
	font-weight: 900;
	color: #81B898;
	letter-spacing: 20rpx; 
	margin-bottom: 60rpx;
	transform: translateX(10rpx);
}

.form-list {
	width: 100%;
	display: flex;
	flex-direction: column;
	gap: 40rpx; 
}

/* 教育背景：与第三步「填写经历」同款一行 + 弹窗编辑 */
.field-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	width: 100%;
	height: 100rpx;
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8;
	border-radius: 40rpx;
	padding: 0 40rpx;
	box-sizing: border-box;
	transition: all 0.3s;
}
.field-item:active {
	border-color: #81B898;
}
.field-label {
	font-size: 28rpx;
	color: #999999;
}
.field-label.has-data {
	color: #333333;
	font-weight: 500;
}

/* 教育背景弹窗 */
.popup-mask {
	position: fixed;
	top: 0; left: 0; right: 0; bottom: 0;
	background-color: rgba(0, 0, 0, 0.4);
	z-index: 999;
	display: flex;
	align-items: center;
	justify-content: center;
	animation: fadeIn 0.3s ease;
}
@keyframes fadeIn {
	from { opacity: 0; }
	to { opacity: 1; }
}
.popup-container.edu-popup {
	width: 600rpx;
	max-height: 80vh;
	background-color: #ffffff;
	border-radius: 40rpx;
	display: flex;
	flex-direction: column;
	overflow: hidden;
	box-shadow: 0 30rpx 60rpx rgba(0,0,0,0.15);
	animation: popUp 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
@keyframes popUp {
	from { transform: scale(0.9); opacity: 0; }
	to { transform: scale(1); opacity: 1; }
}
.popup-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 40rpx 40rpx 20rpx;
}
.popup-title {
	font-size: 36rpx;
	font-weight: 900;
	color: #333333;
}
.add-icon-btn {
	padding: 10rpx;
}
.add-icon-btn:active {
	opacity: 0.6;
}
.popup-scroll-area {
	width: 100%;
	max-height: 50vh;
	padding: 0 40rpx;
	box-sizing: border-box;
}
.popup-footer {
	display: flex;
	align-items: center;
	justify-content: flex-end;
	padding: 30rpx 40rpx 40rpx;
	gap: 40rpx;
}
.btn-cancel {
	font-size: 30rpx;
	color: #999999;
}
.btn-cancel:active { opacity: 0.6; }
.btn-save {
	font-size: 32rpx;
	color: #81B898;
	font-weight: bold;
}
.btn-save:active { opacity: 0.6; }

.edu-popup-row {
	position: relative;
	display: flex;
	align-items: center;
	gap: 16rpx;
	min-height: 88rpx;
	padding: 20rpx 0;
	border-bottom: 1rpx solid #f0f0f0;
	box-sizing: border-box;
}
.edu-popup-row:last-child {
	border-bottom: none;
}
.edu-degree-picker {
	position: relative;
	display: flex;
	align-items: center;
	gap: 8rpx;
	min-width: 140rpx;
	height: 56rpx;
	padding: 0 16rpx;
	background-color: #f5f7f3;
	border-radius: 16rpx;
	border: 2rpx solid #E5EBE8;
}
.edu-degree-text {
	font-size: 26rpx;
	color: #999;
	flex: 1;
}
.edu-degree-text.has-val {
	color: #333;
	font-weight: 500;
}
.edu-degree-picker .rotate-down {
	transform: rotate(180deg);
}
.edu-degree-picker.is-active {
	border-color: #81B898;
	background-color: #f0f7f3;
}
/* 学历选择面板：放在 scroll-view 外，保证点击有效 */
.edu-degree-panel {
	padding: 24rpx 40rpx;
	border-top: 1rpx solid #f0f0f0;
	background-color: #fafcfb;
}
.edu-degree-panel-title {
	display: block;
	font-size: 26rpx;
	color: #666;
	margin-bottom: 16rpx;
}
.edu-degree-options {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}
.edu-degree-opt {
	padding: 16rpx 28rpx;
	font-size: 28rpx;
	color: #333;
	background-color: #fff;
	border: 2rpx solid #E5EBE8;
	border-radius: 32rpx;
}
.edu-degree-opt.selected {
	background-color: #81B898;
	color: #fff;
	border-color: #81B898;
}
.edu-degree-opt:active {
	opacity: 0.9;
}
.edu-school-input {
	flex: 1;
	height: 56rpx;
	font-size: 28rpx;
	color: #333;
	padding: 0 16rpx;
	background-color: #fafcfb;
	border: 2rpx solid #E5EBE8;
	border-radius: 16rpx;
}
.edu-placeholder {
	color: #ccc;
	font-size: 26rpx;
}
.edu-popup-row .delete-btn {
	position: static;
	padding: 8rpx;
}

/* 绩点输入行特殊样式 */
.gpa-wrapper {
	display: flex;
	align-items: center;
	justify-content: space-between; 
	width: 100%;
	height: 100rpx;
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8; 
	border-radius: 40rpx;       
	padding: 0 40rpx;
	box-sizing: border-box;
}

.gpa-label {
	font-size: 28rpx;
	color: #999999;
}

.gpa-inputs {
	display: flex;
	align-items: center;
	gap: 16rpx;
}

.gpa-input-box {
	width: 80rpx;
	height: 50rpx;
	border: 2rpx solid #E5EBE8;
	border-radius: 12rpx;
	text-align: center;
	font-size: 28rpx;
	color: #333;
}

.gpa-slash {
	font-size: 32rpx;
	color: #666;
	font-weight: bold;
}

/* ================== 自定义绝对定位 Dropdown 选择器 ================== */
.dropdown-wrapper {
	position: relative; 
	width: 100%;
	z-index: 1; 
}

.dropdown-input {
	display: flex;
	align-items: center;
	justify-content: space-between; 
	width: 100%;
	height: 100rpx;
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8; 
	border-radius: 40rpx;       
	padding: 0 40rpx;
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
	box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.06);
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

/* ================== 原创提交按钮与跳过 ================== */
.submit-btn {
	margin-top: 100rpx; /* 由于下方加入了跳过按钮，适当减小 margin */
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

/* 暂时跳过 文字按钮 */
.skip-text {
	margin-top: 40rpx;
	font-size: 28rpx;
	color: #81B898;
	font-weight: 500;
	letter-spacing: 2rpx;
}
.skip-text:active {
	opacity: 0.6;
}
</style>
