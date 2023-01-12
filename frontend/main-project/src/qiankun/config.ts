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

// onGlobalStateChange
actions.onGlobalStateChange((state, prevState) => {
  console.log("主应用变更前：", prevState);
  console.log("主应用变更后：", state);
  if (state.showSign) {
    store.commit("Security/SHOWSIGN", { showSign: true, signType: 'in' });
  }
})

actions.setGlobalState(state);

registerMicroApps([
  {
    name: 'zp', // 子应用package.json中的name
    entry: process.env.NODE_ENV === 'development' ? '//localhost:7001' : '/platform',
    container: '#micro', // 插到主应用的div容器
    activeRule: '/platform/zp', // 和子应用设置的路由history--platform属性一致
    props: { actions, mainAppRouter: router }, // 传给微应用的数据
  },
  {
      name: 'pricefile',
      entry: process.env.NODE_ENV === 'development' ? '//localhost:7002' : '/platform',
      container: '#micro',
      activeRule: '/platform/pricefile',
      props: { actions }, // 传给微应用的数据
  },
  // {
  //     name: 'angular-demo',
  //     entry: process.env.NODE_ENV === 'development' ? '//localhost:7003' : '/platform',
  //     container: '#micro',
  //     activeRule: '/platform/angular-demo',
  // },
  // {
  //     name: 'fund',
  //     entry: process.env.NODE_ENV === 'development' ? '//localhost:7004' : '/platform',
  //     container: '#micro',
  //     activeRule: '/platform/fund',
  // },
]);

// start();
