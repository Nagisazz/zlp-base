<template>
    <div class="sign-modal">
        <Dialog :title="signType === 'in' ? '登录' : '注册'" @closeDialog="onCancel()" @onConfirm="onConfirm()">
            <template v-slot:modalContent>
                <div class="modal-content">
                    <div class="modal-item">
                        <span>账号：</span>
                        <input type="text" v-model="loginId" placeholder="请输入账号" />
                    </div>
                    <div class="required" v-if="openVertify && !loginId">账号必填</div>
                    <div class="modal-item" style="margin-top: 20px">
                        <span>密码：</span>
                        <input type="text" v-model="password" placeholder="请输入密码" />
                    </div>
                    <div class="required" v-if="openVertify && !password">密码必填</div>
                    <div class="tip" v-if="signType === 'in'" @click="changeType('up')">首次使用？<span>点我注册</span></div>
                    <div class="tip" v-if="signType === 'up'" @click="changeType('in')">已有账号？<span>点我登录</span></div>
                </div>
            </template>

        </Dialog>
    </div>
</template>

<script>
import { registerApi, loginApi } from '@/api/index.ts';
import {useStore} from 'vuex';
import { initGlobalState } from 'qiankun';
import Dialog from '@/components/Dialog';

export default {
    name: 'Sign',
    components: { Dialog },

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
            openSignModal,
            store,
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

        // 关闭弹框
        onCancel(data) {
            this.openVertify = false;
            this.loginId = '';
            this.password = '';
            this.$emit('closeSignModal', data ? data : {isInfo: false})
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
                    password: this.password,
                    name: this.loginId
                };
                registerApi(param).then((res) => {
                    console.log(res);
                    if (res.status === 200) {
                        this.saveAuthorizedUser(res.data);
                        initGlobalState({
                            token: res.data.token,
                            refreshToken: res.data.refreshToken,
                        }); // 通知所有微应用去同步登录状态
                        this.onCancel({isInfo: true});
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
                        this.onCancel({isInfo: true});
                    }
                })
            }
        }
    },

}
</script>

<style scoped lang="scss">
.modal-content{
    font-size: 16px;
  .modal-item{
    display: flex;
    align-items: center;
    padding: 0 40px;
    box-sizing: border-box;
    span{
      flex: 0 0 50px;
    }
    input{
        width: 80%;
    }
  }
  .required{
    padding-left: 90px;
    font-size: 12px;
    color: red;
  }

  .tip{
    display: inline-block;
    font-size: 12px;
    margin-top: 20px;
    margin-left: 40px;
    span{
        color: #00adeb;
        cursor: pointer;
    }
  }
}
</style>
