import React from 'react';
import { Navigate, useRoutes } from "react-router-dom";
import { RouterConfig } from '@/domain/router';
 
import NotFound from '@/views/notFound';
import lazyLoad from '@/utils/lazyLoad';
import Login from '@/views/login';
import ShareFile from '@/views/shareFile';


export const rootRouter: RouterConfig[] = [
    {
        path: '/',
        element: <ShareFile />
    },
    {
        path: '/login',
        element: <Login />
    },
    {
        path: '*',
        element: <NotFound />
    },
]

const Router = () => {
	const routes = useRoutes(rootRouter);
	return routes;
};

export default Router;