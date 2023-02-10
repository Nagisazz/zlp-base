<template>
    <div class="layout">
      <keep-alive>
        <Header v-if="store.state.IsShowHead.isShowHead"></Header>
      </keep-alive>

      <!-- zp博客时的返回首页 -->
      <div class="sign-btn" v-if="!store.state.IsShowHead.isShowHead && !this.$route.name" @click="toHome()">To Home</div>

      <div class="container" :style="store.state.IsShowHead.containerH">
        <div v-show="!$route.name" id="micro"></div>
        
        <div v-show="$route.name" class="other">
          <router-view />
        </div>
      </div>

      
    </div>
</template>

<script>
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import Header from '@/components/Header';

export default {
  name: 'Layout',
  components: { Header },

  setup() {
    const router = useRouter()
    const onChangeRoute = (route) => {
        router.replace(route);
    }

    const store  = useStore();
    const saveAuthorizedUser = (res) => {
      store.commit("Security/LOGIN", res);
    }

    const openSignModal = (res) => {
      store.commit("Security/SHOWSIGN", res);
    }

    return {
      onChangeRoute,
      saveAuthorizedUser,
      openSignModal,
      store,
    }
  },

  data() {
    return {
    }
  },

  created() {
    
  },

  mounted() {
    
  },

  methods: {
    toHome() {
      this.onChangeRoute('/');
      // setTimeout(() => {
      //   window.location.reload();
      // }, 100);
    },
  }

  

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.layout{
  width: 100%;
  height: 100vh;
  padding: 0;
  margin: 0;
}

.container{
  width: 100%;
  height: calc(100% - 60px)
}

#micro, .other{
  height: 100%;
}

.sign-btn{
  display: inline-block;
  padding: 6px 15px;
  box-sizing: border-box;
  color: #fff;
  border: 1px solid #fff;
  border-radius: 8px;
  margin: 0 10px;
  font-weight: bold;
  position: fixed;
  right: 30px;
  top: 20px;
  z-index: 999;
  cursor: pointer;
}
</style>


