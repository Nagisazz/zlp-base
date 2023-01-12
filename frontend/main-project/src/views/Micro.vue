<template>
    <div class="micro1">
    </div>
</template>

<script>
import { start } from 'qiankun';

export default {
    name: 'Micro',

    mounted() {
        if (!window.qiankunStarted) {
            window.qiankunStarted = true;
            start();
        }
    },
}
</script>

<style scoped>


</style>


