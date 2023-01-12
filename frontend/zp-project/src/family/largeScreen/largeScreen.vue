<template>
    <div class="largeScreen" id="largeScreen">
        <div class="head" v-if="screenData.showHead"></div>
        <div class="title" v-if="screenData.isShowTitle"></div>

        <div class="screen-layout" id="container">
          <div class="screen-layout-item" v-for="(item, index) in screenData.unitArray" :key="index"
            :style="getSize(item.rowspan, item.colspan, item.x, item.y)">
            <component :unitData="item.setData"  :is="item.tree.id"></component>
          </div>
        </div>

        <!-- <screenLayout :rowCount="screenData.row" :columnCount="screenData.column">
          <screenLayoutItem v-for="(item, index) in screenData.unitArray" :key="index"
            :col="item.colspan" :row="item.rowspan">
              <component :unitData="item.setData"  :is="item.tree.id"></component>
          </screenLayoutItem>
        </screenLayout> -->

        <div class="footer" v-if="screenData.isShowFooter"></div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import screen from '@/family/screen';
import TypeProportion from './TypeProportion.vue';
import DataContrast from './DataContrast.vue';


@Component({
  name:'largeScreen',
  components: { TypeProportion, DataContrast }
})
export default class largeScreen extends Vue {

  screenData: any = {}; // 大屏数据
  containerHeight = 0;
  containerWidth = 0;

  created() {
    if (this.$route.query.set_id) {
      this.screenData = screen.list.filter((obj) => {
        return this.$route.query.set_id === obj.set_id;
      })[0];
      console.log(this.screenData);
    }
  }

  mounted() {
    const container = document.getElementById('container') as HTMLElement;
    container.style.height = this.getHeight().height + 'px';
    container.style.width = this.getHeight().width + 'px';
  }

  getHeight() {
    let height = 0;
    if (this.screenData.showHead) {
      height = height + 30;
    }
    if (this.screenData.showTitle) {
      height = height + 30;
    }
    if (this.screenData.showFooter) {
      height = height + 30;
    }
    const screen = document.getElementById('largeScreen') as HTMLElement;
    this.containerHeight = screen.clientHeight - height;
    this.containerWidth = screen.clientWidth - height;
    return { height: this.containerHeight, width: this.containerWidth};
  }

  getSize(row: any, column: any, x: any, y: any) {
    let h = Math.floor((this.containerHeight - (2 * this.screenData.row)) / this.screenData.row);
    let w = Math.floor((this.containerWidth - (2 * this.screenData.column)) / this.screenData.column);
    let height = h * row;
    let width = w * column;
    return { height: height + 'px', width: width + 'px', top: h * y + 'px', left: w * x + 'px'};
  }

}

</script>

<style scoped lang="less">
.largeScreen{
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  background: #081a4b;
  z-index: 999;
}

.head, .title, .footer{
  width: 100%;
  height: 30px;
  border: 1px solid #394988;
}

.screen-layout{
  width: 100%;
  position: relative;
}

.screen-layout-item{
  border: 1px solid #394988;
  display: inline-block;
  position: absolute;
}
</style>