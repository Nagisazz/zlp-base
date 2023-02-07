import { createRouter, createWebHistory } from 'vue-router';
import { start } from 'qiankun';
import store from '@/store';

const routes: any = [{
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    children: [
        {
            path: '/',
            name: 'Home',
            component: () => import('../components/Home.vue'),
            meta: {
                title: '首页',
            }
        },
        {
            path: '/platform:pathMatch(.*)',
            component: import('../views/Micro.vue')
        },
    ]
}]

const router = createRouter({
    history: createWebHistory(process.env.NODE_ENV === 'development' ? '' : '/'),
    routes
})

// 页面进入之后
router.afterEach(() => {
    // 注释这边的start(),部署上去就会不加载子应用
    if (!(window as any).qiankunStarted) {
        (window as any).qiankunStarted = true;
        start();
    }
    store.commit("IsShowHead/CHANGEROUTEPATH", window.location.href);
})

export default router;