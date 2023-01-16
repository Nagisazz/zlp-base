const URL_BASE = process.env.NODE_ENV ===
'development' ? 'http://192.168.0.111:7100/' : 'https://love.nagisazlp.cn';

export default {
    URL:{
        refreshToken: 'http://192.168.0.111:7000/' + 'platform/account/refresh',
        // 上传文件
        uploadFile: 'http://192.168.0.111:7000/' + 'platform/file/upload',
        // 获取文件流
        getFile: 'http://192.168.0.111:7000/' + 'platform/file/stream',
        // 获取文件信息
        getFileInfo: 'http://192.168.0.111:7000/' + 'platform/file/get',
        // 获取缩略图
        getPreviewFile: 'http://192.168.0.111:7000/' + 'platform/file/preview',
        // 删除文件
        delFile: 'http://192.168.0.111:7000/' + 'platform/file/delete',

        // 文件
        // 创建文件
        createFile: URL_BASE + 'share/fileObj/createFile',
        // 创建纯文本文件
        createTxt: URL_BASE + 'share/fileObj/createTxt',
        // 更新纯文本/文件
        updateFile: URL_BASE + 'share/fileObj/update',
        // 删除文件
        deleteFile: URL_BASE + 'share/fileObj/delete',
        // 创建文件夹
        createFolder: URL_BASE + 'share/folder/create',
        // 更新文件夹
        updateFolder: URL_BASE + 'share/folder/update',
        // 删除文件夹
        deleteFolder: URL_BASE + 'share/folder/delete',
        // 根据父id查找目录列表 get
        findList: URL_BASE + 'share/path/list',
        // 生成单个文件分享码
        singleShareCode: URL_BASE + 'share/send/single',
        // 生成多个文件分享码 
        multiShareCode: URL_BASE + 'share/send/multi',
        // 预览分享目录
        previewShare: URL_BASE + 'share/receive/preview', 
        // 保存分享的文件
        saveShareFile: URL_BASE + 'share/receive/save',
    }
}