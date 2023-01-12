import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import constants from '@/router/constants';

Vue.use(VueRouter)

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../layout/Home.vue'),
    children: [
      {
        path: '/',
        name: 'Work',
        component: () => import('../home/work.vue'),
      },
      {
        path: constants.PATH.HOME_WORK,
        name: 'Work',
        component: () => import('../home/work.vue'),
      },
      {
        path: constants.PATH.HOME_WORK_DETAIL,
        name: 'workDetail',
        component: () => import('../home/workDetail.vue'),
      },
      {
        path: constants.PATH.HOME_ARTICLE,
        name: 'Article',
        component: () => import('../home/article.vue'),
      },
      {
        path: constants.PATH.HOME_ABOUT,
        name: 'about',
        component: () => import('../home/about.vue')
      },
    ]
  },
  {
    path: constants.PATH.RESUME,
    name: 'Resume',
    component: () => import('../layout/Resume.vue')
  },
  {
    path: constants.PATH.FAMILY,
    name: 'Family',
    component: () => import('../layout/Family.vue'),
  },
  {
    path: constants.PATH.FAMILY_SCREEN,
    name: 'largeScreen',
    component: () => import('../family/largeScreen/largeScreen.vue'),
  }
]

// const routes = new VueRouter({
//   routes
// })

export default routes
