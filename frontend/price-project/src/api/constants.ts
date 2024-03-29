const URL_BASE_PLATFORM = 'http://platform.zlpnet.cn';
const URL_BASE_GOODS = 'http://goods.zlpnet.cn';

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
        
        // 猜价
        goodTotal: URL_BASE_GOODS + '/goods/guess/total',
        goodSearch: URL_BASE_GOODS + '/goods/guess/get',
    }
}