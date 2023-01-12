<template>
    <div class="Section04">
      <div class="zp-resume-title">作品集</div>
      <div class="zp-resume-content">
        <ul class="work-list">
          <li class="work-list-item" v-for="(item, index) in section04.worksList" :key="index">
            <div class="title">
              <a class="link" v-text="item.title" :href="item.url" target="_blank"></a>
            </div>
            <p class="des"  v-text="item.des"></p>
            <!-- <a v-href="item.url" target="_blank">
              <div class="img">tt</div>
              <img v-src="item.img.src" class="img" v-alt="item.img.alt" />
            </a> -->
            <div class="work-link" v-if="item.url">
              <a class="iconfont zp-lianjie img" :href="item.url" target="_blank"></a>
            </div>
          </li>
        </ul>
        <div class="more">
          <a :href="section04.workUrl[0]" target="_blank" v-text="'Check out more on My GitHub'"></a>
        </div>
        <div class="switch" @click="switchWork($event)" @touchstart="switchWork($event)">
          <i class="left" v-style="workIndex===0?{filter:'brightness(70%)'}:{}"></i>
          <i class="right" v-style="workIndex===section04.worksList.length-1?{filter:'brightness(70%)'}:{}"></i>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import zz from '@/resume/zz';
import pp from '@/resume/pp';

@Component({
  name:'Section04',
})
export default class Section04 extends Vue {
  section04: any = zz;
  zzData: any = zz;
  ppData: any = pp;

  public workIndex: number = 0;

  created() {
    if (this.$route.query.user && this.$route.query.user === 'pp') {
      this.section04 = this.ppData;
    }
  }

  mounted() {
    this.setWorkDraw();
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

  setWorkDraw() {
    const workDom = document.getElementsByClassName('work-list')[0] as HTMLElement;
    const children = workDom.children;
    const distance = this.isPC() ? 3 : -5;
    for (let i = 0; i < children.length; i++) {
      const child = children[i] as HTMLElement;
      child.style.transform = `translateX(${1.5 * i + distance}rem) translateZ(${-1.5 * i}rem) scale(${1 - i * 0.05}, ${1 - i * 0.05})`;
    }
  }

  switchWork($event: any) {
      const workDom = document.getElementsByClassName('work-list')[0] as HTMLElement;
      const children = workDom.children;
      const tag = $event.target.className;
      const distance = this.isPC() ? 3 : -5;
      if (tag === 'left' && this.workIndex !== 0) {
        --this.workIndex;
      } else if (tag === 'right' && this.workIndex !== children.length - 1) {
        ++this.workIndex;
        const ele = children[this.workIndex - 1] as HTMLElement;
        ele.style.transform = `translateX(-19rem)`;
      }
      for (let i = this.workIndex; i < children.length; i++) {
        const eleChild = children[i] as HTMLElement;
        eleChild.style.transform = `translateX(${1.5 * (i - this.workIndex) + distance}rem) translateZ(${-1.5 * (i - this.workIndex)}rem) scale(${1 - (i - this.workIndex) * 0.05}, ${1 -
          (i - this.workIndex) * 0.05})`;
      }
  }
}

</script>

<style scoped lang="less">
.zp-resume-content{
  flex-direction: column;
}
.work-list {
  transform-style: preserve-3d;
  padding: 0;
  height: 60vh;
  transition: transform 2s;
  display: flex;
  justify-content: space-between;
  margin: 0 auto;
  color: #000;

  &-item {
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
    box-sizing: border-box;
    border-radius: 12px;
    padding: 20px;
    height: 60vh;
    min-width: 40vh;
    position: absolute;
    text-align: justify;
    transition: transform 0.5s;
    background: rgba(255, 255, 255, 0.98);

    .title {
      margin: 50px 0;
      text-align: center;

      .link {
        color: #4985a0;
      }
    }

    .des {
      line-height: 40px;
      font-size: 16px;
      text-align: left;
    }

    .qrcode {
      display: flex;
      flex-direction: column;
      align-items: center;

      .img {
        width: 200px;
        height: 200px;
      }
    }

    .work-link {
      display: flex;
      flex-direction: column;
      align-items: center;
      border-radius: 4px;
      cursor: pointer;
      width: 300px;
      margin: 20px auto 0;

      .img {
        width: 300px;
        height: 80px;
        border: 1px solid #999;
        background-size: 50% 50%;
      }

      .zp-lianjie{
        color: #999;
        font-size: 30px;
        text-align: center;
        line-height: 80px;
      }
    }
  }
}
.more {
  text-align: center;
  padding-top: 50px;

  a {
    text-decoration: underline;
  }
}

.switch {
  padding: 30px 0;
  width: 90%;
  margin: 0 auto;
  height: 40px;
  z-index: 1;
  display: flex;
  justify-content: space-between;

  .arrow {
    border-radius: 50%;
    border: 1px solid #fff;
    height: 40px;
    width: 40px;
    position: relative;
    cursor: pointer;
    background: url(../assets/img/arrow.svg) center 35% no-repeat;
    background-size: 70%;
    transform: rotate(90deg);
  }

  .left {
    .arrow;

    transform: rotate(-90deg);
  }

  .right {
    .arrow;
  }
}

@media (max-width: 700px)  { // 小于700的设备
  .work-list{
    width: 60%;
  }
}
@media (min-width: 700px) { //>700的设备 
    .work-list{
      width: 600px;
    }
}

</style>