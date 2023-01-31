<template>
    <div class="home">
      <div class="applition">
        <div class="menu" @click="onChangeRoute('/platform/zp')">朱陈博客</div>
        <div class="menu" @click="onChangeRoute('/platform/price')">商品库价格计算</div>
        <div class="menu" @click="onChangeRoute('/platform/file/')">享到-跨端文件共享及协作</div>
        <div class="menu" @click="onChangeRoute('/platform/fund')">基金投资辅助决策</div>
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
  height: 100%;
}
.applition{
    width: 100%;
    height: 100%;
    display: flex;
}
  
.menu{
  width: 20%;
  height: 24%;
  background: #434e58;
  border-radius: 20px;
  margin: 0 2.5%;
  color: #fff;
  margin-top: 200px;
  display: flex;
  align-items: center;
  font-size: 18px;
  justify-content: center;
  cursor: pointer;
}
.menu:hover{
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
  