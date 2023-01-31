// const URL_BASE_PLATFORM = 'http://192.168.0.116:7000';
const URL_BASE_PLATFORM = 'http://platform.zlpnet.cn';
// const URL_BASE_SHARE = 'http://192.168.0.116:8600';
const URL_BASE_SHARE = 'http://share.zlpnet.cn';

export default {
    URL:{
        refreshToken: URL_BASE_PLATFORM + '/platform/account/refresh',
        // 上传文件
        uploadFile: URL_BASE_PLATFORM + '/platform/file/upload',
        // 获取文件流
        getFile: URL_BASE_PLATFORM + '/platform/file/stream',
        // 获取文件信息
        getFileInfo: URL_BASE_PLATFORM + '/platform/file/get',
        // 获取缩略图
        getPreviewFile: URL_BASE_PLATFORM + '/platform/file/preview',
        // 删除文件
        delFile: URL_BASE_PLATFORM + '/platform/file/delete',

        // 文件
        // 创建文件
        createFile: URL_BASE_SHARE + '/share/fileObj/createFile',
        // 创建纯文本文件
        createTxt: URL_BASE_SHARE + '/share/fileObj/createTxt',
        // 更新纯文本/文件
        updateFile: URL_BASE_SHARE + '/share/fileObj/update',
        // 删除文件
        deleteFile: URL_BASE_SHARE + '/share/fileObj/delete',
        // 创建文件夹
        createFolder: URL_BASE_SHARE + '/share/folder/create',
        // 更新文件夹
        updateFolder: URL_BASE_SHARE + '/share/folder/update',
        // 删除文件夹
        deleteFolder: URL_BASE_SHARE + '/share/folder/delete',
        // 根据父id查找目录列表 get
        findList: URL_BASE_SHARE + '/share/path/list',
        // 生成单个文件分享码
        singleShareCode: URL_BASE_SHARE + '/share/send/single',
        // 生成多个文件分享码 
        multiShareCode: URL_BASE_SHARE + '/share/send/multi',
        // 预览分享目录
        previewShare: URL_BASE_SHARE + '/share/receive/preview', 
        // 保存分享的文件
        saveShareFile: URL_BASE_SHARE + '/share/receive/save',
    }
}