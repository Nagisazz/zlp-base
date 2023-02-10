import './public-path';
import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';
import '@/index.css';
import 'antd/dist/antd.css';
import '@/statics/iconfont/iconfont.css';
import '@/statics/iconfont/iconfont.js';
import '@/style/zp.less';
import { Provider, connect } from 'react-redux';
import store from './redux/store';
import { setTokenInfo } from '@/redux/actions/platformData';
import actions from '@/qiankun/action';

function render(props) {
  const { container } = props;
  console.log(props, actions, '主应用数据');

  const dom = container ? container.querySelector('#file') : document.querySelector('#file')
  const root = createRoot(dom);
  root.render(
    <Provider store={store}>
      <App/>
    </Provider>
  );
}

if (!window.__POWERED_BY_QIANKUN__) {
  render({});
}

export async function bootstrap() {
  console.log('[react18] react app bootstraped');
}

export async function mount(props) {
  console.log('[react18] props from main framework ppppppppppppp');
  if (props) { // 不能注释，否则子应用接收不到主应用数据
    actions.setActions(props)
  }
  sessionStorage.setItem('haveInit', 'N');
  actions.onGlobalStateChange((state) => {
      console.log("我是子应用，我检测到数据了：", state);
      const ssKey = 'platform.login';
      const infoCache = JSON.parse(sessionStorage.getItem(ssKey));
      if (!infoCache) { // 阻止一开始未登录进入时会出现两次登录弹框和刷新
        setTokenInfo(state);
      }
      setTimeout(() => {
        if (infoCache && (infoCache.token !== state.token)) {
          setTokenInfo(state);
          window.location.reload();
        }
      }, 500)
  }, true);
  render(props);
}

export async function unmount(props) {
  console.log('unmountunmountunmountunmountunmount'); 
  const { container } = props;
  const dom = container ? container.querySelector('#file') : document.querySelector('#file');
  createRoot(dom).unmount();
  sessionStorage.removeItem('haveInit');
  // console.log(dom);
  // createRoot.unmount(dom);
}