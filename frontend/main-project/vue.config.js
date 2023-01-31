const path = require("path");

function resolve(dir) {
    return path.join(__dirname, dir);
}

const isDevelopment = process.env.NODE_ENV === 'development' // 判断是否是生产环境

module.exports = {
    publicPath: isDevelopment ? '/' :'/',
    outputDir: 'platform',
    productionSourceMap: isDevelopment,
    devServer: {
        open: true,
        port: 7000,
        headers: {
            'Access-Control-Allow-Origin': '*'
        },
    },

    configureWebpack: {
        name: '朱平测试子应用项目',
        resolve: {
            extensions: ['.js', '.vue', '.json'],
            alias: {
                '@': resolve('src'), // 配置别名
            }
        },
        plugins: [],
        // module: {
        //     rules: [
        //         {
        //             test: /\.scss$/i,
        //             loader: 'sass-loader',
        //             options: {
        //                 additionalData: `@import "@/src/styles/element/index.scss";`,
        //             },
        //         },
        //         {
        //             test: /\.css$/i,
        //             loader: 'sass-loader',
        //             options: {
        //                 additionalData: `@import "@/src/styles/element/index.scss";`,
        //             },
        //         },
        //     ],
        // },

    },

    css: {
        loaderOptions: {
            // 给 sass-loader 传递选项
            // 默认情况下 `sass` 选项会同时对 `sass` 和 `scss` 语法同时生效
            // 因为 `scss` 语法在内部也是由 sass-loader 处理的
            //  注意：在 sass-loader v8 中，这个选项名是 "prependData" 但是在配置 `prependData` 选项的时候
            // `scss` 语法会要求语句结尾必须有分号，`sass` 则要求必须没有分号
            // 在这种情况下，我们可以使用 `scss` 选项，对 `scss` 语法进行单独配置
            // sass: {
            //     additionalData: `@use "~/styles/element/index.scss" as *`, 
            //                        没有配置别名~，所以不能用，要用@
            // },
            scss: {
                additionalData: `@use "@/styles/element/index.scss" as *;`,
            }
        }
    },


    chainWebpack: () => {}

}