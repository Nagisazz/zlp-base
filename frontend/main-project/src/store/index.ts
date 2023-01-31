import { createStore } from 'vuex'
import Security from './module/Security';
import IsShowHead from './module/IsShowHead';


export default createStore({
  modules: {
    Security,
    IsShowHead
  }
})