<template>
    <div class="layout">
      <div class="header">
        <div class="head-title">ZP NETWORK</div>
        <div class="head-info" v-if="haveUserInfo.accountName">
          <span class="sign-btn login" @click="onChangeRoute('/home')">To Home</span>
          <img src="http://1.15.87.105:11000/love/zp/home/user.webp" />
          <el-dropdown>
            <span class="el-dropdown-link head-info-name">
              朱朱
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="updateInfo()">更新信息</el-dropdown-item>
                <el-dropdown-item disabled>Action 4</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        
        <div class="head-info" v-if="!haveUserInfo.accountName">
          <span class="sign-btn login" @click="onChangeRoute('/home')">To Home</span>
          <span class="sign-btn login" @click="onLogin()">sign In</span>
          <span class="sign-btn register" @click="onRegister()">sign Up</span>
        </div>
      </div>
      <div class="container">
        <div v-show="!$route.name" id="micro"></div>
        <div v-show="$route.name" class="other">
          <router-view />
        </div>
      </div>
    </div>
</template>

<script>
import { useRouter } from 'vue-router';
// import { getUserInfoApi } from '@/api/index.ts';
import {useStore} from 'vuex';

export default {
  name: 'Layout',

  setup() {
    const router = useRouter()
    const onChangeRoute = (route) => {
      router.replace(route);
    }

    const store  = useStore();
    const openSignModal = (res) => {
      store.commit("Security/SHOWSIGN", res);
    }

    return {
      onChangeRoute,
      openSignModal,
      store,
    }
  },

  data() {
    return {
      haveUserInfo: {
        accountName: '',
        id: ''
      },
    }
  },

  created() {
    // getUserInfoApi().then((res) => {
    //   console.log(res);
    // });
  },

  mounted() {
    console.log(this.$route);
  },

  methods: {
    // 打开登录
    onLogin() {
      this.openSignModal({ showSign: true, signType: 'in' });
    },

    // 打开注册
    onRegister() {
      this.openSignModal({ showSign: true, signType: 'up' });
    },

    // 关闭登录，注册弹框
    closeModal() {
      this.openSignModal({ showSign: false });
    },

    // 打开更新用户信息弹框
    updateInfo() {
      
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
  padding: 0 20px;
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
    }
    .head-info-name, .zp-logout{
        font-size: 20px;
        margin-left: 20px;
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
  height: calc(100% - 60px);
}

#micro, .other{
  height: 100%;
}
</style>


