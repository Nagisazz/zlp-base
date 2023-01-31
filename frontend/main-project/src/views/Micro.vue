<template>
    <div class="micro1">
    </div>
</template>

<script>
import { start } from 'qiankun';

export default {
    name: 'Micro',

    mounted() {
        console.log(window.qiankunStarted, 'window.qiankunStarted');
        if (!window.qiankunStarted) {
            window.qiankunStarted = true;
            start();
        }
    },
}
</script>

<style scoped>
.micro1{
    height: 100%;
}

</style>


