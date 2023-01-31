import '@/views/login.less';
import { Checkbox, Input } from 'antd';
import { UserOutlined, ShoppingOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import TextEditor from '@/components/textEditor';

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

    const noteModalCancel = () => {

    }

    return (
        <TextEditor data={{}} catalogue={[]} onNoteModalCancel={noteModalCancel}></TextEditor>
    )
}

export default Login;