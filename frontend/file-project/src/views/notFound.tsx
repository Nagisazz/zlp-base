import '@/style/zp.less';
import { useNavigate } from 'react-router-dom';

const NotFound = () => {
    const navigate = useNavigate();
	const goHome = () => {
		navigate('/');
	};

    return (
        <div className="zp-layout zp-no-page">
            <i className="iconfont zp-ku"></i>
            <div className='zp-txt'>画面走丢了，<span onClick={goHome}>去首页！</span></div>
        </div>
    );
}

export default NotFound;