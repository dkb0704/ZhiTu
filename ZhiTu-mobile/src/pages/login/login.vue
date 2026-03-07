<!--
  登录页面 (login.vue)
  功能：用户通过手机号或邮箱 + 密码登录
  登录成功后，会将服务器返回的 JWT Token 存储到本地，然后跳转到岗位浏览页
-->
<template>
	<view class="container">
		<!-- 顶部渐变色背景区域：营造品牌感 -->
		<view class="top-bg">
			<!-- 两个模糊圆形，模拟"弥散光"设计效果 -->
			<view class="bg-blur bg-blur-1"></view>
			<view class="bg-blur bg-blur-2"></view>
			<!-- 页面标题 -->
			<text class="top-title">职途同行</text>
		</view>

		<!-- 底部白色卡片区域：包含登录表单 -->
		<view class="bottom-card">
			
			<!-- 悬浮 Logo 图标（半嵌入顶部背景） -->
			<view class="avatar-box">
				<img :src="logoUrl" class="logo-img" />
			</view>

			<!-- 表单区域 -->
			<view class="form-wrapper">
				
				<!-- 账号输入框：左侧有人像图标 -->
				<view class="input-box">
					<u-icon name="account" size="44rpx" color="#999"></u-icon>
					<input 
						class="input-field" 
						v-model="account" 
						placeholder="请输入手机号/邮箱" 
						placeholder-class="input-placeholder"/>
				</view>

				<!-- 密码输入框：左侧有锁图标，右侧有"眼睛"图标可切换密码是否可见 -->
				<view class="input-box">
					<u-icon name="lock" size="44rpx" color="#999"></u-icon>
					<input 
						class="input-field" 
						:type="showPassword ? 'text' : 'password'"
						v-model="password" 
						placeholder="请输入密码" 
						placeholder-class="input-placeholder"/>
					<!-- 点击眼睛图标可以切换"显示密码 / 隐藏密码" -->
					<view class="eye-icon" @tap="togglePassword">
						<u-icon :name="showPassword ? 'eye-fill' : 'eye-off'" size="40rpx" color="#999"></u-icon>
					</view>
				</view>

				<!-- 登录按钮：点击后调用后端登录接口 -->
				<button class="btn-primary" hover-class="btn-hover" @tap="handleLogin" :loading="loading" :disabled="loading">
					<text class="btn-text">登 录</text>
				</button>

				<!-- 辅助链接：忘记密码 和 注册入口 -->
				<view class="helper-links">
					<text class="link-text" @tap="handleForgotPw">忘记密码？</text>
					<text class="link-text dark" @tap="navToRegister">没有账户？<text class="link-highlight">注册</text></text>
				</view>

			</view>

			<!-- 第三方登录区域：QQ 和微信（位于底部） -->
			<view class="third-party-wrapper">
				<view class="oauth-btn" @tap="handleOauth('qq')">
					<!-- QQ 登录图标 -->
					<u-icon name="qq-fill" color="#12B7F5" size="56rpx"></u-icon>
				</view>
				<view class="oauth-btn" @tap="handleOauth('wechat')">
					<!-- 微信登录图标 -->
					<u-icon name="weixin-fill" color="#09B83E" size="56rpx"></u-icon>
				</view>
			</view>

		</view>
	</view>
</template>

<script setup lang="ts">
/**
 * 登录页面的业务逻辑
 * - account: 用户输入的账号（手机号或邮箱）
 * - password: 用户输入的密码
 * - loading: 登录按钮是否处于"加载中"状态（防止重复点击）
 * - showPassword: 密码是否以明文显示
 */
import { ref } from 'vue'
import { loginAPI } from '@/api/auth'
import logoUrl from '@/static/logo.png'

// ========== 响应式数据 ==========
const account = ref('')      // 账号
const password = ref('')     // 密码
const loading = ref(false)   // 是否正在登录
const showPassword = ref(false) // 是否显示密码明文

/**
 * 切换密码可见性：点击眼睛图标时调用
 */
const togglePassword = () => {
	showPassword.value = !showPassword.value
}

/**
 * 登录主函数：验证输入 → 调用后端接口 → 存储 Token → 跳转到岗位浏览页
 */
const handleLogin = async () => {
	// 基本校验：账号和密码都不能为空
	if (!account.value || !password.value) {
		uni.showToast({ title: '请输入账号和密码', icon: 'none' })
		return
	}
	
	loading.value = true
	try {
		// 调用后端 /auth/login 接口
		const res = await loginAPI({
			account: account.value,
			password: password.value
		})
		
		// 登录成功：将 Token 和用户基本信息保存到手机本地存储
		uni.setStorageSync('token', res.token)
		uni.setStorageSync('userInfo', JSON.stringify({
			userId: res.userId,
			nickname: res.nickname
		}))
		
		// 弹出"登录成功"提示
		uni.showToast({ title: '登录成功', icon: 'success' })
		
		// 延迟 1 秒后跳转到岗位浏览页（带底部导航栏的主页）
		setTimeout(() => {
			uni.switchTab({
				url: '/pages/job/index'
			})
		}, 1000)
		
	} catch (e: any) {
		// 登录失败：展示后端返回的提示（如「账号或密码错误」）或网络错误
		uni.showToast({
			title: e?.message || e?.info || '登录失败，请稍后重试',
			icon: 'none'
		})
	} finally {
		// 无论成功失败，都关闭按钮的"加载中"状态
		loading.value = false
	}
}

