<template>
    <div class="updateInfo">
        <Dialog title="更新用户信息" @closeDialog="onCancel()">
            <template v-slot:modalContent>
                <div class="modal-content">
                    <div class="info-item">
                        <img class="default-img" v-if="!haveUserInfo.avatarurl" src="http://1.15.87.105:11000/love/base/defaultImg.webp" />
                        <img class="default-img" v-if="haveUserInfo.avatarurl" :src="getPreviewImg()" />
                        <Upload @onSuccess="changeFile" v-if="isEdit" style="display: inline-block;">
                            <i class="iconfont zp-shangchuanzhaopian"  title="点击更换头像"></i>
                        </Upload>
                    </div>
                    <div class="info-item">
                        <span class="info-title">账号：</span>
                        <div class="info-content">{{haveUserInfo.loginId}}</div>
                    </div>
                    <div class="info-item">
                        <span class="info-title">昵称：</span>
                        <div class="info-content" v-if="!isEdit">{{haveUserInfo.name}}</div>
                        <div class="info-content" v-if="isEdit">
                            <input type="text" v-model="haveUserInfo.name" />
                        </div>
                    </div>
                    <div class="info-item">
                        <span class="info-title">密码：</span>
                        <div class="info-content">
                            <span v-if="!isEditPassword"> ******  <i class="iconfont zp-edit" @click="onEditPassword()"></i> </span>
                            <span v-if="isEditPassword"> 
                                <input type="text" v-model="haveUserInfo.password" placeholder="请输入原密码"/>
                            </span>
                        </div>
                        <div v-if="isEditPassword" class="info-content newpass">
                            <input type="text" v-model="haveUserInfo.newPassword" placeholder="请输入新密码"/>
                        </div>
                        <div v-if="isEditPassword" class="info-content newpass">
                            <div class="zp-btn" @click="onCancelPassword()">取消</div>
                            <div class="zp-btn zp-primary" @click="onSavePassword()">保存</div>
                        </div>
                    </div>
                    <div class="info-item">
                        <span class="info-title">手机号：</span>
                        <div class="info-content" v-if="!isEdit">{{haveUserInfo.phone || '-'}}</div>
                        <div class="info-content" v-if="isEdit">
                            <input type="text" v-model="haveUserInfo.phone" />
                        </div>
                    </div>
                    <div class="info-item">
                        <span class="info-title">邮箱：</span>
                        <div class="info-content" v-if="!isEdit">{{haveUserInfo.mail || '-'}}</div>
                        <div class="info-content" v-if="isEdit">
                            <input type="text" v-model="haveUserInfo.mail" />
                        </div>
                    </div>
                    <div class="info-item">
                        <span class="info-title">最后登录系统：</span>
                        <div class="info-content" >{{handleSystem(haveUserInfo.lastSystem)}}</div>
                    </div>
                    <div class="info-item">
                        <span class="info-title">最后登录时间：</span>
                        <div class="info-content" >{{handleTime(haveUserInfo.lastLoginTime)}}</div>
                    </div>
                    <div class="info-item">
                        <span class="info-title">最后登录IP：</span>
                        <div class="info-content" >{{haveUserInfo.lastIp || '-'}}</div>
                    </div>
                </div>
            </template>

            <template #modalFooter>
                <span class="dialog-footer">
                    <div class="zp-btn" @click="onCancel()">取消</div>
                    <div class="zp-btn zp-primary" v-if="!isEdit" @click="onIsEdit()">编辑</div>
                    <div class="zp-btn zp-primary" v-if="isEdit" @click="onConfirm()">确定</div>
                </span>
            </template>
        </Dialog>
    </div>
</template>
  
<script>
import {useStore} from 'vuex';
import { getUserInfoApi, updateUserInfoApi, updateUserPassword, uploadFile, getPreviewFile } from '@/api/index';
import { initGlobalState } from 'qiankun';
import Dialog from '@/components/Dialog';
import Upload from '@/components/Upload';
import { app } from "@/main";

