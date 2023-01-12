<template>
  <div class="year-contrast">
    <div class="board-item-line">
        <span>布局：</span>
        <el-radio-group v-model="size">
            <el-radio label="1X1">1X1</el-radio>
            <el-radio label="2X1">2X1</el-radio>
            <el-radio label="2X2">2X2</el-radio>
        </el-radio-group>
    </div>
    <div class="board-item-line">
        <span>维度：</span>
        <el-checkbox-group 
            style="display:inline-block"
            v-model="checkedDimension"
            @change="checkedChange($event)">
            <el-checkbox v-for="item in dimensionsArr" :label="item" :key="item">{{item}}</el-checkbox>
        </el-checkbox-group>

        <el-select v-if="checkedDimension[0] === '年维度对比'"
            v-model="year" placeholder="请选择">
            <el-option  v-for="item in yearsArr"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            </el-option>
        </el-select>

        <el-select v-if="checkedDimension[0] === '月维度对比'"
            v-model="month" placeholder="请选择">
            <el-option  v-for="item in monthsArr"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            </el-option>
        </el-select>
    </div>
    
    <BoardItemGraphics :id="'DataContrast'" :label="dimension" :key="dimension"></BoardItemGraphics>
    
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import * as echarts from 'echarts';
import BoardItemGraphics from '../BoardItemGraphics.vue';

@Component({
  name:'DataContrast',
  components: { BoardItemGraphics }
})
export default class DataContrast extends Vue {
    @Prop()
    public unitInfo: any;

    @Prop()
    public tree: any;

    @Prop()
    public type: any;

    size = '1X1';
    year = '';
    month = '';
    yearsArr = [
        { value: 'year1', label: '近一年' },
        { value: 'year2', label: '近二年' },
        { value: 'year3', label: '近三年' },
        { value: 'year4', label: '近四年' },
        { value: 'year5', label: '近五年' },
    ];
    monthsArr = [
        { value: 'month1', label: '近一月' },
        { value: 'month2', label: '近二月' },
        { value: 'month3', label: '近三月' },
        { value: 'month4', label: '近四月' },
        { value: 'month5', label: '近五月' },
    ];
    dimension = 'year'; // 当前维度
    checkedDimension = ['年维度对比'];
    dimensionsArr = ['年维度对比', '月维度对比'];

    created() {
        if (this.unitInfo.setData.size) {
            this.size = this.unitInfo.setData.size;
        } else {
            this.size = '1X1';
        }
        if (this.type === 'edit') {
            this.year = this.unitInfo.setData.year ? this.unitInfo.setData.year : '';
            this.month = this.unitInfo.setData.month ? this.unitInfo.setData.month : '';
            this.dimension = this.unitInfo.setData.dimension ? this.unitInfo.setData.dimension : 'year';
            if (this.dimension === 'month') {
                this.checkedDimension = ['月维度对比'];
            }
        }
    }

    mounted(){
        // this.drawLine();
    }

    // 维度选择改变事件
    checkedChange(e: any) {
        if (e.length > 1) {
            this.checkedDimension.splice(0, 1)
        }
        if (this.checkedDimension[0] === '年维度对比') {
            this.dimension = 'year';
        } else {
            this.dimension = 'month';
        }
    }

    drawLine(){
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('myChart'))
        // 绘制图表
        
        myChart.setOption({
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                type: 'cross',
                crossStyle: {
                        color: '#999'
                    }
                }
            },
            legend: {
                data: ['收入', '支出'],
            },
            xAxis: [
                {
                type: 'category',
                data: ['一月', '二月', '三月', '四月', '五月'],
                axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                type: 'value',
                name: '元',
                }
            ],
            series: [{
                name: '收入',
                type: 'bar',
                data: [
                    12000, 14000, 10000, 15000, 13000,
                ]
                },
                {
                name: '支出',
                type: 'bar',
                data: [
                    5000, 8000, 4500, 6000, 6500,
                ]
            }]
        });
    }

}

</script>

<style scoped lang="less">
.board-item-line{
    margin: 10px 0;
}
.el-select{
    width: 150px;
    margin-left: 20px;
}
img{
    width: 90%;
    height: 90%;
}
</style>