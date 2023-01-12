<template>
  <div class="zp-layout Article">
    <div class="zp-layout Home">
      <div class="zp-nav-box">
        <!-- <div class="zp-nav-left">朱陈之家</div> -->
        <div class="zp-nav-right">
          <div v-for="(nav, index) in navs" :key="index" class="zp-nav">
            <router-link :to="nav.link">{{nav.name}}</router-link>
          </div>
        </div>
      </div>
      <div class="content">
        <div class="left">
          左侧
        </div>
        <div class="right">
          <el-button type="primary" class="abb-btn" @click="onAdd()"><i class="el-icon-plus"></i>新增</el-button>
          <div class="screen">
            <div class="screen-list" v-for="(screen, index) in screenList" :key="index"
              @click="showLargeScreen(screen)">
              <div class="icon-bg">
                <i class="iconfont zp-edit" @click="onEdit($event, screen)"></i>
                <i class="iconfont zp-fork" @click="onDel($event, screen)"></i>
              </div>
              <img :src="screen.url" alt="">
              <div class="title zp-overflow">{{screen.title}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog title="配置看板" :visible.sync="dialogVisible" 
      width="90%" top="8vh" :close-on-click-modal="false" :close-on-press-escape="false">
      <div v-if="dialogVisible">
        <ConfigBoard :type="type" :screenData="screenData" @confirm="onConfirm()" @cancel="dialogVisible = false"></ConfigBoard>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import ConfigBoard from '@/family/ConfigBoard.vue';
import screen from '@/family/screen';

@Component({
  name:'Family',
  components: { ConfigBoard },
})
export default class Family extends Vue {

    navs: any[] = [
        { 'name': '首页', 'link': '/' },
        { 'name': '文章', 'link': {name:'resume', params: { user : 'zz' }} },
        { 'name': '简历', 'link': '/resume' },
        { 'name': '家庭', 'link': '/family' },
    ];
    dialogVisible = false;
    screenList: any = [];
    screenData: any = {};
    type: string=  'add';

    created() {
      this.screenList = screen.list;
      console.log(this.screenList);
    }

    // 添加大屏
    onAdd() {
      this.dialogVisible = true;
      this.type = 'add';
    }

    // 确定按钮事件
    onConfirm() {
      
    }

    // 进入展示大屏
    showLargeScreen(item: any) {
      this.$router.push({name:'largeScreen', query: { set_id : item.set_id }})
    }

    onDel(e: any, item: any) {
      e.stopPropagation();
      this.screenList = this.screenList.filter((obj: any) => {
        return obj.set_id !== item.set_id;
      });
    }

    onEdit(e: any, item: any) {
      e.stopPropagation();
      this.type = 'edit';
      this.dialogVisible = true;
      this.screenData = item;
    }



}

</script>

<style scoped lang="less">
.content{
  margin-top: 60px;
  width: 100%;
  height: calc(100% - 80px);
  text-align: center;
  font-size: 14px;
  display: flex;
}
.left{
  flex: 1;
}

.right{
  flex: 0 0 300px;
  text-align: center;
  border-left: 1px solid #C6C6C6;
  .abb-btn{
    margin-top: 10px;

    .el-icon-plus{
      margin-right: 4px;
    }
  }
}
.screen{
  height: 100%;
  overflow: auto;
}
.screen-list{
  width: 220px;
  height: 162px;
  border-radius: 8px;
  border: 1px solid #C6C6C6;
  box-shadow: 0px 1px 4px 0px rgba(0, 0, 0, 0.3);
  margin: 10px auto;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  position: relative;

  img{
    width: 100%;
    height: 130px;
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
  }
  .title{
    height: 32px;
    line-height: 32px;
    text-align: left;
    padding: 0 10px;
    box-sizing: border-box;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }

  .icon-bg{
    width: 100%;
    height: 24px;
    line-height: 24px;
    text-align: right;
    background: rgba(0, 0, 0, 0.7);
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
    padding: 0 10px;
    box-sizing: border-box;
    display: none;
    position: absolute;
    top: 0;
    left: 0;
    .iconfont{
      margin-left: 10px;
      color: #fff;
    }
  }
}
.screen-list:hover{
  .icon-bg{
    display: block;
  }
}
</style>