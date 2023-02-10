<template>
    <div class="zp-dialog-modal" v-if="isShow">
        <div class="zp-dialog zp-message-box">
            <div class="zp-dialog-head">
                <div class="dialog-title">提示</div>
                <i class="iconfont zp-fork" @click="closeMessageBox()"></i>
            </div>
            <div class="zp-dialog-content">
                {{content}}
            </div>
            
            <div class="zp-dialog-footer" v-if="type === 'info'">
                <div class="zp-btn" @click="closeMessageBox()">取消</div>
                <div class="zp-btn zp-primary" @click="onConfirm()">确定</div>
            </div>
            <div class="zp-dialog-footer" v-if="type === 'confirm'">
                <div class="zp-btn zp-primary" @click="onConfirm()">确定</div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from "vue";

export default {
    name: 'MessageBox',
    props: {
        content: String,
        type: String,
        remove: Function,
        callback: Function,
    },

    setup (props) {
        let isShow = ref(true);
        const closeMessageBox = () => {
            isShow.value = false;
            props.remove();
        }

        const onConfirm = () => {
            isShow.value = false;
            props.remove();
            props.callback('confirm');
        }
        return {
            isShow,
            closeMessageBox,
            onConfirm
        }
    }


}
</script>

<style scoped lang="scss">
.zp-message-box{
    transform: translateY(-20%);
}
</style>