export default {
    name: 'UpdateInfo',
    components: { Dialog, Upload },

    setup() {
        const store = useStore();
        const saveAuthorizedUser = (res) => {
            store.commit("Security/LOGIN", res);
        }

      return {
        saveAuthorizedUser,
        store,
      }
    },

    data() {
        return {
            updateDialog: true,
            haveUserInfo: {
                avatarurl: '',
                name: '',
                password: '',
                newPassword: '',
                phone: '',
                mail: '',
                lastSystem: '',
                lastLoginTime: '',
                lastIp: ''
            },
            isEdit: false,
            isEditPassword: false,
        }
    },

    created() {

    },

    mounted() {
        getUserInfoApi({}).then((res) => {
            if (res.status === 200) {
                this.haveUserInfo = Object.assign(this.haveUserInfo, res.data);
            }
        });
    },

    methods: {
        // 编辑除密码以外的信息
        onIsEdit() {
            this.isEdit = true;
        },

        // 编辑密码
        onEditPassword() {
            this.isEditPassword = true;
        },

        // 取消密码修改
        onCancelPassword() {
            this.isEditPassword = false;
        },

        // 保存密码
        onSavePassword() {
            const param = {
                oriPassword: this.haveUserInfo.password,
                password: this.haveUserInfo.newPassword,
            };
            updateUserPassword(param).then((res) => {
                if (res.status === 200) {
                    this.isEditPassword = false;
                    this.saveAuthorizedUser(res.data);
                    initGlobalState({
                        token: res.data.token,
                        refreshToken: res.data.refreshToken,
                    }); // 通知所有微应用去同步登录状态
                    app.config.globalProperties.$Message.show({content: '密码更新成功', type: 'success'});
                } else {
                    app.config.globalProperties.$Message.show({content: '密码更新失败', type: 'error'});
                }
            });
        },

        // 上传文件
        changeFile(file) {
            console.log(file);
            const formData = new FormData();
            formData.append('file', file[0]);
            uploadFile(formData, 'systemId=platform&name=' + file.name).then((res) => {
                if (res.status === 200) {
                    this.haveUserInfo.avatarurl = res.data.id;
                }
            });
        },

        // 预览头像图片
        getPreviewImg() {
            return getPreviewFile('systemId=platform&fileId=' + this.haveUserInfo.avatarurl);
        },

        // 取消按钮
        onCancel() {
            this.$emit('closeDialog', false)
        },

        // 确认按钮
        onConfirm() {
            const param = {
                name: this.haveUserInfo.name,
                phone: this.haveUserInfo.phone,
                mail: this.haveUserInfo.mail,
                avatarurl: this.haveUserInfo.avatarurl
            };
            updateUserInfoApi(param).then((res) => {
                if (res.status === 200) {
                    this.$emit('closeDialog', false);
                    this.saveAuthorizedUser(res.data);
                    initGlobalState({
                        token: res.data.token,
                        refreshToken: res.data.refreshToken,
                    }); // 通知所有微应用去同步登录状态
                    app.config.globalProperties.$Message.show({content: '更新成功', type: 'success'});
                } else {
                    app.config.globalProperties.$Message.show({content: '更新失败', type: 'error'});
                }
            });
        },

        handleTime(time) {
            if (time) {
                return time.replace('T', ' ');
            } else {
                return '-';
            }
        },

        handleSystem(system) {
            if (system === 'platform') {
                return '平台';
            } else if (system === 'zp') {
                return '博客';
            } else if (system === 'price') {
                return '商品库价格计算';
            } else if (system === 'file') {
                return '享到-跨端文件共享及协作';
            } else if (system === 'fund') {
                return '基金投资辅助决策';
            } else {
                return system || '-';
            }
        }

    }

}
 </script>
  
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.updateInfo{
  width: 100%;
}
.modal-content{
    width: 100%;
    text-align: center;
    font-size: 14px;
    .info-item{
        width: 100%;
        margin-bottom: 20px;
        .info-title{
            width: 50%;
            text-align: right;
            font-weight: bold;
            display: inline-block;
        }
        .info-content{
            width: 50%;
            display: inline-block;
            text-align: left;
        }
    }
    .default-img{
        width: 100px;
        height: 100px;
        border-radius: 50%;
        border: 1px solid #d9d9d9;
        vertical-align: bottom;
    }

    .iconfont{
        cursor: pointer;
    }

    .newpass{
        margin: 10px 10px 0 50%;
    }

    .elplus-input{
        width: 50%;
    }
}

</style>
  