<template>
	<view class="container">
		
		<!-- 顶部背景区域 -->
		<view class="top-bg">
			<!-- 弥散模糊圆：营造页面氛围 -->
			<view class="bg-blur bg-blur-1"></view>
			<view class="bg-blur bg-blur-2"></view>
			<!-- 页面标题 -->
			<text class="top-title">职途同行</text>
		</view>

		<!-- 底部白色卡片区域 -->
		<view class="bottom-card">
			
			<!-- 卡片标题 -->
			<text class="card-title">注 册</text>

			<!-- 表单区域 -->
			<view class="form-wrapper">
				
				<!-- 用户名输入框 -->
				<view class="input-box">
					<input 
						type="text" 
						v-model="account" 
						placeholder="用户名" 
						class="native-input" 
						placeholder-class="placeholder-style"
					/>
				</view>

				<!-- 密码输入框 -->
				<view class="input-box">
					<input 
						:type="showPassword ? 'text' : 'password'" 
						v-model="password" 
						placeholder="密码" 
						class="native-input" 
						placeholder-class="placeholder-style"
					/>
					<!-- 切换密码可见性的图标 -->
					<view class="eye-icon" @click="togglePassword">
						<u-icon 
							:name="showPassword ? 'eye-fill' : 'eye-off'" 
							size="20" 
							color="#999"
						></u-icon>
					</view>
				</view>

				<!-- 手机号输入框 -->
				<view class="input-box">
					<input 
						type="number" 
						v-model="phone" 
						placeholder="手机号" 
						maxlength="11"
						class="native-input" 
						placeholder-class="placeholder-style"
					/>
				</view>

				<!-- 邮箱输入框 -->
				<view class="input-box">
					<input 
						type="text" 
						v-model="email" 
						placeholder="邮箱地址" 
						class="native-input" 
						placeholder-class="placeholder-style"
					/>
				</view>

			</view> <!-- 表单结束 -->

			<!-- 提交按钮：药丸状，内含箭头图标，底部带柔和阴影 -->
			<button class="submit-btn" :disabled="loading" @click="handleRegister">
				<!-- 如果在请求后端，显示 loading，否则显示白色右箭头 -->
				<u-loading-icon v-if="loading" color="#fff" size="24"></u-loading-icon>
				<u-icon v-else name="arrow-right" color="#fff" size="28" bold></u-icon>
			</button>

			<!-- 底部登录链接 -->
			<view class="bottom-links">
				<text class="link-text" @click="navToLogin">已有账号？点击登录</text>
			</view>

		</view>
	</view>
</template>

<script setup lang="ts">
/**
 * 注册页面 (register.vue)
 * 
 * 用户输入：用户名 (account), 密码 (password), 手机号 (phone), 邮箱 (email)
 * 点击提交后，先进行前端正则校验，通过后调用后端 API。
 */
import { ref } from 'vue'
import { registerAPI } from '@/api/auth'

// ========== 响应式数据 ==========
const account = ref('')      // 用户名
const password = ref('')     // 密码
const phone = ref('')        // 手机号
const email = ref('')        // 邮箱
const loading = ref(false)   // 请求状态
const showPassword = ref(false) // 密码可见性开关

/** 切换密码明文/密文显示 */
const togglePassword = () => {
	showPassword.value = !showPassword.value
}

/** 跳转到登录页 */
const navToLogin = () => {
	uni.navigateTo({ url: '/pages/login/login' })
}

/** 
 * 执行注册前的前端校验逻辑
 * 返回 true 表示所有格式无误；返回 false 表示有错（已弹窗提示）
 */
const validateForm = (): boolean => {
	// 1. 判空
	if (!account.value.trim()) {
		uni.showToast({ title: '请输入用户名', icon: 'none' })
		return false
	}
	if (!password.value) {
		uni.showToast({ title: '请输入密码', icon: 'none' })
		return false
	}
	if (!phone.value.trim()) {
		uni.showToast({ title: '请输入手机号', icon: 'none' })
		return false
	}
	if (!email.value.trim()) {
		uni.showToast({ title: '请输入邮箱', icon: 'none' })
		return false
	}

	// 2. 密码长度校验 (与后端约束一致)
	if (password.value.length < 6 || password.value.length > 20) {
		uni.showToast({ title: '密码长度需在6-20位之间', icon: 'none' })
		return false
	}

	// 3. 手机号正则校验: 11位，1开头，第二位3-9
	const phoneRegex = /^1[3-9]\d{9}$/
	if (!phoneRegex.test(phone.value)) {
		uni.showToast({ title: '手机号格式不正确', icon: 'none' })
		return false
	}

	// 4. 邮箱正则校验
	const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/
	if (!emailRegex.test(email.value)) {
		uni.showToast({ title: '邮箱格式不正确', icon: 'none' })
		return false
	}

	return true
}

/**
 * 点击“注册”按钮 -> 校验 -> 请求后端
 */
