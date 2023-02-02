<template>
    <div class="layout">
      <Header v-if="store.state.IsShowHead.isShowHead"></Header>

      <div class="container" :style="containerH">
        <div v-show="!$route.name" id="micro"></div>
        <div v-show="$route.name" class="other">
          <router-view />
        </div>
      </div>

      
    </div>
</template>

<script>
import {useStore} from 'vuex';
import Header from '@/components/Header';

export default {
  name: 'Layout',
  components: { Header },

  setup() {

    const store  = useStore();
    const saveAuthorizedUser = (res) => {
      store.commit("Security/LOGIN", res);
    }

    const openSignModal = (res) => {
      store.commit("Security/SHOWSIGN", res);
    }

    return {
      saveAuthorizedUser,
      openSignModal,
      store,
    }
  },

  data() {
    return {
      containerH: 'height: calc(100vh - 60px)',
    }
  },

  created() {
    
  },

  mounted() {
    console.log(this.$route, this.store.state.IsShowHead.isShowHead);
    if (!this.store.state.IsShowHead.isShowHead) {
      this.containerH = 'height: 100vh';
    }
  },

  methods: {
    
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
</style>


