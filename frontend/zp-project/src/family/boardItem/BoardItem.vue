<template>
  <div class="board-item">
    <el-row :gutter="20">
      <!-- 左侧部分 -->
      <el-col :span="8">
        <el-input
          size="small"
          placeholder="输入关键字进行过滤"
          v-model="filterText">
        </el-input>

        <el-tree
          class="filter-tree"
          :data="data"
          :props="defaultProps"
          default-expand-all
          :filter-node-method="filterNode"
          @node-click="selectTree($event)"
          ref="tree">
        </el-tree>
      </el-col>

      <!-- 右侧部分 -->
      <el-col :span="16">
        <component :type="type" :unitInfo="unitInfo" :tree="showComponent"  :is="showComponent.id" ref="itemChild"></component>
      </el-col>
    </el-row>

    <div class="zp-dialog-footer">
      <el-button size="medium" round @click="cancel()">取 消</el-button>
      <el-button type="primary" size="medium" round @click="confirm()">确 定</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import TypeProportion from '../boardItem/TypeProportion.vue';
import DataContrast from '../boardItem/DataContrast.vue';
import Image from '../boardItem/Image.vue';
import Calendar from '../boardItem/Calendar.vue';

@Component({
  name:'BoardItem',
  components: { TypeProportion, DataContrast, Image, Calendar }
})
export default class BoardItem extends Vue {

  @Prop()
  public type: any;

  filterText = '';
  data = [{
    id: 'finance',
    label: '财务',
    children: [{
      id: 'TypeProportion',
      label: '各项占比',
      size: '1X1'
    },{
      id: 'DataContrast',
      label: '柱状图对比',
      size: '1X1'
    }]
  }, {
    id: 'tool',
    label: '工具',
    children: [{
      id: 'Image',
      label: '图片',
      size: '1X1'
    },{
      id: 'Calendar',
      label: '日历',
      size: '1X1'
    }]
  }];
  defaultProps = {
    children: 'children',
    label: 'label'
  }
  showComponent: any = {}; // 右侧显示的组件

  @Prop()
  public unitInfo: any;

  created() {
    console.log(this.unitInfo);
    if (this.unitInfo.tree.id) {
      this.data.map((obj) => {
        obj.children.map((child) => {
          if (child.id === this.unitInfo.tree.id) {
            this.showComponent = child;
          }
        })
      })
    } else {
      this.showComponent = this.data[0].children[0];
    }
  }

  @Watch('filterText')
  filterTextChange(newVal: any) {
    (this.$refs.tree as any).filter(newVal);
  }

  filterNode(value: any, data: any) {
    if (!value) return true;
    return data.label.indexOf(value) !== -1;
  }

  // 左侧树点击事件
  selectTree(e: any) {
    console.log(e);
    this.showComponent = e;
  }

  // 取消
  cancel() {
      this.$emit('selectCancel');
  }

  // 确定
  confirm() {
    const data= {
      unitInfo: this.unitInfo,
      tree: this.showComponent,
      setData: (this.$refs.itemChild as any)['_data']
    };
    this.$emit('selectOver', data);
  };

}

</script>

<style scoped lang="less">
</style>