const handleRegister = async () => {
	// 防抖：正在提交中则拒绝重复连点
	if (loading.value) return

	// 前端校验拦截
	if (!validateForm()) return

	loading.value = true

	try {
		// 调用 api/auth.ts 中配置的注册接口
		const res = await registerAPI({
			nickname: account.value,
			password: password.value,
			phone: phone.value,
			email: email.value
		})

		// 注册成功：将 Token 和新用户的基本信息保存到手机本地存储
		// 这步极其重要，可防止用上一个登录账号的旧缓存污染当前新用户的资料
		uni.setStorageSync('token', res.token)
		uni.setStorageSync('userInfo', JSON.stringify({
			userId: res.userId,
			nickname: res.nickname
		}))

		// 后端正常返回 -> 直接跳转到上传简历页，不再额外弹出成功提示或延时等待
		uni.redirectTo({ url: '/pages/resume/upload' })

	} catch (error: any) {
		// 后端返回的业务提示（如「该手机号已注册」「该邮箱已注册」）或网络错误，直接展示
		uni.showToast({
			title: error?.message || error?.info || '注册失败，请稍后重试',
			icon: 'none'
		})
	} finally {
		// 不管成功失败，最后关闭 loading 转圈
		loading.value = false
	}
}
</script>

<style lang="scss" scoped>
/* ================== 页面整体容器 ================== */
.container {
	position: relative;
	width: 100vw;
	height: 100vh;           /* 撑满全屏高度 */
	display: flex;
	flex-direction: column;  /* 垂直分为 上半背景区 + 下半卡片区 */
	
	overflow: hidden;        /* 防止弥散圆溢出引起滚动条 */
}

/* ================== 顶部背景与标题 ================== */
.top-bg {
	position: relative;
	height: 40vh;             /* 占据屏幕上边约 40% 的高度 */
	background-color: transparent; /* 移除以前的纯色背景，融入底层渐变 */
	border-bottom-left-radius: 40rpx; 
	border-bottom-right-radius: 40rpx;
	overflow: hidden;
	display: flex;
	align-items: flex-start;  /* 大标题靠上 */
	padding: 42px 0 0 17px;   /* 精确定位：距上42px 距左17px */
}

/* “职途同行”大标题 */
.top-title {
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', 'Microsoft YaHei', sans-serif;
	font-size: 32px;
	font-weight: 900;
	color: #2d3436;
	letter-spacing: 2rpx;
	-webkit-text-stroke: 1px #2d3436;
	z-index: 10;
}

/* 弥散模糊圆：营造柔和光感 */
.bg-blur {
	position: absolute;
	border-radius: 50%;
	filter: blur(50px);
	z-index: 0;
}
.bg-blur-1 { width: 400rpx; height: 400rpx; background: rgba(129, 184, 152, 0.2); top: -50rpx; left: -50rpx; }
.bg-blur-2 { width: 500rpx; height: 500rpx; background: rgba(129, 184, 152, 0.15); bottom: -100rpx; right: -100rpx; }

/* ================== 底部白色卡片区域 ================== */
.bottom-card {
	position: relative;
	flex: 1;                 /* 填充剩下的所有高度空间 */
	background-color: #ffffff;
	border-top-left-radius: 60rpx;   /* 巨大圆角结构 */
	border-top-right-radius: 60rpx;
	margin-top: -80rpx;      /* 往上提，盖住一部分 top-bg 从而产生重叠层次感 */
	z-index: 20;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 60rpx 60rpx 0;
	box-shadow: 0 -10rpx 30rpx rgba(0,0,0,0.02);
}

/* ================== 表单内容区 ================== */
/* 卡片标题：“注 册” */
.card-title {
	font-size: 44rpx;
	font-weight: 900;
	color: #81B898;
	letter-spacing: 20rpx; /* 字体间距拉开，营造呼吸感 */
	margin-bottom: 60rpx;
}

.form-wrapper {
	width: 100%;
	display: flex;
	flex-direction: column;
	gap: 40rpx; /* 控制四个输入框之间的垂直间距 */
}

/* 独立抽出的输入框容器：白底、圆角、淡淡边框 */
.input-box {
	display: flex;
	align-items: center;
	background-color: #ffffff;
	border: 2rpx solid #E5EBE8;
	border-radius: 40rpx;
	height: 100rpx;
	padding: 0 40rpx;
}

/* 原生输入框，占满剩余空间 */
.native-input {
	flex: 1;
	height: 100%;
	font-size: 30rpx;
	color: #333333;
}

/* 原生输入框的 placeholder 样式 */
.placeholder-style {
	color: #999999;
	font-size: 28rpx;
}

/* 眼睛图标容器：保证居中 */
.eye-icon {
	display: flex;
	align-items: center;
	margin-left: 20rpx;
}

/* ================== 原创提交按钮 ================== */
/* 
 * 宽大的椭圆按钮，带有一点底部阴影
 * 中间通常只放一个向右的白色箭头，极简现代风
 */
.submit-btn {
	margin-top: 80rpx;
	width: 300rpx;
	height: 100rpx;
	border-radius: 50rpx;     /* 胶囊形状 */
	background-color: #81B898;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 10rpx 20rpx rgba(129, 184, 152, 0.4);
	border: none;
	// 消除微信小程序按钮默认边框
	&::after { border: none; }
}

/* 按钮点击反馈 */
.submit-btn:active {
	transform: scale(0.96);    /* 按下时稍微收缩 */
	opacity: 0.9;
}

/* ================== 底部辅助链接 ================== */
.bottom-links {
	margin-top: 40rpx;
	display: flex;
	width: 100%;
	justify-content: center;
}

.link-text {
	font-size: 26rpx;
	color: #999999;
}

.link-text:active {
	opacity: 0.7;
}
</style>
