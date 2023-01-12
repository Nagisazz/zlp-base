<template>
    <router-view/>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  components: {
  },
})
export default class App extends Vue {

}
</script>

<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
body{
  margin: 0;
  padding: 0;
}
#app{
  width: 100vw;
  height: 100%;
  // background: url('http://1.15.87.105:11000/love/zp/0.jpg') no-repeat;
  // overflow: hidden;
}

</style>
