//使用import.meta.globEager读取components文件夹的文件，以后缀名ts区分
// const componentsList = import.meta.globEager("./components/**"); vite写法
// const componentsList = require.context('../', false, /\.ts$/); // webpack写法

import Message from '../Message.vue';
import { h, render, App } from 'vue';
 
const createMount = (app: App, opt: any) => {
    const mountNode = document.createElement('div');
    document.body.appendChild(mountNode);
    const vnode = h(Message, {
        ...opt,
        remove() {
            document.body.removeChild(mountNode);
        },
    });
    // 将vnode的上下文指向当前app的上下文
    vnode.appContext = app._context;
    render(vnode, mountNode);
};
 
const MessageModal = {
    install(app: App) {
        app.config.globalProperties.$Message = {
            show: (options = {} as any) => {
                options.id = options.id || 'custom_message_' + 1; //唯一id 删除组件时用于定位
                createMount(app, options);
            },
        };
    },
};
 
export default MessageModal;
