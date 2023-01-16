import React from 'react';
import '@/components/header.less';
import { useNavigate } from 'react-router';


type selfProps = {
    title: string;
}

const Header: React.FC<selfProps> = (props) => {

    const nagitive = useNavigate();
    const goOut = () => {
        nagitive('/login');
    }

    return (
        <div className="zp-price-head">
            <div className="head-title">{props.title}</div>
            <div className="head-info">
                <img src="http://1.15.87.105:11000/love/zp/home/user.webp" />
                <span className="head-info-name">朱朱</span>
                <i className="iconfont zp-logout" onClick={() => {goOut()}}></i>
            </div>
        </div>
    );
}

export default Header;