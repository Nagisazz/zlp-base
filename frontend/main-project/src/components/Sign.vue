<template>
    <div class="sign-modal">
        <el-dialog
            v-model="dialogVisible"
            :title="signType === 'in' ? '登录' : '注册'"
            width="50%"
            :before-close="handleClose">
            <div class="modal-content">
                <div class="modal-item">
                    <span>账号：</span>
                    <el-input v-model="loginId" placeholder="请输入账号" />
                </div>
                <div class="required" v-if="openVertify && !loginId">账号必填</div>
                <div class="modal-item" style="margin-top: 20px">
                    <span>密码：</span>
                    <el-input v-model="password" placeholder="请输入密码" />
                </div>
                <div class="required" v-if="openVertify && !password">密码必填</div>
                <div class="tip" v-if="signType === 'in'" @click="changeType('up')">首次使用？<span>点我注册</span></div>
                <div class="tip" v-if="signType === 'up'" @click="changeType('in')">已有账号？<span>点我登陆</span></div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="onCancel()">取消</el-button>
                    <el-button type="primary" @click="onConfirm()">确定</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script>
import { registerApi, loginApi } from '@/api/index.ts';
import {useStore} from 'vuex';
import { initGlobalState } from 'qiankun';
import { ElMessage } from 'element-plus';

export default {
    name: 'Sign',

    setup(){
		const store = useStore();
        const saveAuthorizedUser = (res) => {
            store.commit("Security/LOGIN", res);
        }
        const openSignModal = (res) => {
            store.commit("Security/SHOWSIGN", res);
        }
        console.log(store);
        return {
            saveAuthorizedUser,
            store,
            openSignModal
        }
	},


    data() {
        return {
            dialogVisible: true,
            loginId: '',
            password: '',
            openVertify: false,
            signType: this.store.state.Security.signType,
        }
    },

    created() {
    },

    mounted() {
        console.log(this.$route);
    },

    methods: {

        changeType(type) {
            this.signType = type;
            this.loginId = '';
            this.password = '';
            this.openSignModal({ showSign: true, signType: type });
        },

        handleClose(done) {
            console.log(done, '关闭弹框');
            this.openVertify = false;
            this.loginId = '';
            this.password = '';
            this.openSignModal({ showSign: false });
        },

        // 取消按钮
        onCancel() {
            this.openVertify = false;
            this.loginId = '';
            this.password = '';
            this.openSignModal({ showSign: false });
        },

        // 确认按钮
        onConfirm() {
            this.openVertify = true;
            if (!this.loginId || !this.password) {
                return;
            }
            if (this.signType === 'up') { // 注册
                const param = {
                    type: "1", // 1pc 2wx
                    systemId: "platform", // 系统标识
                    loginId: this.loginId,
                    password: this.password
                };
                registerApi(param).then((res) => {
                    console.log(res);
                    if (res.status === 200) {
                        this.saveAuthorizedUser(res.data);
                        initGlobalState({
                            token: res.data.token,
                            refreshToken: res.data.refreshToken,
                        }); // 通知所有微应用去同步登录状态
                        this.onCancel();
                    } else {
                        ElMessage({
                            message: res.message,
                            type: 'warning',
                        })
                    }
                })
            } else { // 登录
                const param = {
                    systemId: "platform", // 系统标识
                    loginId: this.loginId,
                    password: this.password
                };
                loginApi(param).then((res) => {
                    console.log(res);
                    if (res.status === 200) {
                        this.saveAuthorizedUser(res.data);
                        initGlobalState({
                            token: res.data.token,
                            refreshToken: res.data.refreshToken,
                        });
                        this.onCancel();
                    } else {
                        ElMessage({
                            message: res.message,
                            type: 'warning',
                        })
                    }
                })
            }
        }
    },

}
</script>

<style scoped lang="scss">
.modal-content{
  .modal-item{
    display: flex;
    align-items: center;
    padding: 0 40px;
    box-sizing: border-box;
    span{
      flex: 0 0 50px;
    }
  }
  .required{
    padding-left: 90px;
    font-size: 12px;
    color: red;
  }

  .tip{
    display: inline-block;
    transform: translateY(65px);
    span{
        color: #00adeb;
        cursor: pointer;
    }
  }
}
</style>
