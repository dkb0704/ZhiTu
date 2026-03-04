import { createSSRApp } from "vue";
import App from "./App.vue";
import uviewPlus from 'uview-plus';
import { createPinia } from 'pinia';

export function createApp() {
  const app = createSSRApp(App);
  const pinia = createPinia();

  app.use(pinia);
  app.use(uviewPlus);

  // uview-plus uni.$u.config.unit = 'rpx' setup if needed later

  return {
    app,
    Pinia: pinia // returning Pinia for SSR compatibility setup if needed
  };
}
