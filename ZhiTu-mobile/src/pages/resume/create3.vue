<template>
	<!-- 简历制作第三步 -->
	<view class="container" @click="closeDropdown">
		
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
					
					<!-- 1. 曾获荣誉 (点击直接弹窗填多条) -->
					<view class="field-item" @click="openPopup('honors')">
						<text class="field-label" :class="{'has-data': formData.honors.length > 0}">
							{{ formData.honors.length > 0 ? `已填写 ${formData.honors.length} 项荣誉` : '请填写你曾获的荣誉' }}
						</text>
						<u-icon name="plus-circle" color="#666" size="20"></u-icon>
					</view>

					<!-- 2. 技能证书 (下拉选择器，与之前学历选择器风格一致) -->
					<view class="dropdown-wrapper" :class="{ 'z-highest': isSkillsDropdownOpen }" @click.stop="toggleSkillsDropdown">
						<view class="dropdown-input" :class="{ 'is-active': isSkillsDropdownOpen }">
							<text class="value-text" :class="{ 'has-val': formData.skills.length > 0 }">
								{{ formData.skills.length > 0 ? formData.skills.join(', ') : '请选择你的技能证书' }}
							</text>
							<u-icon 
								name="arrow-right" 
								color="#999" 
								size="16"
								class="arrow-icon"
								:class="{ 'rotate-down': isSkillsDropdownOpen }"
							></u-icon>
						</view>
						<view class="dropdown-menu" v-if="isSkillsDropdownOpen">
							<scroll-view scroll-y class="dropdown-scroll">
								<view 
									class="dropdown-item" 
									v-for="(item, index) in skillOptions" 
									:key="index"
									:class="{ 'selected': formData.skills.includes(item) }"
									@click.stop="toggleSkillOption(item)"
								>
									{{ item }}
								</view>
							</scroll-view>
						</view>
					</view>

					<!-- 3. 在校经历 -->
					<view class="field-item" @click="openPopup('schoolExperiences')">
						<text class="field-label" :class="{'has-data': formData.schoolExperiences.length > 0}">
							{{ formData.schoolExperiences.length > 0 ? `已填写 ${formData.schoolExperiences.length} 项在校经历` : '请填写你的在校经历' }}
						</text>
						<u-icon name="plus-circle" color="#666" size="20"></u-icon>
					</view>

					<!-- 4. 项目经历 -->
					<view class="field-item" @click="openPopup('projectExperiences')">
						<text class="field-label" :class="{'has-data': formData.projectExperiences.length > 0}">
							{{ formData.projectExperiences.length > 0 ? `已填写 ${formData.projectExperiences.length} 项项目经历` : '请填写你的项目经历' }}
						</text>
						<u-icon name="plus-circle" color="#666" size="20"></u-icon>
					</view>

					<!-- 5. 工作经历 -->
					<view class="field-item" @click="openPopup('workExperiences')">
						<text class="field-label" :class="{'has-data': formData.workExperiences.length > 0}">
							{{ formData.workExperiences.length > 0 ? `已填写 ${formData.workExperiences.length} 项工作经历` : '请填写你的工作经历' }}
						</text>
						<u-icon name="plus-circle" color="#666" size="20"></u-icon>
					</view>

				</view> <!-- 表单项列表结束 -->

				<!-- 底部提交大按钮 -->
				<button class="submit-btn" @click="handleSubmit">
					<u-icon name="arrow-right" color="#fff" size="32" bold></u-icon>
				</button>
				
				<!-- 暂时跳过 -->
				<text class="skip-text" @click="handleSkip">暂时跳过</text>

			</view> <!-- 卡片结束 -->
		</view>

		<!-- ================== 核心模态弹窗 (Popup) ================== -->
		<view class="popup-mask" v-if="isPopupOpen" @click.stop="closePopup">
			<view class="popup-container" @click.stop>
				<!-- 弹窗标题栏 -->
				<view class="popup-header">
					<text class="popup-title">{{ currentPopupTitle }}</text>
					<!-- 右上角➕号，点击追加新的一行输入框 -->
					<view class="add-icon-btn" @click="addPopupItem">
						<u-icon name="plus" color="#81B898" size="24" bold></u-icon>
					</view>
				</view>

				<!-- 动态输入框列表区域 (可滚动) -->
				<scroll-view scroll-y class="popup-scroll-area">
					<view 
						class="textarea-wrapper" 
						v-for="(text, index) in tempPopupData" 
						:key="index"
					>
						<textarea 
							class="custom-textarea"
							v-model="tempPopupData[index]"
							:placeholder="`${getPlaceholderPrefix()}${index + 1}`"
							placeholder-style="color:#ccc; font-size:28rpx;"
							auto-height
						/>
						<!-- 滑出/删除小按钮 (可选体验增强) -->
						<view class="delete-btn" v-if="tempPopupData.length > 1" @click="removePopupItem(index)">
							<u-icon name="minus-circle" color="#e74c3c" size="18"></u-icon>
						</view>
					</view>
				</scroll-view>

				<!-- 底部操作按钮栏 -->
				<view class="popup-footer">
					<text class="btn-cancel" @click="closePopup">取消</text>
					<text class="btn-save" @click="savePopupData">保存</text>
				</view>
			</view>
		</view> <!-- Popup End -->

	</view>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { updateProfileAPI } from '@/api/profile'

