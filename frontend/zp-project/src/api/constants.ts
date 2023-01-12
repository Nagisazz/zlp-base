const URL_BASE = process.env.NODE_ENV ===
'development' ? 'https://love.nagisazlp.cn' : 'https://love.nagisazlp.cn';
export default{
    URL:{
        LOGIN: URL_BASE + '/login',
        FAMILY_KANBAN: URL_BASE + '/zlp/blessing/list'
    }
}

