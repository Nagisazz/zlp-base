<template>
  <div class="board-item-graphics">
    <img :src="'http://1.15.87.105:11000/love/zp/family/' + imgUrl + '.png'" alt="">
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component({
  name:'BoardItemGraphics',
})
export default class BoardItemGraphics extends Vue {

    @Prop()
    public id: string;
    @Prop({default: ''})
    public label: string;

    imgUrl = '';

    created() {
        console.log(this.id);
        this.imgUrl = (this.id + (this.label ? '_' + this.label : '')).trim();
    }


}

</script>

<style scoped lang="less">
img{
    max-width: 95%;
    max-height: 95%;
}
</style>