import 'zone.js';
import { createApp } from 'vue'
import App from './App.vue'

import router from '@/router/index'
import store from '@/store/index'

import '@/qiankun/config';
import '@/assets/iconfont/iconfont.css';
import '@/assets/iconfont/iconfont.js';

const app = createApp(App);

app.use(router);
app.use(store);

app.mount('#app')








