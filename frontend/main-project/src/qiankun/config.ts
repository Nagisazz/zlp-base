import { registerMicroApps, initGlobalState, start } from 'qiankun';
import router from '@/router/index';
import store from '@/store/index';



const state = {
  showSign: false,
  token: store.state.Security.authorizedUser ? store.state.Security.authorizedUser.token : '',
  refreshToken: store.state.Security.authorizedUser ? store.state.Security.authorizedUser.refreshToken : ''
} 
const actions = initGlobalState(state);

// 监听全局状态，子应用更新主应用数据后触发
actions.onGlobalStateChange((state, prevState) => {
  console.log("主应用变更前：", prevState);
  console.log("主应用变更后：", state);
  if (state.showSign) {
    store.commit("Security/SHOWSIGN", { showSign: true, signType: 'in' });
  }
  if (store.state.Security.authorizedUser && store.state.Security.authorizedUser.token !== state.token) {
    store.commit("Security/LOGIN", state);
  }
})

actions.setGlobalState(state);

registerMicroApps([
  {
    name: 'zp', // 子应用package.json中的name
    entry: 'http://temp.zlpnet.cn:1000/zp/',
    container: '#micro', // 插到主应用的div容器
    activeRule: '/platform/zp', // 和子应用设置的路由history--platform属性一致
    props: { actions, router }, // 传给微应用的数据
  },
  {
    name: 'price',
    entry: 'http://temp.zlpnet.cn:1000/price/',
    container: '#micro',
    activeRule: '/platform/price',
    props: { actions }, // 传给微应用的数据
  },
  {
    name: 'file',
    entry: 'http://localhost:7003/file/',
    // entry: 'http://localhost:8091/file/',
    // entry: 'http://temp.zlpnet.cn:1000/file/',
    container: '#micro',
    activeRule: '/platform/file',
    props: { actions }, // 传给微应用的数据
  },
  {
      name: 'fund',
      entry: 'http://temp.zlpnet.cn:1000/fund/',
      container: '#micro',
      activeRule: '/platform/fund',
      props: { actions }, // 传给微应用的数据
  },
]);

// start();
