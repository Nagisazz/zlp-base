<template>
    <div class="layout">
      <div class="header" v-if="store.state.IsShowHead.isShowHead">
        <div class="head-title">ZP NETWORK</div>
        <div class="head-info" v-if="haveUserInfo.accountName">
          <span class="sign-btn login" v-if="!this.$route.name" @click="onChangeRoute('/')">To Home</span>
          <img src="http://1.15.87.105:11000/love/defaultImg.webp" />
          <el-dropdown>
            <span class="el-dropdown-link head-info-name">
              {{haveUserInfo.accountName}}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="onUpdateInfo()">更新信息</el-dropdown-item>
                <el-dropdown-item @click="goOut()">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        
        <div class="head-info" v-if="!haveUserInfo.accountName">
          <span class="sign-btn login" v-if="!this.$route.name" @click="onChangeRoute('/')">To Home</span>
          <span class="sign-btn login" @click="onLogin()">sign In</span>
          <span class="sign-btn register" @click="onRegister()">sign Up</span>
        </div>
      </div>
      <div class="container" :style="containerH">
        <div v-show="!$route.name" id="micro"></div>
        <div v-show="$route.name" class="other">
          <router-view />
        </div>
      </div>

      <UpdateInfo v-if="updateDialog" @closeDialog="closeDialog"></UpdateInfo>
      <Sign v-if="store.state.Security.showSign" @closeSignModal="closeSignModal"></Sign>
    </div>
</template>

<script>
import { useRouter } from 'vue-router';
import { getUserInfoApi } from '@/api/index.ts';
import {useStore} from 'vuex';
import { initGlobalState } from 'qiankun';
import UpdateInfo from '@/components/UpdateInfo';
import Sign from '@/components/Sign';

export default {
  name: 'Layout',
  components: { UpdateInfo, Sign },

  setup() {
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

  data() {
    return {
      haveUserInfo: {
        accountName: '',
      },
      containerH: 'height: calc(100% - 60px)',
      updateDialog: false, // 是否打开更新用户信息开窗
    }
  },

  created() {
    this.getUserInfoApi();
  },

  mounted() {
    console.log(this.$route, this.store.state.IsShowHead);
    if (this.store.state.IsShowHead.isShowHead) {
      this.containerH = '100%';
    }
  },

  methods: {
    getUserInfoApi() {
      getUserInfoApi().then((res) => {
        if (res.status === 200) {
          this.haveUserInfo.accountName = res.data.name;
        }
      })
    },

    // 打开登录
    onLogin() {
      this.openSignModal({ showSign: true, signType: 'in' });
    },

    // 打开注册
    onRegister() {
      this.openSignModal({ showSign: true, signType: 'up' });
    },

    // 关闭注册/登录弹框
    closeSignModal() {
      this.getUserInfoApi();
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

    // 退出登录
    goOut() {
      this.haveUserInfo.accountName = '';
      this.saveAuthorizedUser({});
      initGlobalState({
          token: '',
          refreshToken: '',
      });
    }

    

  }

  

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.layout{
  width: 100%;
  height: 100vh;
  padding: 0;
  margin: 0;
}
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
}

.container{
  width: 100%;
  height: calc(100% - 60px)
}

#micro, .other{
  height: 100%;
}
</style>


