const URL_BASE_PLATFORM = 'http://platform.zlpnet.cn';
const URL_BASE_FUND = 'http://fund.zlpnet.cn';

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

        // 查询
        investFund: URL_BASE_FUND + '/fund/invest',
        // 开始持续计算
        startFund: URL_BASE_FUND + '/fund/start',
        // 基金列表
        fundList: URL_BASE_FUND + '/fund/list',
        // 基金详情
        fundInfo: URL_BASE_FUND + '/fund/info',
        // 基金信息更新
        fundUpdate: URL_BASE_FUND + '/fund/update',
        // 停止计算
        fundStop: URL_BASE_FUND + '/fund/stop',
        
    }
}