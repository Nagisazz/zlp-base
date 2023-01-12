<template>
  <div class="config-board">
    <el-row :gutter="20">
      <!-- 左侧看板部分 -->
      <el-col :span="16">
        <div class="grid-content bg-purple">
          <div class="board board-left">
            <table id="table" >
              <tr v-if="boardForm.showTitle">
                <td class="fixed-height" rowspan="1" colspan="4">
                  {{boardForm.title}}
                </td>
              </tr>
              <tr v-if="boardForm.showHead">
                <td class="fixed-height" rowspan="1" colspan="4">
                  <i class="el-icon-plus" @click="chooseBoardHeader()"></i>
                </td>
              </tr>
              <tr v-for="(y, yIndex) in layoutDisplay" :key="yIndex">
                <template v-for="(item, xIndex) in y">
                  <td :key="xIndex" v-if="item.show"
                    :rowspan="item.rowspan" :colspan="item.colspan" 
                    class="interface-td">
                    <div class="interface-content" :style="getWidthH(item)" @click="chooseBoardItem(yIndex, xIndex)">
                      <template v-if="item.tree.id" >
                        <a class="cross" @click="delChart($event, item)">
                          <i class="el-icon-close"></i>
                        </a>
                        <BoardItemGraphics :id="item.tree.id" :label="item.setData.dimension"></BoardItemGraphics>
                      </template>
                      <template v-else>
                        <i class="el-icon-plus"></i>
                      </template>
                    </div>
                  </td>
                </template>
              </tr>
              <tr v-if="boardForm.showFooter">
                <td class="fixed-height" rowspan="1" colspan="4">
                  <i class="el-icon-plus" @click="chooseBoardFooter()"></i>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </el-col>

      <!-- 右侧内容选择部分 -->
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <div class="board board-right">
            <el-form :rules="rules" ref="boardForm" :model="boardForm" label-width="80px">
              <el-form-item label="看板名称" prop="title" ref="title">
                <el-input v-model="boardForm.title"></el-input>
                <el-checkbox v-model="boardForm.showTitle">显示标题</el-checkbox>
              </el-form-item>
              <el-form-item label="看板布局" >
                <el-select v-model="boardForm.layout" placeholder="请选择" @change="changeLayout($event)">
                  <el-option  v-for="item in layoutsArr"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="翻转方向">
                <el-select style="width: 120px" v-model="boardForm.direction" placeholder="请选择">
                  <el-option value="level"  label="水平"></el-option>
                  <el-option value="vertical" label="垂直"></el-option>
                </el-select>
                间隔<el-input style="width: 80px" v-model="boardForm.interval"></el-input>s
              </el-form-item>
              <el-form-item label="">
                <el-checkbox v-model="boardForm.showHead">页眉</el-checkbox>
                <el-checkbox v-model="boardForm.showFooter">页脚</el-checkbox>
              </el-form-item>
              <el-form-item label="背景图">
                <el-upload
                  class="upload-demo"
                  action="https://jsonplaceholder.typicode.com/posts/"
                  :on-preview="handlePreview"
                  :on-remove="handleRemove"
                  :before-remove="beforeRemove"
                  :file-list="fileList">
                  <el-button size="small" type="primary">点击上传</el-button>
                  <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
                </el-upload>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>

      <img v-if="showImg" :src="dataURL" alt="" style="width:200px; height: 150px;">
    </el-row>
    <div class="zp-dialog-footer">
      <el-button size="medium" round @click="cancel()">取 消</el-button>
      <el-button type="primary" size="medium" round @click="confirm()">确 定</el-button>
    </div>

    <el-dialog title="选择元素" :visible.sync="boardItemVisible" 
      width="80%" top="10vh" :append-to-body="true">
      <div v-if="boardItemVisible">
        <BoardItem :type="$attrs.type" :unitInfo="currentUnitInfo" @selectOver="selectOver" @selectCancel="boardItemVisible = false"></BoardItem>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import BoardItem from './boardItem/BoardItem.vue';
import BoardItemGraphics from './BoardItemGraphics.vue';
import html2canvas from 'html2canvas';
import { Unit, Tree } from '../domain/unit';
import { getKanBan } from '../api';

@Component({
  name:'ConfigBoard',
  components: { BoardItem, BoardItemGraphics }
})
export default class ConfigBoard extends Vue {
  props: {
    type: {
      type: string,
      default: 'add',
      required: true
    },
    screenData: any
  }

