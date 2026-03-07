<style lang="scss">
	/*每个页面公共css */
	@import "uview-plus/index.scss";
	
	page {
		/* background-color: #F7FBFA; */
		background-image: url("@/static/back.png");
		background-size: cover;
		background-color: #F7FBFA;
		background-position: center;
		background-repeat: no-repeat;
		font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei', sans-serif;
	}
</style>
<script>
export default {
  onLaunch: function () {
    console.log("App Launch");
  },
  onShow: function () {
    console.log("App Show");
  },
  onHide: function () {
    console.log("App Hide");
  },
};
</script>
