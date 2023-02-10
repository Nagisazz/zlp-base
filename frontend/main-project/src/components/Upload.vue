<template>
    <div class="zp-Upload-modal">
        <div class="trigger-container" @click="onUpload">
            <input
                class="hidden"
                ref="fileUploader"
                type="file"
                :multiple="multiple"
                :accept="acceptType"
                @change="fileChange"
            />
            <slot></slot>
        </div>
    </div>
</template>

<script>
import { computed, ref, toRefs } from "vue"

export default {
    name: 'Upload',
    props: {
        // 上传连接
        action: String,
        // 允许上传的格式，数组会被转为字符串，支持的字符串参照mdn文档
        accept: {
            type: [String, Array],
            default: "image/*"
        },
        // 上传限制
        limit: Object,
        // 是否允许多选
        multiple: {
            type: Boolean,
            default: false
        },
        // 上传前处理函数
        beforeUpload: Function,
    },

    emits: ["onSuccess"],

    setup (props, context) {
        // 原生的input dom
        const fileUploader = ref(null)

        // 校验accept格式
        const acceptType = computed(() => {
            if (typeof props.accept !== "string") {
                if (Array.isArray(props.accept)) {
                    return props.accept.join()
                } else {
                    console.error("accept接收字符串或数组，请输入正确的格式")
                }
            }
            return props.accept
        })

        // 组件状态
        const state = ({
            fileStatus: 'error', // 文件上传状态
            fileList: [], // 当前文件列表
            fileIndex: 0 // 文件处理索引（处理进度索引）
        })

        // 调用原生的upload
        const onUpload = () => {
            if (fileUploader.value) {
                fileUploader.value.click()
            }
        }

        // 自定义验证 处理beforeUploade。
        const customCheck = async (files) => {
            return new Promise((resolve, reject) => {
                // 判断有无改属性
                if (props.beforeUpload) {
                // 自定义验证的结果
                const result = props.beforeUpload(files)
                if (typeof result !== "boolean") {
                    reject(new Error("beforeUploadu应该返回一个布尔值"))
                }
                resolve(result)
                } else {
                resolve(true)
                }
            })
        }

        // 文件大小验证（组件自带的 通过limit.size定义的）
        const sizeCheck = (files) => {
            return new Promise((resolve, reject) => {
                const { size } = props.limit
                if (size) {
                let index = 0
                while (index < files.length) {
                    const file = files[index]
                    const fileSize = file.size / 1024
                    if (fileSize > size) {
                    const msg = `${file.name}文件大小超出${size}K，请重新调整！`
                    // Message.error(msg)
                    reject(new Error(msg))
                    }
                    index++
                }
                resolve(true)
                }
                resolve(true)
            })
        }

        // 文件数量验证（组件自带的 通过limit.maxFiles定义的）
        const lengthCheck = (files) => {
            return new Promise((resolve, reject) => {
                const { maxFiles } = props.limit
                if (maxFiles) {
                console.log(files.length, maxFiles)
                if (files.length > maxFiles) {
                    const msg = `文件数量不得超过${maxFiles}个`
                    // Message.error(msg)
                    reject(new Error(msg))
                }
                resolve(true)
                }
                resolve(true)
            })
        }

        
        // 上传前处理
        const fileChange = async (e) => {
            const target = e.target;
            const files = target.files;
            if (files && files.length) {
                // 上传前验证
                await customCheck(files)
                if (props.limit) {
                await sizeCheck(files)
                await lengthCheck(files)
                }

                // 本地 不上传到服务器时,验证完直接传回
                if (!props.action) {
                    context.emit("onSuccess", files)
                    state.fileList = files
                    state.fileStatus = 'success'
                } else {
                    // 否则依次开始上传
                    state.fileStatus = 'uploading'
                    state.fileList = files
                    state.fileIndex = 0
                    uploadFile(state.fileList[state.fileIndex])
                }
            }
        }

        // 上传文件
        const uploadFile = async (file) => {
            console.log(file);
            state.fileStatus = 'success'
            context.emit("onSuccess", state.fileList)
            state.fileList = []
        }

        return {
            acceptType,
            fileChange,
            onUpload,
            fileUploader,
            ...toRefs(state),
            ...toRefs(props)
        }
    }

}
</script>

<style scoped lang="scss">
.hidden{
    display: none;
}
</style>
