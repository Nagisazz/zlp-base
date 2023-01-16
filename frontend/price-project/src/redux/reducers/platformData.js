// 处理动作的函数
const ssKey = 'platform.login';
const infoCache = sessionStorage.getItem(ssKey);

const reducer = (state = { 
	loginInfo: infoCache === null ? null : JSON.parse(infoCache), 
}, action) => {
	switch (action.type) {
		case 'SETTOKENINFO':
			return {
				loginInfo: { ...state, loginInfo: action.loginInfo },
				
			};
		default:
			return state;
	}
};

export default reducer;