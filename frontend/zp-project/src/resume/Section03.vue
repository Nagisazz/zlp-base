<template>
    <div class="Section03">
      <div class="zp-resume-title">经历</div>
      <div class="zp-resume-content">
        <div class="slider" style="transform: rotateY(0deg) rotateX(0deg);">
          <div class="slider-container" >
            <div class="left">
              <h4 v-text="expCur.title"></h4>
              <p class="post">"<span v-text="expCur.post"></span>"</p>
            </div>
            <div class="right">
              <div class="time" v-text="expCur.time"></div>
              <ul class="list">
                <li class="exp-item" v-for="(item, idx) in expCur.contentList" :key="idx">
                  <div class="exp">{{item}}</div> 
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="shout-cut">
          <ul>
            <li v-for="(item, index) in section03.expList" :key="index" :class="[{'-selected': index == selectExpIndex}]"
              @click="switchExp($event,index)" @touchstart="switchExp($event,index)"></li>
          </ul>
        </div>
      </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import zz from '@/resume/zz';
import pp from '@/resume/pp';

@Component({
  name:'Section03',
})
export default class Section03 extends Vue {
  section03: any = zz;
  zzData: any = zz;
  ppData: any = pp;

  public expDom: any;
  public contentDom: any;
  public sliderDom: any;
  public selectExpIndex: number = 0; // 当前经历index
  public expCur: any = {}; // 当前显示经历
  
  created() {
    if (this.$route.query.user && this.$route.query.user === 'pp') {
      this.section03 = this.ppData;
      this.expCur = this.section03.expList[0];
    }
  }

  mounted() {
      this.setExpTouch3D();
  }

  public setExpTouch3D() {
    this.expDom = document.getElementsByClassName('slider')[0];
    this.contentDom = this.expDom.parentNode;
    this.expDom.addEventListener('mousemove', this.expMousemoveHandler);
    this.expDom.addEventListener('mouseout', this.expMouseoutHandler);
  }

    /**
     * 节流函数
     *
     * @export
     * @param {Function} fn
     * @param {Number} time
     * @return {Function}
     */
  // public throttleGenerator(fn: any, time: any) {
  //   let date = new Date().getTime();
  //   return (e) => {
  //     const nowDate = new Date().getTime();
  //     if ((nowDate - date) > time) {
  //       fn(e);
  //       date = nowDate;
  //     }
  //   };
  // }
  /**
   * expMousemoveHandler
   *
   * @param {*} e
   */
  public expMousemoveHandler(e: any) {
    const pageX = e.pageX;
    const pageY = e.pageY;
    const bannerWidth = this.expDom.offsetWidth;
    const bannerHeight = this.expDom.offsetHeight;
    const offsetLeft = this.expDom.offsetLeft;
    const offsetTop = this.contentDom.offsetTop;
    const x = pageX - offsetLeft - bannerWidth / 2;
    const y = -(pageY - offsetTop - bannerHeight / 2);
    this.expDom.style.transform = `rotateY(${x / 50}deg) rotateX(${y / 25}deg)`;
  }
  /**
   * expMouseoutHandler
   *
   */
  public expMouseoutHandler() {
    this.expDom.style.transform = 'rotateY(0deg) rotateX(0deg)';
  }

  public switchExp(e: any, index: number) {
    e.stopPropagation();
    this.sliderDom = document.getElementsByClassName('slider-container')[0] as HTMLElement;
    this.sliderDom.style.opacity = '0';
    this.sliderDom.addEventListener('webkitTransitionEnd', this.transitionEndHandler(index));
  }

    /**
   * transitionEndHandler
   *
   */
  transitionEndHandler(index: number) {
    this.sliderDom.style.opacity = '1';
    this.expCur = this.section03.expList[index];
    this.selectExpIndex = index;
    this.sliderDom.removeEventListener('webkitTransitionEnd', this.transitionEndHandler);
  }

}
</script>


<style scoped lang="less">
.slider{
  width: 80vw;
  height: 55vh;
  padding: 0 5vw;
  background: #f2f4f5;
  margin: 0 auto;
  border-radius: 0.3125rem;
  color: #000;
  transition: transform .3s linear;
  overflow: hidden;
  box-shadow: 0 0 0.78125rem rgb(0 0 0 / 50%);
  border: 1px solid #eee;
  display: flex;
  justify-content: center;
}
.zp-resume-content{
  flex-direction: column;
}


h4 {
  color: #945c4c;
  font-size: 20px;
  text-align: center;
  margin-bottom: 20px;
}

.time {
  color: #af7464;
  font-size: 20px;
}

.post {
  color: #9e6351;
  font-size: 16px;
  text-align: center;
}

.tech {
  color: #9e6351;
  font-size: 14px;
}

.list {
  .exp-item {
    font-size: 16px;
    line-height: 32px;
    margin: 12px 0;
  }
  .exp{
    position: relative;
    padding-left: 3vw;
    &::before {
      content: '';
      position: absolute;
      top: 50%;
      left: 0;
      width: 10px;
      height: 10px;
      -webkit-transform: translateY(-50%);
      transform: translateY(-50%);
      border-radius: 50%;
      background-color: #af7464;
    }
  }
}
.shout-cut {
  width: 50vw;
  margin: 0 auto;
  padding-top: 15vh;

  ul {
    display: flex;
    justify-content: space-between;
    align-items: center;
    z-index: 101;

    li {
      width: 14px;
      height: 14px;
      border-radius: 50%;
      background-color: #623c32;
      cursor: pointer;
      transition: background-color 0.3s linear;
      position: relative;

      &::before {
        content: '';
        position: absolute;
        top: -30px;
        right: -30px;
        left: -30px;
        bottom: -30px;
      }
    }

    .-selected {
      background-color: #af7164;
    }
  }
}

@media (max-width: 700px)  { // 小于700的设备
  .slider-container{
    .left, .right{
      width: 100%;
    }
    .exp, .time{
      font-size: .75rem;
      line-height: 1rem;
    }
    .exp{
      position: relative;
      padding-left: 3vw;
      &::before {
        content: '';
        width: 0.4rem;
        height: 0.4rem;
      }
    }
  }
}
@media (min-width: 700px) { //>700的设备 
  .slider-container{
    display: flex;
    align-items: center;
    .left{
      flex: 160px 0 0;
    }
    .right{
      margin-left: 50px;
      flex: 1;
    }
  }
}
</style>