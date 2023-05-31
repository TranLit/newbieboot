import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/normalize.css';
import './assets/global.css';
import request from "./utils/request";

Vue.config.productionTip = false

// size: "mini" ; "small"
Vue.use(ElementUI)

Vue.prototype.request = request

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
