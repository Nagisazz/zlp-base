<template>
    <div class="Section02">
      <div class="zp-resume-title">技术</div>
      <div class="content">
        <div class="left">
          <wordcloud
            :fontSize="[22,28]"
            :fontScale="'log'"
            :spiral="'rectangular'"
            :data="section02.defaultWords"
            nameKey="name"
            valueKey="value"
            :color="section02.myColors"
            :showTooltip="false"
            :drawOutOfBound="true">
          </wordcloud>
        </div>
        <div class="right">
          <div v-for="(desc, idx) in section02.peopleDesc" :key="idx">
            {{desc}}
          </div>
          <div>
            <span>● Github：</span> 
              <a :href="section02.workUrl[0]" target="_blank">{{section02.workUrl[0]}}</a>
          </div>
          <div>
            <span>● 博客：</span> 
              <a :href="section02.workUrl[1]" target="_blank">{{section02.workUrl[1]}}</a>
          </div>
        </div>
      </div>
      
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import wordcloud from 'vue-wordcloud';
import zz from '@/resume/zz';
import pp from '@/resume/pp';

@Component({
  components: { wordcloud }
})
export default class Section02 extends Vue {
  section02: any = zz;
  zzData: any = zz;
  ppData: any = pp;

  created() {
    if (this.$route.query.user && this.$route.query.user === 'pp') {
      this.section02 = this.ppData;
    }
  }
}

</script>

<style scoped lang="less">
.content{
  padding: 20px;
  box-sizing: border-box;
}
.left{
  background-color: #fff;
  border-radius: 10px;
}
.right{
  color: #fff;
  font-size: 16px;
  box-sizing: border-box;
  height: 100%;
}

a, a:hover, a:link {
    color: #fff;
}
@media (max-width: 700px)  { // 小于700的设备
  .content{
    padding-top: 0;
  }
  .left, .right{
    width: 100%;
  }
  .right{
    height: 380px;
    display: flex;
    flex-direction: column;
    div{
      padding: 0.6rem 0;
    }
  }
  .wordCloud{
    height: 200px;
  }
}
@media (min-width: 700px) { //>700的设备 
  .left{
    width: 45%;
  }
  .right{
    width: 55%;
    padding-left: 20px;
    div{
      padding: 1rem 0;
    }
  }
  .content{
    display: flex;
  }
  .wordCloud{
    height: 400px;
  }
}
</style>