// ================= 数据定义 =================
const formData = reactive({
	honors: [] as string[],
	skills: [] as string[],
	schoolExperiences: [] as string[],
	projectExperiences: [] as string[],
	workExperiences: [] as string[]
})

// 技能证书的选项字典
const skillOptions = ['CET-4', 'CET-6', '普通话等级证书', '计算机二级', '计算机三级', '教师资格证', '会计从业资格证', '驾驶证', 'PMP', '其他']
const isSkillsDropdownOpen = ref(false)

const toggleSkillsDropdown = () => {
	isSkillsDropdownOpen.value = !isSkillsDropdownOpen.value
}

const closeDropdown = () => {
	isSkillsDropdownOpen.value = false
}

// 多选逻辑：点击一个选项切换其选中状态
const toggleSkillOption = (item: string) => {
	const idx = formData.skills.indexOf(item)
	if (idx === -1) {
		formData.skills.push(item)
	} else {
		formData.skills.splice(idx, 1)
	}
}

// ================= 弹窗逻辑状态 =================
const isPopupOpen = ref(false)
const currentEditKey = ref<keyof typeof formData | null>(null) // 当前在编辑哪个字段

// 临时存放弹窗内的数据（防抖/取消机制：只有点保存才会赋值回 formData）
const tempPopupData = ref<string[]>([])

// 计算当前弹窗的中文标题
const currentPopupTitle = computed(() => {
	switch (currentEditKey.value) {
		case 'honors': return '荣誉奖项'
		case 'skills': return '技能证书'
		case 'schoolExperiences': return '在校经历'
		case 'projectExperiences': return '项目经历'
		case 'workExperiences': return '工作经历'
		default: return '填写信息'
	}
})

// 获取输入框 Placeholder 前缀
const getPlaceholderPrefix = () => {
	switch (currentEditKey.value) {
		case 'honors': return '请填写你的荣誉'
		case 'skills': return '请填写你的技能证书'
		case 'schoolExperiences': return '请填写你的在校经历'
		case 'projectExperiences': return '请填写你的项目经历'
		case 'workExperiences': return '请填写你的工作经历'
		default: return '内容'
	}
}

// ================= 交互方法 =================

// 打开某个字段的弹窗
const openPopup = (key: keyof typeof formData) => {
	currentEditKey.value = key
	
	// 从真实数据中克隆一份到临时数据（解耦引用）。
	// 如果本来是空的，默认给个长度为1的空字符串数组，给用户留个空框。
	if (formData[key].length === 0) {
		tempPopupData.value = ['']
	} else {
		tempPopupData.value = [...formData[key]]
	}
	
	isPopupOpen.value = true
}

// 在弹窗内点击右上角 +，新增一个空输入框
const addPopupItem = () => {
	tempPopupData.value.push('')
}

// 在弹窗内删除某一条输入框
const removePopupItem = (index: number) => {
	tempPopupData.value.splice(index, 1)
}

