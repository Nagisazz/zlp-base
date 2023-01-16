export interface MetaProps {
    keepAlive?: boolean;
	requiresAuth?: boolean;
	title: string;
	key?: string;
}

export interface RouterConfig {
    path?: string,
    element?: React.ReactNode,
    children?: RouterConfig[],
    meta?: MetaProps
}