<template>
    <div class="about">
        <div class="about-info">
            <div v-for="(item, index) in aboutList" :key="index">
                {{item}}
            </div>
        </div>

        <div class="git" v-for="(item, index) in gits" :key="index" @click="toGit(item)">
            <span>GitHub:</span>{{item}}
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getHomeData } from '../api';

@Component({
  name:'about',
})
export default class about extends Vue {
    aboutList: any = [
        "我们是一对程序员小夫妻",
        "他96，我95，我属猪，他属鼠",
        "是积极向上，充满想法的人",
        "平时在空闲时间喜欢利用自己所学写些有趣的小项目",
        "这些项目都已经上传到GitHub",
        "如果有小伙伴有兴趣欢迎自取哦！！！"
    ];
    gits: any = [
        "https://github.com/Nagisazz",
        "https://github.com/chp-22"
    ];
    
    created() {
        getHomeData().then((res) => {
            this.aboutList = res.aboutList;
            this.gits = res.gits;
        })
    }

    toGit(url: any) {
        window.open(url);
    }
    
}

</script>

<style scoped lang="less">
.about-info{
    width: 100%;
    text-align: center;
    line-height: 2.2;
    margin-bottom: 20px;
}
.git{
    width: 300px;
    margin: 0 auto;
    margin-bottom: 10px;
    cursor: pointer;
    span{
        display: inline-block;
        width: 80px;
        text-align: right;
        margin-right: 10px;
    }
}
</style>