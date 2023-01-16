import actions from '@/qiankun/action';

// 动作层拆离
export const setTokenInfo = (loginInfo) => {
	const ssKey = 'platform.login';
	console.log(loginInfo, 'loginInfologinInfo');
	sessionStorage.setItem(ssKey, JSON.stringify(loginInfo));
	return {
		type: 'SETTOKENINFO',
		loginInfo
	};
};
