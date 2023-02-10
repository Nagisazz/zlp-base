<template>
    <div class="zp-message" v-if="isShow">
        <div :class="{'zp-message-content': true, 'success': type == 'success', 'error': type == 'error'}">
            <i :class="{'iconfont zp-warning-circle': true, 'zp-success': type == 'success', 'zp-error': type == 'error'}"></i> 
            {{content}}
        </div>
    </div>
</template>

<script setup lang="ts" name="Message">
    import { ref, watch } from "vue";
    interface IProps {
        content: String,
        type: String,
        remove: () => void;
    }

    const props = withDefaults(defineProps<IProps>(), {});
    let isShow = ref(true);

    const onHidden = () => {
        setTimeout(() => {
            isShow.value = false;
        }, 2000); // 2s后消失 
    }
    onHidden();

    watch(isShow, () => {
        props.remove();
    });

</script>

<style scoped lang="scss">
.zp-message{
    position: fixed;
    top: 40px;
    left: 45%;
    width: auto;
}
.zp-message-content{
    padding: 10px 14px;
    border-radius: 4px;
    background: #fdf6ec;
    border: 1px solid #E6A23C;
    color: #E6A23C;
}
.iconfont{
    margin-right: 4px;
    transform: translateY(1px);
    display: inline-block;
}
.success{
    background: #f0f9eb;
    border: 1px solid #67c23a;
    color: #67c23a;
}
.error{
    background: #fef0f0;
    border: 1px solid #f56c6c;
    color: #f56c6c;
}
</style>
