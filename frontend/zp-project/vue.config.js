const path = require('path');
const packageJson = require('./package.json');

function resolve(dir) {
    return path.join(__dirname, dir)
}

const name = packageJson.name
const isDevelopment = process.env.NODE_ENV === 'development' // 判断是否是生产环境
module.exports = {
    // publicPath默认值是'/'，即你的应用是被部署在一个域名的根路径上
    // 设置为'./'，可以避免打包后的静态页面空白
    // 当在非本地环境时，这里以项目test-daily为例，即打包后的h5项目部署服务器的test-daily目录下
    // 那么这里就要把publicPath设置为/test-daily/，表示所有的静态资源都在/test-daily/里
    // 打包部署后，会发现index.html引用的静态资源都添加了路径/test-daily/
    publicPath: isDevelopment ? './' : `/platform/zp/${name}/`,
    outputDir: 'zp',
    productionSourceMap: isDevelopment,
    devServer: {
        open: false,
        port: 7001,
        headers: {
            'Access-Control-Allow-Origin': '*'
        }
    },
    configureWebpack: {
        output: {
            library: `${name}-[name]`,
            libraryTarget: 'umd', // 把微应用打包成 umd 库格式
            jsonpFunction: `webpackJsonp_${name}`
        }
    },

    lintOnSave: process.env.NODE_ENV === 'development',
    productionSourceMap: false, // 生产环境不需要 source map
    assetsDir:'assets',

}