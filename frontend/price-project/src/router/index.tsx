import React from 'react';
import { Navigate, useRoutes } from "react-router-dom";
import { RouterConfig } from '@/domain/router';
 
import Goods from '@/views/goods';
import lazyLoad from '@/utils/lazyLoad';


export const rootRouter: RouterConfig[] = [
    // {
    //     path: '/',
    //     element: <Navigate to="/home" />
    // }, 
    // 因为home下面还有子路由，所以不可以这样写。
    // 如果home没有子路由，这样写的话路径默认添加/home-->http://localhost:7002/home
    {
        path: '/',
        element: <Goods />,
        children: [
            // {
            //     path:'/',
            //     element: <Navigate to="/price" />
            // },
            {
                path: '/',
                element: lazyLoad(React.lazy(() => import("@/pages/goods/price")))
            },
            {
                path: '/price',
                element: lazyLoad(React.lazy(() => import("@/pages/goods/price")))
            },
            {
                path: '/phone',
                element: lazyLoad(React.lazy(() => import("@/pages/goods/phone")))
            },
        ]
    },
]

const Router = () => {
	const routes = useRoutes(rootRouter);
	return routes;
};

export default Router;