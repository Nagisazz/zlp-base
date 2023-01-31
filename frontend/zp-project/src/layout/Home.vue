<template>
  <div class="zp-layout">
    <ul class="zp-bg-bubbles">
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
      <li></li>
    </ul>
    <div class="zp-nav-user" ref="user">
      <div class="user line">
        <img class="user-img" :src="user.info.imgUrl" alt="">
        <div>
          <div class="user-name">{{user.info.zz}}<span class="user-tag">{{user.info.zzTag}}</span></div>
          <div class="user-name">{{user.info.cc}}<span class="user-tag">{{user.info.ccTag}}</span> </div>
        </div>
      </div>
      <div class="zp-navs zp-more">
          <div @click="showMore()" v-if="!isShowMore">
            查看更多
          </div>
          <div @click="hideMore()"  v-if="isShowMore">
            收起
          </div>
      </div>
      <div class="more-user" v-if="isShowMore">
        <div class="user-contact" v-for="(item, index) in user.contacts" :key="index">
          <i :class="['iconfont', item.icon]"></i>
          <div class="contact-num">
            <div class="num" :class="'copy'+item.icon+item.zzId" @click="onclip(item.icon+item.zzId)" :data-clipboard-text="item.zz">{{item.zz}}</div>
            <div class="num" :class="'copy'+item.icon+item.ccId" @click="onclip(item.icon+item.ccId)" :data-clipboard-text="item.cc">{{item.cc}}</div>
          </div>
        </div>
      </div>
    </div>
    <div class="zp-content" :style="{'height': contentHeight}">
      <div class="zp-nav-box">
        <div class="zp-nav-title">
          {{currentNav.desc}}
        </div>
        <div class="zp-navs">
          <div v-for="(nav, index) in navs" :key="index" class="zp-nav" 
            @click="onNav(nav)" 
            :style="{ color: nav.name === currentNav.name && nav.name !== '返回' ? '#ffdb70' : '#fff' }">
            {{nav.name}}
          </div>
        </div>
      </div>
      <div class="zp-container" :style="{'height': containerHeight}">
        <router-view @workDetail="openWorkDetail($event)"/>
      </div>
    </div>
  </div>

</template>


<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import ClipboardJS from 'clipboard';
import { getHomeData } from '../api';

@Component({
  name:'Home',
})
export default class Home extends Vue {

  public loginInfo: any;
  user: any = {
    "info": {
      "zz": "朱晟喆 zz",
      "zzTag": "后端工程师",
      "cc": "陈黄平 cc",
      "ccTag": "前端工程师",
      "imgUrl": "http://1.15.87.105:11000/love/zp/home/user.webp"
    },
    "contacts": [
      { "icon": "zp-email", "zzId": "1", "zz": "1234@qq.com", "ccId": "2", "cc": "2369092439@qq.com" },
      { "icon": "zp-dianhua", "zzId": "3", "zz": "18260059387", "ccId": "4", "cc": "18651905053" },
      { "icon": "zp-weixin1", "zzId": "5", "zz": "NagisaZC", "ccId": "6", "cc": "18651905053" }
    ]
  };
  originNavs: any[] = [
    { "name": "作品", "link": "/work", "desc": "Work" },
    { "name": "文章", "link": "/article", "desc": "Article" },
    { "name": "关于", "link": "/about", "desc": "About" },
  ];
  navs: any[] = this.originNavs;
  currentNav: any = this.navs[0];
  clipboard: any;
  isShowMore: boolean = true;
  contentHeight = '88vh';
  containerHeight = 'calc(88vh - 74px)';

