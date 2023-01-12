import { createRouter, createWebHistory } from 'vue-router';

const routes: any = [{
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    children: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/home',
            name: 'Home',
            component: () => import('../components/Home.vue'),
            meta: {
                title: '首页'
            }
        },
        {
            path: '/platform:pathMatch(.*)',
            component: import('../views/Micro.vue')
        },
    ]
}]

const router = createRouter({
    history: createWebHistory(process.env.NODE_ENV === 'development' ? '' : '/platform'),
    routes
})

// 页面进入之后
router.afterEach(() => {
    console.log('router---index.js进入路由画面了');
})

export default router;