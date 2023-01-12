<template>
  <div class="year-contrast">
    <div id="myChart" :style="{width: '90%', height: '90%'}"></div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import * as echarts from 'echarts';

@Component({
  name:'DataContrast',
})
export default class DataContrast extends Vue {
    @Prop()
    public unitData: any;

    created() {
    }

    mounted(){
        this.drawLine();
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