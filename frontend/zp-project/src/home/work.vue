<template>
    <ul class="works">
        <li class="work" v-for="(work, index) in works" :key="index"
            @mouseenter="mouseenter(work)"
            @mouseleave="mouseleave()">
          <div class="more-modal" v-show="currentWork.id === work.id"
            @click="showMore()">
            <i class="iconfont zp-eye"></i>
          </div>
          <img class="work-img" :class="[{'work-img-hover': currentWork.id === work.id}]" :src="work.url" alt="">
          <div class="work-title">{{work.title}}</div>
        </li>
    </ul>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getHomeData } from '../api';

@Component({
  name:'work',
})
export default class work extends Vue {
  works: any[] = [
    { id: 'underover', url: 'http://1.15.87.105:11000/love/zp/home/logo.jpg', title: '谁是卧底小程序' },
    { id: 'merry', url: 'http://1.15.87.105:11000/love/zp/home/merry.png', title: '婚礼请柬+时间轴' },
    { id: 'price', url: 'http://1.15.87.105:11000/love/zp/home/price.png', title: '双十一猜价活动攻略' },
    { id: 'fund', url: 'http://1.15.87.105:11000/love/zp/home/fund.png', title: '预测基金投资金额' },
  ]
  currentWork: any = {id: 0};

  created() {
    getHomeData().then((res) => {
      this.works = res.works;
    })
  }

  mouseenter(work: any) {
    this.currentWork = work;
  }

  mouseleave() {
    this.currentWork = {id: 0};
  }

  showMore() {
    // this.$router.push({ path: `/work/${this.currentWork.id}` })
    this.$emit('workDetail', this.currentWork);
  }

}

</script>

<style scoped lang="less">
.works{
  width: 100%;
  height: 100%;
  display: grid;
  gap: 30px;
  grid-template-columns: repeat(3, 1fr);
}
@media (max-width: 800px)  { // 小于950的设备
  .works{
    grid-template-columns: repeat(2, 1fr);
  }
}
@media (max-width: 500px)  { // 小于950的设备
  .works{
    grid-template-columns: repeat(1, 1fr);
  }
}
.work{
  height: 80%;
  animation: scaleUp 0.25s ease forwards;
  cursor: pointer;
  position: relative;
}
.work-img{
  width: 100%;
  height: 90%;
  object-fit: cover;
  border-radius: 20px;
}
// .work-img-hover{
//   transform: scale(1.1);
// }

.work-title{
  padding-left: 20px;
  box-sizing: border-box;
  margin-top: 10px;
}
.more-modal{
  width: 100%;
  height: 90%;
  border-radius: 20px;
  text-align: center;
  position: absolute;
  top: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
}
.more-modal .iconfont{
  font-size: 24px;
}

</style>