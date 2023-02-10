import MessageBox from '../MessageBox.vue';
import { h, render, App } from 'vue';
 
const createMount = (app: App, opt: any) => {
    const mountNode = document.createElement('div');
    document.body.appendChild(mountNode);
    const vnode = h(MessageBox, {
        ...opt,
        remove() {
            document.body.removeChild(mountNode);
        },
    });
    // 将vnode的上下文指向当前app的上下文
    vnode.appContext = app._context;
    render(vnode, mountNode);
};
 
const MessageBoxModal = {
    install(app: App) {
        app.config.globalProperties.$MessageBox = {
            show: (options = {} as any) => {
                options.id = options.id || 'custom_messageBox_' + 1; //唯一id 删除组件时用于定位
                createMount(app, options);
            },
        };
    },
};
 
export default MessageBoxModal;
