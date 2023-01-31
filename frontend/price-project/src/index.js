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
import actions from '@/qiankun/action';

function render(props) {
  const { container } = props;
  console.log(props, actions, '主应用数据');

  const dom = container ? container.querySelector('#price') : document.querySelector('#price')
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
  actions.onGlobalStateChange((state) => {
      console.log("我是子应用，我检测到数据了：", state);
      const ssKey = 'platform.login';
      const infoCache = JSON.parse(sessionStorage.getItem(ssKey));
      setTimeout(() => {
        if (!infoCache || (infoCache.token !== state.token)) {
          setTokenInfo(state);
          window.location.reload();
        }
      }, 1000)
  }, true);
  render(props);
}

export async function unmount(props) {
  const { container } = props;
  createRoot(container ? container.querySelector('#price') : document.querySelector('#price')).unmount();
}