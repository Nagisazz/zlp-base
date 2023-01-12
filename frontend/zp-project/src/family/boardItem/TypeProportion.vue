<template>
  <div class="type-roportion">
    <div class="board-item-line">
        <span>布局：</span>
        <el-radio-group v-model="size">
            <el-radio label="1X1">1X1</el-radio>
        </el-radio-group>
    </div>
    <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="选择占比项目" >
        <el-select class="mult-select" v-model="form.types" multiple placeholder="不选默认全部">
            <el-option  v-for="item in typesArr"
            :key="item.value"
            :label="item.label"
            :value="item.value">
            </el-option>
        </el-select>
        </el-form-item>
    </el-form>
    <!-- <div id="myChart" :style="{width: '500px', height: '300px'}"></div> -->
    <BoardItemGraphics :id="'TypeProportion'"></BoardItemGraphics>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import * as echarts from 'echarts';
import BoardItemGraphics from '../BoardItemGraphics.vue';

@Component({
    name:'TypeProportion',
    components: { BoardItemGraphics }
})
export default class TypeProportion extends Vue {
    @Prop()
    public unitInfo: any;

    @Prop()
    public tree: any;

    @Prop()
    public type: any;

    size = '1X1';
    form = {
        types: []
    };
    typesArr = [
        { value: 'beauty', label: '美容' },
        { value: 'clothe', label: '衣物' },
        { value: 'human_relationship', label: '人情往来' },
        { value: 'house_property', label: '住房物业' },
        { value: 'recharge_payment', label: '充值缴费' },
        { value: 'home', label: '回家' },
        { value: 'travel', label: '旅游' },
        { value: 'car', label: '车费' },
    ];

    created() {
        if (this.unitInfo.setData.size) {
            this.size = this.unitInfo.setData.size;
        } else {
            this.size = '1X1';
        }
        if (this.type === 'edit') {
            this.form.types = this.unitInfo.setData.types ? this.unitInfo.setData.types : [];
        }
    }

    mounted(){
        // this.drawLine();
    }

    drawLine(){
        // 基于准备好的dom，初始化echarts实例
        let myChart = echarts.init(document.getElementById('myChart'))
        // 绘制图表
        myChart.setOption({
            legend: {
                orient: 'vertical',
                bottom: 'bottom'
            },
            series: [{
                type: 'pie',
                radius: '50%',
                data: [
                    { value: 1048, name: this.typesArr[0].label },
                    { value: 735, name: this.typesArr[1].label },
                    { value: 580, name: this.typesArr[2].label },
                    { value: 484, name: this.typesArr[3].label },
                    { value: 300, name: this.typesArr[4].label }
                ],
                emphasis: {
                    itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }]
        });
    }

}

</script>

<style scoped lang="less">
.board-item-line{
    margin: 10px 0;
}
.mult-select{
    width: 70%;
}
</style>