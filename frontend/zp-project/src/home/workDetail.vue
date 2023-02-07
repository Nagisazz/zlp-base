<template>
    <div class="work-detail">
        <img :src="detailUrl" alt="" v-if="type !== 'merry'" @click="toMain()">
        <video height="400" controls v-if="type === 'merry'">
            <source :src="detailUrl" type="video/mp4">
        </video>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  name:'workDetail',
})
export default class workDetail extends Vue {
    detailUrl = '';
    type: any;

    created () {
        console.log(this.$route.query.id);
        this.type = this.$route.query.id;
        if (this.type === 'underover') {
            this.detailUrl = 'http://1.15.87.105:11000/love/zp/home/underover-detail.jpg';
        } else if (this.type === 'fund') {
            this.detailUrl = 'http://1.15.87.105:11000/love/zp/home/fund-detail.jpg';
        } else if (this.type === 'price') {
            this.detailUrl = 'http://1.15.87.105:11000/love/zp/home/price.png';
        } else if (this.type === 'merry') {
            this.detailUrl = 'http://1.15.87.105:11000/love/zp/home/merry-detail.mp4';
        } else if (this.type === 'share-file') {
            this.detailUrl = 'http://1.15.87.105:11000/love/zp/home/share-file.png';
        }
        
    }

    // 去其他子应用
    toMain() {
        // @ts-ignore 
        // 忽略ts校验
        if ((window as any).__POWERED_BY_QIANKUN__) {
            if (this.type === 'price') {
                window.history.pushState(null, 'title', '/platform/price'); // 子应用跳转主应用
                // this.parentRouter.push({
                //     path: '/platform/price'
                // });
            } else if (this.type === 'share-file') {
                window.history.pushState(null, 'title', '/platform/file');
            } else if (this.type === 'fund') {
                window.history.pushState(null, 'title', '/platform/fund');
            }
        }
    }

}

</script>

<style scoped lang="less">
.work-detail{
    text-align: center;
    img, video{
        width: 90%;
    }
}
</style>