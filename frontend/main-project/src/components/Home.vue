<template>
    <div class="home">
      <div class="applition">
        <div class="head">
          <keep-alive>
            <Header :from="'home'"></Header>
          </keep-alive>
        </div>
        
        <div class="title" ref="title"></div>
        <div class="menus">
          <div class="menu-item" @click="onChangeRoute('/platform/zp')">
            <div class="menu" >
              <img src="http://1.15.87.105:11000/love/base/zp.png" alt="" />
            </div>
            <div class="app-name">朱陈博客</div> 
          </div>
          <div class="menu-item" @click="onChangeRoute('/platform/price')">
            <div class="menu" >
              <img src="http://1.15.87.105:11000/love/base/price.png" alt="" />
            </div>
            <div class="app-name">商品库价格计算</div> 
          </div>
          <div class="menu-item"  @click="onChangeRoute('/platform/file')">
            <div class="menu">
              <img src="http://1.15.87.105:11000/love/base/file.png" alt="" />
            </div>
            <div class="app-name">享到-跨端文件共享及协作</div> 
          </div>
          <div class="menu-item" @click="onChangeRoute('/platform/fund')">
            <div class="menu" >
              <img src="http://1.15.87.105:11000/love/base/fund.png" alt="" />
            </div>
            <div class="app-name">基金投资辅助决策</div> 
          </div>
        </div>
      </div>
    </div>
</template>
  
<script>
import { useRouter } from 'vue-router';
import Header from '@/components/Header';

export default {
    name: 'Home',
    components: { Header },

    setup() {
      const router = useRouter()
      const onChangeRoute = (route) => {
        router.replace(route);
      }

      return {
        onChangeRoute,
      }
    },

    created() {

    },

    mounted() {
      const divNode = this.$refs.title;
      const codingStr = "Let me share with you a few useful apps";
      let j = 0;
      // 创建循环定时器，通过循环定时器每200毫米执行里面的回调函数
      const coding = setInterval(function(){
        // slice()方法是截取字符串指定位置的方法
        // 第一个参数是起始位置的索引，第二个参数是结束位置的索引（不包含结束位置）
        // 最后返回的就是截取的字串
        // 最后是模拟光标的效果
        divNode.innerHTML = codingStr.slice(0,j) + '_';
        j++;
        // 如果索引大于了字符串的长度，把最后的光标删除并清除计时器
        if(j > codingStr.length){
          divNode.innerHTML = codingStr.slice(0,j);
          clearInterval(coding);
        }
      }, 80)
    },
}
 </script>
  
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.home{
  width: 100%;
  height: 100%;
}
.head{
  padding: 5% 0;
  box-sizing: border-box;
}
.title{
  text-align: center;
  font-size: 50px;
  margin: 0 auto;
  word-break: break-all;
  color: #1E255E;
  font-weight: bold;
  padding: 5% 0;
  width: 60%;
  box-sizing: border-box;
}
.applition{
    width: 100%;
    height: 100%;
    background-image: url('http://1.15.87.105:11000/love/base/bg.png');
    background-size: 100% 100%;
}
.menus{
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-top: 100px;
}
.menu-item{
  width: 20%;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  cursor: pointer;
}
.menu{
  width: 100%;
  height: 80%;
  background-image: url('http://1.15.87.105:11000/love/base/screen.png');
  background-size: 100% 100%;
  padding: 8px;
  box-sizing: border-box;
  img{
    width: 100%;
    height: 82%;
    border-radius: 10px;
    margin: 0 auto;
  }
}
.app-name{
  width: 80%;
  height: 40px;
  background: #4BF2CC;
  border-radius: 10px;
  text-align: center;
  color: #1E255E;
  line-height: 40px;
  font-size: 18px;
  font-weight: bold;
  margin-top: 40px;
}
.menu:hover{
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}

@media (max-height: 1000px)  { // 小于1000的设备
  .head, .title{
    padding: 40px 0;
    box-sizing: border-box;
  }
}
@media (max-width: 1600px)  {
  .menu-item{
    height: 340px;
  }
}
@media (max-width: 1410px)  {
  .menu-item{
    height: 300px;
  }
  .menus{
    margin-top: 0;
  }
}
</style>
  