// 点击取消或遮罩，直接关闭，不保存 temp 数据
const closePopup = () => {
	isPopupOpen.value = false
	currentEditKey.value = null
}

// 点击保存，回写数据并过滤掉纯空项
const savePopupData = () => {
	if (currentEditKey.value) {
		// 过滤掉完全没填字符串的项，避免存入无用的脏数据
		const validData = tempPopupData.value.filter(item => item.trim() !== '')
		formData[currentEditKey.value] = validData
	}
	isPopupOpen.value = false
}

// ================= 页面级按钮 =================
const handleSubmit = async () => {
	console.log('第三页提交的数据:', formData)
	try {
		uni.showLoading({ title: '保存中...' })
		await updateProfileAPI({
			honors: JSON.stringify(formData.honors),
			skills: JSON.stringify(formData.skills),
			campus: JSON.stringify(formData.schoolExperiences),
			projects: JSON.stringify(formData.projectExperiences),
			internships: JSON.stringify(formData.workExperiences)
		})
		uni.hideLoading()
		uni.navigateTo({ url: '/pages/resume/create4' })
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

/* 顶部背景与弥散光 */
.top-bg {
	position: absolute;
	top: 0; left: 0;
	width: 100%; height: 38vh;
	z-index: 1; pointer-events: none;
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
	position: absolute; top: 42px; left: 17px; z-index: 10;
}
.top-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', sans-serif;
	font-size: 32px; font-weight: 900; color: #2d3436; 
	letter-spacing: 2rpx; -webkit-text-stroke: 1px #2d3436;
}

/* ================== 中央表单卡片 (基础骨架复用) ================== */
.center-content {
	position: relative; flex: 1; z-index: 20;
	display: flex; flex-direction: column; align-items: center; justify-content: flex-start; 
	padding-top: 420rpx;         
}
.form-card {
	width: 630rpx; min-height: 800rpx; background-color: #ffffff;
	border-radius: 40rpx; display: flex; flex-direction: column; align-items: center;
	padding: 40rpx 40rpx 80rpx; box-shadow: 0 10rpx 40rpx rgba(129, 184, 152, 0.15);
}
.card-title {
	font-size: 44rpx; font-weight: 900; color: #81B898;
	letter-spacing: 20rpx; margin-bottom: 60rpx; transform: translateX(10rpx);
}

.form-list {
	width: 100%; display: flex; flex-direction: column; gap: 40rpx; 
}

/* ====== 这里是第三页特有的条目样式 ====== */
/* 假装是一个输入框，其实是个按钮触发器 */
.field-item {
	display: flex; align-items: center; justify-content: space-between; 
	width: 100%; height: 100rpx;
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8; 
	border-radius: 40rpx;       
	padding: 0 40rpx; box-sizing: border-box;
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

/* ====== 下拉选择器样式 (与 create.vue 保持一致) ====== */
.dropdown-wrapper {
	position: relative; width: 100%; z-index: 1;
}
.dropdown-input {
	display: flex; align-items: center; justify-content: space-between;
	width: 100%; height: 100rpx; background-color: #ffffff;
	border: 2rpx solid #E5EBE8; border-radius: 40rpx;
	padding: 0 40rpx; box-sizing: border-box; transition: all 0.3s;
}
.dropdown-input.is-active {
	border-color: #81B898;
	box-shadow: 0 4rpx 12rpx rgba(129, 184, 152, 0.1);
}
.value-text {
	font-size: 28rpx; color: #999999; transition: color 0.3s;
	flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.value-text.has-val { color: #333333; font-weight: 500; }
.arrow-icon { transition: transform 0.3s ease; }
.arrow-icon.rotate-down { transform: rotate(90deg); }
.dropdown-menu {
	position: absolute; top: 108rpx; left: 0; width: 100%;
	background-color: #ffffff; border: 2rpx solid #E5EBE8;
	border-radius: 30rpx; box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.06);
	overflow: hidden; z-index: 999;
	animation: slideDown 0.2s cubic-bezier(0.25, 0.8, 0.25, 1);
}
@keyframes slideDown {
	from { opacity: 0; transform: translateY(-10rpx); }
	to   { opacity: 1; transform: translateY(0); }
}
.dropdown-scroll { max-height: 440rpx; width: 100%; }
.dropdown-item {
	padding: 24rpx 40rpx; font-size: 28rpx; color: #666666;
	background-color: #f5f7f3; transition: all 0.2s;
}
.dropdown-item:nth-child(even) { background-color: #d7e3d2; }
.dropdown-item.selected {
	background-color: #81B898 !important; color: #ffffff; font-weight: bold;
}
.dropdown-item:active { opacity: 0.8; }
.dropdown-wrapper.z-highest { z-index: 100 !important; }

/* 底部按钮 (与前两页一致) */
.submit-btn {
	margin-top: 80rpx; width: 300rpx; height: 100rpx; border-radius: 50rpx;
	background-color: #81B898; display: flex; align-items: center; justify-content: center;
	box-shadow: 0 10rpx 20rpx rgba(129, 184, 152, 0.4); border: none; transition: all 0.2s;
	&::after { border: none; }
}
.submit-btn:active { transform: scale(0.96); box-shadow: 0 5rpx 10rpx rgba(129, 184, 152, 0.2); }

.skip-text {
	margin-top: 40rpx; font-size: 28rpx; color: #81B898; font-weight: 500; letter-spacing: 2rpx;
}

/* ================== 弹窗模态框 UI 设计 ================== */
.popup-mask {
	position: fixed; top: 0; left: 0; right: 0; bottom: 0;
	background-color: rgba(0, 0, 0, 0.4); /* 半透明黑底遮罩 */
	z-index: 999; /* 必须极高，盖住所有东西 */
	display: flex; align-items: center; justify-content: center;
	animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
	from { opacity: 0; }
	to { opacity: 1; }
}

.popup-container {
	width: 600rpx;
	max-height: 80vh;
	background-color: #ffffff;
	border-radius: 40rpx;
	display: flex; flex-direction: column;
	overflow: hidden; /* 删除按钮已改为内部定位，恢复 hidden 确保圆角渲染正确 */
	box-shadow: 0 30rpx 60rpx rgba(0,0,0,0.15);
	animation: popUp 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

@keyframes popUp {
	from { transform: scale(0.9); opacity: 0; }
	to { transform: scale(1); opacity: 1; }
}

/* 弹窗顶部 */
.popup-header {
	display: flex; align-items: center; justify-content: space-between;
	padding: 40rpx 40rpx 20rpx;
}
.popup-title {
	font-size: 36rpx; font-weight: 900; color: #333333;
}
.add-icon-btn {
	padding: 10rpx; /* 增加点击热区 */
}
.add-icon-btn:active {
	opacity: 0.6;
}

/* 中间滚动及输入区域 */
.popup-scroll-area {
	width: 100%;
	max-height: 50vh;
	padding: 0 40rpx;
	box-sizing: border-box;
}

/* 多行文本框包裹器 */
.textarea-wrapper {
	position: relative;
	width: 100%;
	border: 2rpx solid #c3ddcc;
	border-radius: 20rpx;
	margin-bottom: 24rpx;
	background-color: #FAFCFB;
	box-sizing: border-box; /* 确保边框包含在宽度内，不会超出父元素 */
	transition: border-color 0.3s;
}
.textarea-wrapper:focus-within {
	border-color: #81B898;
}

.custom-textarea {
	width: 100%; min-height: 120rpx; padding: 24rpx; box-sizing: border-box;
	font-size: 28rpx; color: #333; line-height: 1.5;
}

/* 删除小按钮定位在文本框内部右上角，避免被外层裁剪 */
.delete-btn {
	position: absolute; right: 10rpx; top: 10rpx;
	background-color: transparent; border-radius: 50%;
	padding: 6rpx; z-index: 10;
}

/* 弹窗底部操作区 */
.popup-footer {
	display: flex; align-items: center; justify-content: flex-end;
	padding: 30rpx 40rpx 40rpx; gap: 40rpx;
}
.btn-cancel {
	font-size: 30rpx; color: #999999;
}
.btn-cancel:active { opacity: 0.6; }

.btn-save {
	font-size: 32rpx; color: #81B898; font-weight: bold;
}
.btn-save:active { opacity: 0.6; }
</style>
