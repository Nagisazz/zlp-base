import '@/App.css';
import Router from '@/router/index';
import { BrowserRouter } from "react-router-dom";

function App() {
  // web 应用程序中一般使用 BrowserRouter 组件, 还用另一种 HashRouter 组件方式;
  // 这两种方式的区别:

  // 底层原理不一样：
  // BrowserRouter调用的是 H5 history API,低版本兼容性问题;
  // HashRouter 使用的是 URL 哈希值;
  // 地址栏表现形式不一样：
  // BrowserRouter的路径：localhost:3000/index
  // HashRouter的路径：localhost:3000/#/index
  // 刷新后对路由 state 参数的影响
  // BrowserRouter没有任何影响，因为 state 保存在 history 对象中
  // HashRouter刷新后会导致路由 state 参数的丢失
  // 值得注意的是，官方强烈建议不要使用 HashRouter;
  /* React.StrictMode
      1、识别不安全的生命周期
      2、关于使用过时字符串 ref API 的警告
      3、关于使用废弃的 findDOMNode 方法的警告
      4、检测意外的副作用
      5、检测过时的 context API */
    /* <React.StrictMode> 注释StrictMode的话useEffect就不会调用两次了 */
  return <div> 
    <BrowserRouter basename={(window as any).__POWERED_BY_QIANKUN__ ? '/platform/price' : '/'}>
        <Router/> 
    </BrowserRouter>
  </div>;
  
}

export default App;
