/** 路由切换 */
import {ActionContext} from 'vuex';


const IsShowHead = {
  namespaced: true,
  state() {
    return {
        routerPath: '',
        isShowHead: true,
    }
  },

  mutations: {
    CHANGEROUTEPATH(state: any, path: String) {
        state.routerPath = path;
        console.log(path);
        if (path.indexOf('/zp') > -1 || path.indexOf('platform') === -1) { // base以及博客不需要头部
            state.isShowHead = false;
        } else {
            state.isShowHead = true;
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

