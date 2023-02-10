<template>
    <div class="zp-dialog-modal">
        <div class="zp-dialog">
            <div class="zp-dialog-head">
                <div class="dialog-title">{{title}}</div>
                <i class="iconfont zp-fork" @click="closeDialog()"></i>
            </div>
            <div class="zp-dialog-content">
                <slot name="modalContent"></slot>
            </div>
            <div class="zp-dialog-footer">
                <slot name="modalFooter">
                    <div class="zp-btn" @click="closeDialog()">取消</div>
                    <div class="zp-btn zp-primary" @click="onConfirm()">确定</div>
                </slot>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Dialog',
    props: {
        title: String
    },

    data() {
        return {
        }
    },

    created() {
    },

    mounted() {
    },

    methods: {
        // 关闭开窗事件
        closeDialog() {
            this.$emit('closeDialog');
        },

        // 开窗确定事件
        onConfirm() {
            this.$emit('onConfirm');
        }
    },

}
</script>

<style scoped lang="scss">
</style>
