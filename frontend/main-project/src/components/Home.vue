<template>
    <div class="home">
      <div class="applition">
        <div class="menu" @click="onChangeRoute('/platform/zp')">子应用1</div>
        <div class="menu" @click="onChangeRoute('/platform/pricefile/price')">子应用2--猜价</div>
        <div class="menu" @click="onChangeRoute('/platform/pricefile/file')">子应用2--共享文件</div>
        <div class="menu" @click="onChangeRoute('/platform/angular-demo')">子应用3</div>
        <div class="menu" @click="onChangeRoute('/platform/fund')">子应用4</div>
      </div>
    </div>
</template>
  
<script>
import { useRouter } from 'vue-router';

export default {
    name: 'Home',
    components: {},

    setup() {
      const router = useRouter()
      const onChangeRoute = (route) => {
        router.replace(route);
      }

      return {
        onChangeRoute,
      }
    },
}
 </script>
  
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.home{
  width: 100%;
  
}
.applition{
    width: 100%;
    display: flex;
}
  
.menu{
  width: 20%;
  height: 80px;
  text-align: center;
  line-height: 80px;
  background: #2c3e50;
  margin: 0 2.5%;
  color: #fff;
  margin-top: 100px;
}
</style>
  