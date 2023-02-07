<template>
    <div>
        <div class="header" :class="{'header-home': from === 'home'}">
            <div class="head-title">ZP NETWORK</div>
            <div class="head-info" v-if="store.state.Security.authorizedUser && store.state.Security.authorizedUser.token">
                <span class="sign-btn login" v-if="!this.$route.name" @click="toHome()">To Home</span>
                <img v-if="!haveUserInfo.avatarurl" src="http://1.15.87.105:11000/love/base/defaultImg.webp" />
                <img v-if="haveUserInfo.avatarurl" :src="getPreviewImg()" />
                <el-dropdown>
                    <span class="el-dropdown-link head-info-name">
                    {{haveUserInfo.name}}
                    </span>
                    <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item @click="onUpdateInfo()">更新信息</el-dropdown-item>
                        <el-dropdown-item @click="goOut()">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
            
            <div class="head-info" v-if="!store.state.Security.authorizedUser">
                <span class="sign-btn login" v-if="!this.$route.name" @click="toHome()">To Home</span>
                <span class="sign-btn login" @click="onLogin()">sign In</span>
                <span class="sign-btn register" @click="onRegister()">sign Up</span>
            </div>
        </div>
        <UpdateInfo v-if="updateDialog" @closeDialog="closeDialog"></UpdateInfo>
        <Sign v-if="store.state.Security.showSign" @closeSignModal="closeSignModal($event)"></Sign>
    </div>
</template>

<script>
import {useStore} from 'vuex';
import { useRouter } from 'vue-router';
import { initGlobalState } from 'qiankun';
import { getPreviewFile, getUserInfoApi } from '@/api/index';
import Sign from '@/components/Sign';
import UpdateInfo from '@/components/UpdateInfo';

export default {
    name: 'Header',
    components: { UpdateInfo, Sign },

    setup(){
		const router = useRouter()
        const onChangeRoute = (route) => {
            router.replace(route);
        }

        const store  = useStore();
        const saveAuthorizedUser = (res) => {
            store.commit("Security/LOGIN", res);
        }

        const openSignModal = (res) => {
            store.commit("Security/SHOWSIGN", res);
        }

        return {
            onChangeRoute,
            saveAuthorizedUser,
            openSignModal,
            store,
        }
	},

    props: {
        from: String
    },

    data() {
        return {
            haveUserInfo: {},
            updateDialog: false, // 是否打开更新用户信息开窗
        }
    },

    created() {
        if (this.store.state.Security.authorizedUser) {
            if (this.store.state.Security.authorizedUser && this.store.state.Security.authorizedUser.name) {
                this.haveUserInfo = this.store.state.Security.authorizedUser;
                
            } else {
                this.getUserInfoApi();
            }
        }
    },

    mounted() {
    },


    methods: {
        getUserInfoApi() {
            getUserInfoApi().then((res) => {
                if (res.status === 200) {
                    this.haveUserInfo = res.data;
                }
            })
        },

        // 关闭注册/登录弹框
        closeSignModal(data) {
            if (data.isInfo) {
                this.getUserInfoApi();
            } else {
                this.onChangeRoute('/');
            }
            this.openSignModal({ showSign: false });
        },

        // 打开更新用户信息弹框
        onUpdateInfo() {
            this.updateDialog = true;
        },

        // 关闭更新用户信息弹框
        closeDialog() {
            this.updateDialog = false;
            this.getUserInfoApi();
        },


        // 预览头像图片
        getPreviewImg() {
            return getPreviewFile('systemId=platform&fileId=' + this.haveUserInfo.avatarurl);
        },

        // 打开登录
        onLogin() {
            this.openSignModal({ showSign: true, signType: 'in' });
        },

        // 打开注册
        onRegister() {
            this.openSignModal({ showSign: true, signType: 'up' });
        },

        toHome() {
            this.onChangeRoute('/');
            setTimeout(() => {
                window.location.reload();
            }, 100);
        },

        // 退出登录
        goOut() {
            this.haveUserInfo.name = '';
            this.saveAuthorizedUser({});
            initGlobalState({
                token: '',
                refreshToken: '',
            });
            this.toHome();
        }
    }

}
</script>

<style scoped lang="scss">
.header{
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 80px 0 20px;
  box-sizing: border-box;
  background-image: url('http://1.15.87.105:11000/love/price/head-bg.png');
  background-size: 100% 100%;
}
.header-home{
    background: none;
    padding: 0 20%;
    .sign-btn{
        font-size: 16px;
        font-weight: bold;
    }
}


.head-title{
    color: #3f4586;
    font-size: 22px;
    font-weight: bold;
}
.head-info{
    cursor: pointer;
    color: #fff;
    height: 100%;
    display: flex;
    align-items: center;
    img{
        width: 40px;
        height: 40px;
        border-radius: 50%;
        margin-left: 40px;
    }
    .head-info-name, .zp-logout{
        font-size: 20px;
        margin-left: 10px;
        vertical-align: middle;
    }

    .sign-btn{
      display: inline-block;
      padding: 6px 15px;
      box-sizing: border-box;
      color: #3f4586;
      border: 1px solid #fff;
      border-radius: 8px;
      margin: 0 10px;
      font-weight: bold;
    }
}
</style>
