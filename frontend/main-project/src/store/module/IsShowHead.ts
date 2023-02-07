/** 路由切换 */
import {ActionContext} from 'vuex';


const IsShowHead = {
  namespaced: true,
  state() {
    return {
        routerPath: '',
        isShowHead: true,
        containerH: 'height: calc(100vh - 60px)'
    }
  },

  mutations: {
    CHANGEROUTEPATH(state: any, path: String) {
        state.routerPath = path;
        console.log(path, '浏览器地址');
        if (path.indexOf('/zp') > -1 || path.indexOf('platform') === -1) { // base以及博客不需要头部
            state.isShowHead = false;
            state.containerH = 'height: 100vh';
        } else {
            state.isShowHead = true;
            state.containerH = 'height: calc(100vh - 60px)';
        }
    },
  },
 
  actions: {
    changeRoutePath(context: ActionContext<'', any> , path: String) {
      context.commit('CHANGEROUTEPATH' , path);
    },
  },
}

export default IsShowHead;

