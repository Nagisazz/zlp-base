<template>
  <!-- <el-config-provider namespace="elplus"> -->
      <router-view></router-view>
  <!-- </el-config-provider> -->
</template>

<script>
import {useStore} from 'vuex';

export default {
  name: 'APP',

  setup(){
    const store = useStore();
    return {
        store,
    }
  },


}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-size: 14px;
}
body{
  padding: 0;
  margin: 0;
}

</style>