  layoutDisplay: any = [];
  boardItemVisible: Boolean = false;
  currentUnitInfo: any = {}; // 想要新增的单元信息
  headFooterHeight = 0; // 除了主体内容的其他内容高度和
  dataURL = '';
  showImg = false;

  created() {
    if (this.$attrs.type === 'edit') {
      console.log(this.$attrs.screenData);
      const screen = this.$attrs.screenData as any;
      this.boardForm = Object.assign(this.boardForm, this.$attrs.screenData);
      this.changeLayout(this.boardForm.layout);
      setTimeout(() => {
        screen.unitArray.map((arr: any) => {
          this.setLayout(arr.y, arr.x, arr.colspan, arr.rowspan, arr.size, arr.tree, arr.setData);
        })
      }, 0)
    } else {
      this.changeLayout('4X3');
    }
    
    const param = {
      code: "0039DTFa1O4u3D08ZTHa1XXqXf09DTFT",
      key: "wx23ccf23190071b3a",
      token: "NEjhYAAgp7BX4ogBR5o1o8wjc0KPdn0JnIAPkIsONu8=_1650973170384"
    };
    getKanBan(param).then((res) => {
      console.log(res);
    })
  }

  mounted() {
    // setTimeout(() => {
    //   const table = document.getElementById('table') as HTMLElement;
    //   table.style.background = 'url(../assets/img/email.png)'; // 需要换成服务器图片地址
    //   table.style.backgroundRepeat = 'no-repeat';
    //   table.style.backgroundSize = '100% 100%';
    // }, 500);
  }

  updated() {
    const table = document.getElementById('table') as HTMLElement;
    const basicWidth = Math.round(table.offsetWidth / this.layoutDisplay[0].length);
    console.log(basicWidth);
    if (!table) { return; }
    const children = table.getElementsByClassName('interface-td');

    if (!children) { return; }

    Array.from(children).forEach((x: any) => {
      x.style.width = `${(x.colSpan * basicWidth)}px`;
      x.style['max-width'] = `${(x.colSpan * basicWidth)}px`;
    });
  }

  getWidthH(item: any): any {
    const basicHeight = (380 - this.headFooterHeight) / this.layoutDisplay.length;
    const dd = {
      height: Math.round(item.rowspan * basicHeight) + 'px',
    }
    return dd;
  }

  // 选择具体看板元素内容
  chooseBoardItem(y: number, x: number) {
    this.boardItemVisible = true;
    this.currentUnitInfo = this.layoutDisplay[y][x];
    console.log(y, x);
    console.log(this.currentUnitInfo);
  }

  chooseBoardHeader() {

  }

  chooseBoardFooter() {
    
  }

  setLayout(y: number, x: number, colspan: number, rowspan: number, size: string, tree: Tree, setData: any) {
    const array = JSON.parse(JSON.stringify(this.layoutDisplay));

    // 清空原栏位并将合并栏位复原
    if (array[y][x].colspan !== 1 || array[y][x].rowspan !== 1 || array[y][x].tree.id) {
      for (let i = y; i < Number(y) + Number(rowspan); i++) {
        for (let j = x; j < Number(x) + Number(colspan); j++) {
          array[i][j] = new Unit();
          array[i][j].y = y;
          array[i][j].x = x;
        }
      }
    }

    colspan = Number(size.split('X')[0]) || 1;
    rowspan = Number(size.split('X')[1]) || 1;
    let flag = true;
    
    // 取得新的SIZE大小并确认所合并栏位是否已有组件值或是已经被合并过或是不存在
    for (let i = y ; i < Number(y) + Number(rowspan); i++) {
      for (let j = x; j < Number(x) + Number(colspan); j++) {
        if (!array[i] || !array[i][j] || array[i][j].tree.id ||
          array[i][j].rowspan === 0 || array[i][j].colspan === 0) {
          flag = false;
          break;
        }
        array[i][j].colspan = 0;
        array[i][j].rowspan = 0;
        array[i][j].show = false;
      }
      if (!flag) {
        break;
      }
    }
    array[y][x].tree = tree;
    array[y][x].setData = setData;
    array[y][x].colspan = colspan;
    array[y][x].rowspan = rowspan;
    array[y][x].show = true;

    if (flag) {
      this.layoutDisplay = array;
      console.log(this.layoutDisplay);
    } else {
      // this.$message('剩余空间不足');
    }
  }

  // 看板元素选择完毕事件
  selectOver(data: any) {
    console.log(data);
    const unitInfo = data.unitInfo;
    const setData = data.setData;
    const tree = data.tree;
    this.setLayout(unitInfo.y, unitInfo.x, unitInfo.colspan, unitInfo.rowspan, setData.size, tree, setData);
    this.boardItemVisible = false;
  }

