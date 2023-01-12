<template>
    <div class="zp-layout resume" >
        <div class="navs" v-if="isPC()">
            <div class="nav" v-for="(item, index) in nav" :key="index">
                <div class="origin" :class="[{'active': currentPage === index}]" 
                @click="directToPage(index)">
                    <i :class="['iconfont', item.icon]"></i>
                    <span class="tip-text">{{item.text}}</span>
                </div>
            </div>
        </div>

        <main id="fullpage">
            <div class="section screen1">
                <Section01></Section01>
            </div>
            <div class="section screen2">
                <Section02></Section02>
            </div>
            <div class="section screen3">
                <Section03></Section03>
            </div>
            <div class="section screen4">
                <Section04></Section04>
            </div>
        </main>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { Fullpage } from '@fe_korey/fullpage';
import Section01 from "@/resume/Section01.vue";
import Section02 from "@/resume/Section02.vue";
import Section03 from "@/resume/Section03.vue";
import Section04 from "@/resume/Section04.vue";

@Component({
  components: { Section01, Section02, Section03, Section04 },
})
export default class Resume extends Vue {
    public currentPage = 0;
    public fullScreen: any;
    public nav: any[] = [
        { text: '首页', icon: 'zp-shouye' },
        { text: '技术', icon: 'zp-xinxijishu' },
        { text: '经历', icon: 'zp-gongzuojingli' },
        { text: '作品', icon: 'zp-zuopin' }
    ];

    created() {
        
    }

    mounted() {
        this.createFullpage();
    }

    // 创建全屏翻页
    private createFullpage() {
        var that = this;
        this.fullScreen = new Fullpage({
            root: '#fullpage',
            hasArrow: true,
            speedTime: 0.5,
            slideCallback(index: any) {
                that.currentPage = index;
            }
        });
    }

    // 手动跳转到某屏
    public directToPage(index: any) {
      this.currentPage = index;
      this.fullScreen.directToPage(index);
    }

    // 判断是否是PC端打开，移动端不显示导航
    public isPC() {
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


}
</script>

<style scoped lang="less">
.navs {
    position: fixed;
    top: 50%;
    right: 20px;
    transform: translateY(-50%);
    font-size: 12px;
    z-index: 99999;
    display: flex;
    justify-content: space-around;
    align-items: center;
    flex-direction: column;
    overflow: hidden;
    .nav{
        height: 40px;
        line-height: 40px;
        padding-left: 80px
    }
    .origin {
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: rgba(0,0,0,0.7);
        cursor: pointer;
        opacity: 0.1;
        position: relative;
        text-align: center;
    }

    .iconfont{
        transform: scale(0.8);
        color: #fff;
        opacity: 1;
        display: none;
    }
    .tip-text{
        position: absolute;
        left: -70px;
        top: 0;
        background: rgba(0,0,0,0.7);
        color: #fff;
        padding: 4px 12px;
        height: 16px;
        line-height: 16px;
        opacity: 1;
        border-radius: 3PX 0 0 3PX;
        font-weight: bold;
        display: none;
    }
    .tip-text::after{
        content: '';
        width:0;
        height:0;
        border-width: 12px 0 12px 10px;
        border-style:solid;
        border-color:transparent transparent transparent rgba(0,0,0,0.7);;
        position: absolute;
        right: -10px;
        top: 0;
    }

    .origin:hover{
        width: 24px;
        height: 24px;
        line-height: 24px;
        opacity: 1;
        .tip-text, .iconfont{
            display: block;
            z-index: 99999;
        }
    }
    .active{
        width: 24px;
        height: 24px;
        line-height: 24px;
        opacity: 1;
        .iconfont{
            display: block;
        }
    }
}
.section{
    width: 100%;
    height: 100%;
    display: absolute;
}
.screen1{
    background: rgb(135, 176, 165) url('../assets/img/bg.png');
}
.screen2{
    background: rgb(77, 94, 143) url('../assets/img/bg.png');
}

.screen3{
    background: rgb(148, 92, 76) url('../assets/img/bg.png');
}

.screen4{
    background: rgb(75, 133, 160) url('../assets/img/bg.png');
}


</style>