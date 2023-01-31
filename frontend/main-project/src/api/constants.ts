// const URL_BASE = 'http://192.168.0.116:7000';
const URL_BASE = 'http://platform.zlpnet.cn';
export default{
    URL:{
        register: URL_BASE + '/platform/sso/register',
        login: URL_BASE + '/platform/sso/login',
        getUserInfo: URL_BASE + '/platform/account/info',
        updateInfo: URL_BASE + '/platform/account/update',
        updatePassword: URL_BASE + '/platform/account/updatePassword',
        refreshToken: URL_BASE + '/platform/account/refresh',
        // 上传文件
        uploadFile: URL_BASE + '/platform/file/upload',
        // 获取文件流
        getFile: URL_BASE + '/platform/file/stream',
        // 获取文件信息
        getFileInfo: URL_BASE + '/platform/file/get',
        // 获取缩略图
        getPreviewFile: URL_BASE + '/platform/file/preview',
        // 删除文件
        delFile: URL_BASE + '/platform/file/delete',
    }
}

