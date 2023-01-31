/** 用于保存当前用户登录状态 */
import {ActionContext} from 'vuex';
interface Info {
    /** 当前登录用户的token信息 */
    token: string | '';
    refreshToken: string | '';
};
const ssKey = 'platform.login';
const infoCache = sessionStorage.getItem(ssKey);
const state: any = {
    loginInfo: infoCache === null ? null : JSON.parse(infoCache),
}

const mutations = {
  SETTOKENINFO(state: any , loginInfo: Info) {
    console.log(loginInfo, 'loginInfo');
    state.loginInfo = loginInfo;
    sessionStorage.setItem(ssKey, JSON.stringify(loginInfo))
  }
};

const actions = {
  setTokenInfo(context: ActionContext<Info, any> , loginInfo: Info) {
    context.commit('SETTOKENINFO' , loginInfo);
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
