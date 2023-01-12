const URL_BASE = process.env.NODE_ENV ===
'development' ? 'http://192.168.0.111:7000' : 'https://love.nagisazlp.cn';
export default{
    URL:{
        REGISTER: URL_BASE + '/platform/sso/register',
        LOGIN: URL_BASE + '/platform/sso/login',
        GETUSERINFO: URL_BASE + '/platform/account/info',
        UPDATEUSERINFO: URL_BASE + '/platform/account/update',
        REFRESHTOKEN: URL_BASE + '/platform/account/refresh',
    }
}

