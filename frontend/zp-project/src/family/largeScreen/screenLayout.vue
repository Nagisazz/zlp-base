<template>
    <div class="screenLayout">
        ffff
    </div>
</template>


<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component({
    name: 'screenLayout'
})
export default class screenLayout extends Vue {
    @Prop()
    rowCount: number;

    @Prop()
    columnCount: number;

    mountd() {
        const unitWidth = 100 / this.rowCount;
        const unitHeight = 100 / this.columnCount;

        console.log(this.$children);
    }
    
}

</script>

<style scoped lang="less">

</style>