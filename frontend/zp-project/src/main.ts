import './public-path';
import Vue from 'vue';
import VueRouter from 'vue-router';
import App from './App.vue';
import routes from './router';
import echarts from 'echarts';
import ElementUI from 'element-ui'; //element-ui的全部组件

import 'element-ui/lib/theme-chalk/index.css';// element-ui的css
import './assets/iconfont/iconfont.css'; // 图标库
import './style/zp.less'; // 公共样式文件
import './style/index.less'; // 组价复写样式文件
import 'fullpage.js/vendors/scrolloverflow'; // 全屏翻页插件

import store from '@/store/index';


Vue.config.productionTip = false;
Vue.use(ElementUI);
// Vue.use(echarts);

let router: any = null;
let instance: any = null;
function render(props?: any) {
  if (props) {
    const { container, mainAppRouter } = props;
    // Vue.prototype.parentRouter = mainAppRouter;

    console.log(props, '主应用数据');
    
    props.onGlobalStateChange((state: any, prevState: any) => {
      console.log("通信状态发生改变：", state, prevState);
      store.commit("PlatformData/SETTOKENINFO", state);
    });
  }
  

  router = new VueRouter({
    base: (window as any).__POWERED_BY_QIANKUN__ ? '/platform/zp/' : '/',
    mode: 'history',
    routes,
  });

  instance = new Vue({
    router,
    store,
    render: (h) => h(App),
  }).$mount('#zp'); // public--》index.html中的id

}

// 独立运行时
if (!(window as any).__POWERED_BY_QIANKUN__) {
  render();
}

export async function bootstrap() {
  console.log('[vue] vue app bootstraped');
}
export async function update(props: any) {
    console.log('update props', props);
}
export async function mount(props: any) {
  console.log('[vue] props from main framework', props);
  render(props);
}
export async function unmount() {
  instance.$destroy();
  instance.$el.innerHTML = '';
  instance = null;
  router = null;
}