/** 跳转到注册页面 */
const navToRegister = () => {
	uni.navigateTo({ url: '/pages/register/register' })
}

/** 忘记密码（尚未实现） */
const handleForgotPw = () => {
	console.log('忘记密码')
}

/** 第三方登录（尚未实现） */
const handleOauth = (provider: string) => {
	console.log('第三方登录:', provider)
}
</script>

<style lang="scss" scoped>
/* ================== 页面整体容器 ================== */
.container {
	width: 100vw;
	height: 100vh;
	display: flex;
	flex-direction: column;
	
	overflow: hidden;
}

/* ================== 顶部绿色渐变背景区域 ================== */
.top-bg {
	position: relative;
	width: 100%;
	height: 38vh;                 /* 占屏幕高度的 38% */
	background-color: transparent; /* 依赖底层的渐变色 */
	overflow: hidden;
}
/* 页面大标题 */
.top-title {
	position: absolute;
	top: 42px;
	left: 17px;
	font-family: 'FZLanTingHei-H-GBK', 'PingFang SC', 'Microsoft YaHei', sans-serif;
	font-size: 32px;
	font-weight: 900;
	color: #2d3436;
	letter-spacing: 2rpx;
	-webkit-text-stroke: 1px #2d3436; /* 描边加粗 */
	z-index: 10;
}
/* 弥散模糊圆：装饰用，营造柔和光感 */
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
	flex: 1;
	background-color: #ffffff;
	border-top-left-radius: 60rpx;     /* 左上圆角 */
	border-top-right-radius: 60rpx;    /* 右上圆角 */
	margin-top: -80rpx;                /* 向上位移，与背景区产生重叠效果 */
	z-index: 20;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 0 60rpx;
	box-shadow: 0 -10rpx 30rpx rgba(0,0,0,0.02);
}

/* 悬浮 Logo：半嵌入背景与卡片的交界处 */
.avatar-box {
	position: absolute;
	top: -180rpx;
	left: 50rpx;
	width: 250rpx;
	height: 250rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}
.logo-img {
	width: 100%;
	height: 100%;
	mix-blend-mode: multiply; /* 让白色背景透明，只保留主体的颜色 */
}
.avatar-text {
	font-weight: 900;
	font-size: 28rpx;
	color: #000;
	letter-spacing: 1rpx;
}

/* ================== 表单区域 ================== */
.form-wrapper {
	width: 100%;
	margin-top: 180rpx;   /* 在头像下方留出空间 */
	display: flex;
	flex-direction: column;
	gap: 36rpx;           /* 每个输入框之间的间距 */
}

/* 输入框外壳：圆角药丸形 + 淡绿色边框 */
.input-box {
	width: 100%;
	height: 100rpx;
	border-radius: 50rpx;
	border: 2rpx solid #D6E4DB;  /* 淡绿色边框 */
	padding: 0 40rpx;
	box-sizing: border-box;
	display: flex;
	align-items: center;
}

/* 覆盖 uView 组件自带的边线样式 */
:deep(.u-input__content) {
	border: none !important;
}
:deep(.u-line) {
	display: none !important;
}

/* 输入框本体：占据剩余宽度 */
.input-field {
	flex: 1;
	height: 100%;
	font-size: 32rpx;
	padding-left: 10rpx;
	border: none;
	outline: none;
	background: transparent;
}

/* 眼睛图标点击区域：增大可点击范围 */
.eye-icon {
	padding: 10rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

/* ================== 登录按钮 ================== */
.btn-primary {
	width: 100%;
	height: 100rpx;
	border-radius: 50rpx;
	background-color: #81B898;
	margin-top: 10rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	border: none;
	box-shadow: 0 8rpx 20rpx rgba(129, 184, 152, 0.3);
}
/* 去除 Uni-app 按钮的默认边框 */
.btn-primary::after { border: none; }
/* 按下时的视觉反馈 */
.btn-hover {
	background-color: #73aa8b;
	transform: scale(0.98);
}
.btn-text {
	color: #ffffff;
	font-size: 36rpx;
	font-weight: bold;
	letter-spacing: 24rpx;
	text-indent: 24rpx;  /* 配合 letter-spacing 使文字视觉居中 */
}

/* ================== 辅助链接 ================== */
.helper-links {
	width: 100%;
	display: flex;
	justify-content: space-between;
	margin-top: 10rpx;
	padding: 0 20rpx;
}
.link-text {
	font-size: 28rpx;
	color: #81B898;     /* 默认绿色（忘记密码） */
}
.link-text.dark {
	color: #666666;     /* 灰色（注册提示文字） */
}
.link-highlight {
	color: #81B898;     /* "注册"两字高亮为绿色 */
}

/* ================== 第三方登录区域 ================== */
.third-party-wrapper {
	position: absolute;
	bottom: 100rpx;
	left: 0;
	width: 100%;
	display: flex;
	justify-content: center;
	gap: 60rpx;
}
/* 第三方登录按钮：椭圆药丸形 */
.oauth-btn {
	width: 120rpx;
	height: 70rpx;
	border-radius: 35rpx;
	border: 2rpx solid #D6E4DB;
	display: flex;
	align-items: center;
	justify-content: center;
	box-sizing: border-box;
}
</style>
