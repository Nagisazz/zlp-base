/** 用于保存当前用户登录状态 */
import {ActionContext} from 'vuex';
import {AuthorizedUser} from '@/domain/security';

interface State {
  /** 当前登录用户的信息 */
  authorizedUser: AuthorizedUser|null;
}
const ssKey = 'security.authorizedUser';
const userCache = sessionStorage.getItem(ssKey);
const Security = {
  namespaced: true,
  state() {
    return {
      authorizedUser: userCache === null ? null : JSON.parse(userCache),
      showSign: false, // 是否显示登陆注册弹框
      signType: 'in', // 登陆  up注册
    }
  },

  mutations: {
    LOGIN(state: State , user: AuthorizedUser) {
      state.authorizedUser = user;
      sessionStorage.setItem(ssKey, JSON.stringify(user))
    },
    REFRESHTOKEN(state: State , tokenInfo: AuthorizedUser) { // 更新token，refreshToken
      if (state.authorizedUser) {
        state.authorizedUser.token = tokenInfo.token;
        state.authorizedUser.refreshToken = tokenInfo.refreshToken;
        sessionStorage.setItem(ssKey, JSON.stringify(state.authorizedUser));
      }
    },
    SHOWSIGN(state: any , val: any) { // 控制全局登陆，注册弹框的开关
      state.showSign = val.showSign ? val.showSign : false;
      state.signType = val.signType ? val.signType : 'in';
    },
    LOGOUT(state: State) {
      state.authorizedUser = null;
      sessionStorage.removeItem(ssKey)
    },
    RESET(state: State , user: AuthorizedUser) {
        state.authorizedUser = user;
        sessionStorage.setItem(ssKey, JSON.stringify(user))
    },
  },
 
  actions: {
    login(context: ActionContext<State, any> , user: AuthorizedUser) {
      context.commit('LOGIN' , user);
    },
    refreshToken(context: ActionContext<State, any> , tokenInfo: AuthorizedUser) {
      context.commit('REFRESHTOKEN' , tokenInfo);
    },
    showSign(context: ActionContext<State, any>) {
      context.commit('SHOWSIGN');
    },
    logout(context: ActionContext<State, any>) {
      context.commit('LOGOUT');
    },
    reset(context: ActionContext<State, any> , user: AuthorizedUser) {
      context.commit('RESET',user);
    },
  },
}

export default Security;

