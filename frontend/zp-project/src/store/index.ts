import Vue from 'vue'
import Vuex from 'vuex'

import PlatformData from './modules/PlatformData'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    PlatformData,
  },
  strict: process.env.NODE_ENV !== 'production',
})