  // 删除单元
  delChart(e: any, item: any) {
    if (e) {
      e.cancelBubble = true;
      e.stopPropagation(); // 阻止事件冒泡
    }
    this.setLayout(item.y, item.x, item.colspan, item.rowspan, item.tree.size, new Tree(), {});
  }
  // 左侧end

  // 右侧  start
  boardForm = {
    title: '',
    showTitle: false,
    layout: '4X3',
    direction: 'vertical', // 方向
    interval: '20',
    showHead: false,
    showFooter: false
  };
  rules = {
    title: [
      { required: true, message: '请输入看板名称', trigger: 'blur' },
    ]
  };
  layoutsArr = [
    { label: '1x1', value: '1X1' },
    { label: '2x1', value: '2X1' },
    { label: '3x2', value: '3X2' },
    { label: '4x3', value: '4X3' },
  ];
  fileList = [
    // {name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'},
  ]

  @Watch('boardForm.showTitle')
  private showTitleOnChange(newVal: boolean) {
    if(newVal) {
      this.headFooterHeight = this.headFooterHeight + 34;
      this.layoutDisplay = [...this.layoutDisplay];
    } else {
      this.headFooterHeight = this.headFooterHeight - 34;
    }
  }

  @Watch('boardForm.showHead')
  private showHeadOnChange(newVal: boolean) {
    if(newVal) {
      this.headFooterHeight = this.headFooterHeight + 34;
      this.layoutDisplay = [...this.layoutDisplay];
    } else {
      this.headFooterHeight = this.headFooterHeight - 34;
    }
  }

    @Watch('boardForm.showFooter')
  private showFooterOnChange(newVal: boolean) {
    if(newVal) {
      this.headFooterHeight = this.headFooterHeight + 34;
      this.layoutDisplay = [...this.layoutDisplay];
    } else {
      this.headFooterHeight = this.headFooterHeight - 34;
    }
  }

  handleRemove(file: File, fileList: any[]) {
    console.log(file, fileList);
  };

  handlePreview(file: File) {
    console.log(file);
  };

  beforeRemove(file: File, fileList: any[]) {
    // return this.$confirm(`确定移除 ${ file.name }？`);
  }

  // 布局下拉选择事件
  changeLayout(e: any) {
    const arr = e.split('X');
    const x = Number(arr[0]) || 1;
    const y = Number(arr[1]) || 1;
    const array = new Array();
    for (let i = 0; i < y; i++) {
      array[i] = new Array();
      for (let j = 0; j < x; j++) {
        array[i][j] = new Unit();
        array[i][j].y = i;
        array[i][j].x = j;
      }
    }
    this.layoutDisplay = array;
  }

  // 右侧end

  // 取消
  cancel() {
    this.$emit('cancel')
  }

  // 确定
  confirm() {
    (this.$refs['boardForm'] as any).validate((valid: any) => {
      if (valid) {
        // this.$emit('confirm');
        let imageWrapper = document.getElementById('table');
        const that = this;
        html2canvas(imageWrapper, {
          scale: 1,
          useCORS: true,
          allowTaint: true,//允许跨域图片
          taintTest: false
        }).then((canvas: any) => {
          this.showImg = true;
          this.dataURL = canvas.toDataURL("image/png");
          // console.log(dataURL);
          // that.finalCanvas.push({
          //   fileUrl: dataURL,
          //   renameFileName: item.userDTO.user.realName + '.png'
          // });
          // if (that.finalCanvas.length >= that.allDataUrl.length) {
          //   resolve(that.finalCanvas)
          // }
        })
      } else {
        console.log('error submit!!');
        return false;
      }
    });
  };

}

</script>

<style scoped lang="less">
.config-board{
  .board{
    height: 400px;
  }
  .el-icon-plus{
    cursor: pointer;
  }

  .board-left{
    background: #000;
    padding: 10px;
    border-radius: 4px;
    table{
      width: 100%;
      height: 100%;
      tr{
        text-align: center;
        width: 100%;
      }
      .fixed-height{
        height: 30px;
        background-color: #081a4b;
        border: 1px solid #394988;
        color: #fff;
      }
    }
    .interface-content {
      background-color: #081a4b;
      border: 1px solid #394988;
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      overflow: hidden;
      position: relative;
      .cross {
        position: absolute;
        top: 0;
        right: 10px;
        z-index: 999;
        color: #666;

        &:hover {
          cursor: pointer;
        }
      }
    }
  }
}



</style>