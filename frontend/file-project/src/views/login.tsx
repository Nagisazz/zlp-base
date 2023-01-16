import '@/views/login.less';
import { Checkbox, Input } from 'antd';
import { UserOutlined, ShoppingOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';

const Login = () => {

    const nagivate = useNavigate();
    const onLogin = () => {
        nagivate('/sharefile');
    }

    // 记住我
    const onRemmber = () => {

    }

    // 忘记密码
    const forgetPassword = () => {

    }

    return (
        <div className="zp-layout zp-login">
            <div className='login-box'>
                <div className="login-title">WELCOME</div>
                <Input className='input-item' placeholder="account"
                    prefix={<UserOutlined />} />
                <Input className='input-item' placeholder="password"
                    prefix={<ShoppingOutlined color="#666" />} />
                <div className='login-btn' onClick={() => {(onLogin())}}>立即登录</div>
                <div className='login-operate'>
                    <div><Checkbox onChange={onRemmber}>记住我</Checkbox></div>
                    <div onClick={() => {forgetPassword()}}>忘记密码?</div>
                </div>
                <div className='login-tip'>@rightppppppppppp</div>
            </div>
        </div>
    )
}

export default Login;