  created() {
    console.log(this.$route.query);
    getHomeData().then((res) => {
      console.log(res);
      this.originNavs = res.originNavs;
      this.user = res.user;
      if (this.$route.query.id) {
        this.navs = [{ 'name': '返回', 'link': '/work', 'desc': sessionStorage.getItem('title') }]
        this.currentNav = this.navs[0];
      } else {
        this.navs = this.originNavs;
        this.currentNav = this.navs[0];
      }

      this.navs.map((obj) => {
        if (this.$route.name) {
          if (obj.desc.toUpperCase() === this.$route.name.toUpperCase()) {
            this.currentNav = obj;
          }
        }
      })
    })
  }

  mounted() {
    this.calcH();
  }

  calcH() {
    const user = this.$refs.user as HTMLBRElement;
    console.log(user.offsetHeight);
    if (document.body.clientWidth >= 800) {
      this.contentHeight = '88vh';
    } else {
      this.contentHeight = (document.body.clientHeight - user.offsetHeight - 40) + 'px';
      this.containerHeight = (document.body.clientHeight - user.offsetHeight - 40) - 74 + 'px';
    }
    
    console.log(this.containerHeight);
  }

  // destroyed() {
  //   this.clipboard.destroy();
  // }

  onNav(nav: any) {
    if (nav.name === '返回') {
      this.navs = this.originNavs;
      this.currentNav = this.navs[0];
      this.$router.go(-1);
    } else {
      this.$router.push({path: nav.link});
      this.currentNav = nav;
    }
  }

  // 查看作品详情
  openWorkDetail(e: any) {
    console.log(e.title);
    sessionStorage.setItem('title', e.title);
    this.navs = [{ 'name': '返回', 'link': '/work', 'desc': e.title }]
    this.currentNav = this.navs[0];
    this.$router.push({path: '/work/detail', query: { id: e.id}});
  }

  // 复制内容
  onclip(label: any) {
    const _this = this
    this.clipboard = new ClipboardJS('.copy' + label);
    console.log('.copy' + label);
    // this.clipboard.on('success',(e: ClipboardJS.Event) => {
    //     e.clearSelection();
    //     _this.$message({
    //         message:'已复制到粘贴板',
    //         type:'success'
    //     })
    // });
    // this.clipboard.on('error', () => {
    //     _this.$message({
    //         message:'复制错误，请重新复制！',
    //         type:'error'
    //     })
    // });
  }

  showMore() {
    this.isShowMore = true;
    this.calcH();
  }

  hideMore() {
    this.isShowMore = false;
    this.calcH();
  }


}

</script>

<style scoped lang="less">
.user{
  display: flex;
  flex-direction: column;
  text-align: center;
  margin-bottom: 20px;
  margin-top: 40px;
}
.line::after{
  content: '';
  width: 90%;
  height: 1px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.7);
}
.user-img{
  width: 120px;
  height: 120px;
  border-radius: 20px;
  background: #333;
  margin: 0 auto;
  margin-bottom: 20px;
}
.user-name{
  margin-bottom: 20px;
}
.user-tag{
  display: inline-block;
  width: auto;
  line-height: 20px;
  border-radius: 6px;
  background: #82acfe;
  padding: 2px 10px;
  font-size: 12px;
  vertical-align: middle;
  margin-left: 10px;
}
.user-contact{
  width: 100%;
}
.user-contact .iconfont{
  font-size: 26px;
  vertical-align: middle;
}
.contact-num{
  display: inline-block;
  width: 160px;
  text-align: left;
  padding-left: 10px;
  vertical-align: middle;
}
.user-contact .num{
  margin: 15px auto;
  cursor: pointer;
}


@media (max-width: 950px)  { // 小于950的设备
  .user-contact .num{
    margin: 8px auto;
  }
}
@media (max-width: 800px)  { // 小于800的设备
  .user{
    display: flex;
    flex-direction: row;
    padding: 0;
    width: 300px;
    height: 120px;
    align-items: center;
    margin-top: 20px;
  }
  .more-user{
    display: flex;
  }
  .user-img{
    margin-bottom: 0;
  }
  .user-name{
    margin: 10px auto;
  }
  .line::after{
    width: 0;
    height: 0;
  }
}
</style>