import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'

import router from '@/router/index'
import store from '@/store/index'
import 'zone.js/dist/zone';
import '@/qiankun/config';
import '@/assets/iconfont/iconfont.css';
import '@/assets/iconfont/iconfont.js';

const app = createApp(App);

app.use(ElementPlus);
app.use(router);
app.use(store);

app.mount('#app')








