<template>
    <div class="Section01">
      <div class="info">
        <div class="photo"></div>
        <ul class="list">
          <li class="item" v-for="(item, index) in section01.infoList" :key="index" v-text="item"></li>
        </ul>
      </div>
      <div class="des">
        <p class="quote" v-text="section01.quote"></p>
        <div class="des-list">
          <div class="des-item" v-for="(item, idx) in section01.desList" :key="idx">
            <div> <i :class="['iconfont', item.icon]" ></i> </div>
            <div class="dec-txt">
              <div @click="onclip(idx)" :class="'copy'+idx" :data-clipboard-text="item.code">{{item.code}}</div>
              <img v-if="item.img && isPC()" :src="item.img" alt=""
                 :id="item.icon" class="wei" @click="onScaleImg(item.icon)" @touchstart="onScaleImg()">
            </div> 
          </div>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import ClipboardJS from 'clipboard';
import zz from '@/resume/zz';
import pp from '@/resume/pp';

@Component({
  name:'Section01',
})
export default class Section01 extends Vue {
  section01: any = zz;
  zzData: any = zz;
  ppData: any = pp;

  created() {
    if (this.$route.query.user && this.$route.query.user === 'pp') {
      this.section01 = this.ppData;
    }
    if (this.$route.params.user && this.$route.params.user === 'pp') {
      this.section01 = this.ppData;
    }
    console.log(this.$route.params, this.$route.query);
  }

  onScaleImg(code: any) {
    const wei = document.getElementById(code) as HTMLElement;
    console.log(wei.style);
    if (wei.style.transform === 'scale(10)') {
      wei.style.transform = 'scale(1)';
    } else {
      wei.style.transform = 'scale(10)';
    }
  }

  // 复制内容
  onclip(idx: any) {
    const _this = this
    const clipboard = new ClipboardJS('.copy' + idx);
    clipboard.on('success',(e: ClipboardJS.Event) => {
        e.clearSelection();
        // _this.$message({
        //     message:'已复制到粘贴板',
        //     type:'success'
        // })
    });
    clipboard.on('error', () => {
        // _this.$message({
        //     message:'复制错误，请重新复制！',
        //     type:'error'
        // })
    });
  }

  // 判断是否是PC端打开，移动端不显示导航
  isPC() {
      const u = navigator.userAgent;
      const Agents = ['Android', 'iPhone', 'webOS', 'BlackBerry', 'SymbianOS', 'Windows Phone', 'iPad', 'iPod'];
      let flag = true;
      for (let i = 0; i < Agents.length; i++) {
          if (u.indexOf(Agents[i]) > 0) {
          flag = false;
          break;
          }
      }
      return flag;
  }

}

</script>

<style scoped lang="less">
.Section01{
  position: absolute;
  top: 10vh;
  left: 5vw;
  right: 5vw;
}
.info{
  margin-top: 10vh;
  position: relative;

  .photo {
    width: 10rem;
    height: 10rem;
    border-radius: 50%;
    margin: 0 auto;
    background: url('../assets/img/tou.png') no-repeat center center;
    background-size: contain;
    transition: box-shadow 0.5s linear;
  }
  .list {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 3rem;
    height: 3rem;
    transform: translateX(-50%) translateY(-50%);

    .item {
      position: absolute;
      top: 0;
      left: 0;
      width: 3rem;
      height: 3rem;
      line-height: 3rem;
      border-radius: 50%;
      text-align: center;
      color: #fff;
      font-size: 14px;
      box-shadow: inset 0 0 1rem 5px rgba(255, 255, 255, 0.67);
      animation: pulse 2s infinite linear;

      @keyframes pulse {
        0% {
          transform: scaleX(1);
        }

        50% {
          transform: scale3d(1.1, 1.1, 1.1);
        }

        to {
          transform: scaleX(1);
        }
      }

      &:nth-child(1) {
        top: -190%;
        left: -170%;
      }

      &:nth-child(2) {
        top: 120%;
        left: -220%;
        width: 3rem;
        height: 3rem;
        line-height: 3rem;
      }

      &:nth-child(3) {
        top: -170%;
        left: 170%;
        width: 3.2rem;
        height: 3.2rem;
        line-height: 3.2rem;
      }

      &:nth-child(4) {
        top: 100%;
        left: 210%;
        width: 3.1rem;
        height: 3.1rem;
        line-height: 3.1rem;
      }
    }
  }
}

.des {
  text-align: center;
  margin: 0 auto;
  color: #fff;

  .des-list {
    padding-top: 20px;
    font-size: 18px;
    width: 100%;
    white-space: wrap;
    display: flex;
    flex-wrap: wrap;
    .dec-txt{
      cursor: pointer;
    }

    .iconfont{
      font-size: 36px;
      display: inline-block;
      margin-bottom: 20px;
    }
    .wei{
      width: 20px;
      height: 20px;
      vertical-align: middle;
      z-index: 999;
    }
  }

  .quote {
    line-height: 50px;
    position: relative;
    padding: 20px 0;
    font-size: 22px;

    &::after {
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
      height: 1px;
      background-color: rgba(255, 255, 255, 0.6);
    }
  }
}
@media (max-width: 700px)  { // 小于700的设备
  .des-item{
    width: 50%;
    margin-bottom: 10px;
  }
}
@media (min-width: 700px) { //>700的设备 
    .des-item{
      width: 25%;
    }
}
</style>