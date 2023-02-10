import 'zone.js';
import { createApp } from 'vue'
import App from './App.vue'

import router from '@/router/index'
import store from '@/store/index'

import '@/qiankun/config';
import '@/assets/iconfont/iconfont.css';
import '@/assets/iconfont/iconfont.js';
import '@/styles/zp.scss';
import message from "@/components/function/message";
import messageBox from "@/components/function/messageBox";


export const app = createApp(App);

app.use(router);
app.use(store);
app.use(message);
app.use(messageBox);

app.mount('#app')








