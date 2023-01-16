import { Outlet, useNavigate } from 'react-router-dom';
import { getGoodsTotal } from '@/api/index';
import { useEffect, useRef, useState } from 'react';
import '@/views/goods.less';

const Goods = () => {
    // useState 返回的修改函数是异步的，调用后并不会直接生效，因此立马读取 value 获取到的是旧值（0）。
    // React 这样设计的目的是为了性能考虑，争取把所有状态改变后只重绘一次就能解决更新问题，
    // 而不是改一次重绘一次，也是很容易理解的。
    const [notices, setNotices] = useState<String[]>([
        '今日商品刺客--雪糕',
        '每轮的商品品牌基本都会在第一轮出现',
        '可用空格分隔关键词叠加回车搜索',
        'xxx贡献了商品100件'
    ]);
    // const [count, setCount] = useState(0);
    // const latestVal = useRef(count); // useRef是解决输出不是最新值的最底层方案。原因是闭包导致的
    const [intervalTime] = useState(2000);
    let [currentLi, setCurrentLi] = useState(0);
    const newCurrentLi = useRef(currentLi);

    const [loading, setLoading] = useState(false);
    const [goodsTotal, setGoodsTotal] = useState(0);

	useEffect(() => {
        getTotalData();
        // console.log(latestVal.current);
        // newItem();
        // const timerId2 = setInterval(() => {
        //     moveNext();
        // }, intervalTime)
        // return () => {
        //     if (timerId2) { // 销毁时清空计时器
        //         clearInterval(timerId2);
        //     }
        // }
	}, []); // 第二个参数为空时只会在第一次渲染时执行 模拟componentDidMount第一次渲染
    
    // useLayoutEffect(() => {
    // }, [])

    // useEffect(() => {
    //     console.log('n变化了');
    // }, [n]) // 第二个参数为要监听的数据  模拟componentDidUpdate
    // 不传第二个参数，则会在 state 的任意一个属性改变时都会触发该函数回调
    // useEffect(() => {
    //     console.log('任意属性变化');
    // })

    // tab页点击，子路由切换
    const nagivate = useNavigate();
    const onTab = (type: string) => {
        nagivate(type);
        // setCount((val) => val + 1);
        // latestVal.current += 1;
    }

    // 翻滚数据里添加一条数据
    const newItem = () => {
        const noticeDom = document.getElementById('noticeDom');
        if (noticeDom) {
            var newitem = noticeDom.children[0];
            var newli = newitem.cloneNode(true);
            noticeDom.appendChild(newli);
        }
    }
    // 上下滚动效果
    const moveNext = () => {
        let form = 32 * newCurrentLi.current;
        setCurrentLi((val) => val + 1);
        newCurrentLi.current++;
        const formli = 32 * newCurrentLi.current;
        const oneTime = 20; // 滚动效果20ms一次
        const changeAmount = (formli - form) / 20; // 20ms一次滚动的距离
        const noticeDom = document.getElementById('noticeDom');
        
        const timerId1 = setInterval(() => { // 向上滚动的效果，模拟滚动条
            form += changeAmount;
            if (form >= formli) { // 一条数据滚完，到下一条了，清空掉上一条滚动效果的计时器
                clearInterval(timerId1);
                if (newCurrentLi.current === notices.length + 1) {
                    form = 0;
                    setCurrentLi(0);
                    newCurrentLi.current = 0;
                }
            }
            
            if (noticeDom) {
                noticeDom.scrollTop = form;
            }
        }, oneTime)
    }

    // 获取商品收录总数
    const getTotalData = async () => {
		setLoading(true);
		try {
			const { data } = await getGoodsTotal();
            console.log(data);
			setGoodsTotal(data);
		} finally {
			setLoading(false);
		}
	};

    const toZp = () => {
        window.open('http://zp.nagisazlp.cn/', "_blank");
    }

    return (
        <div className="zp-layout">
            {/* <Header title='PRICE GUESSING STRATEGY'></Header> */}
            <div className="zp-layout-content zp-price-content">
                <div className="zp-price-tabs">
                    <div className="zp-price-tab" onClick={() => onTab('/')}>
                        <img src="http://1.15.87.105:11000/love/price/home.png" alt=""/> 首页
                    </div>
                    <div className="zp-price-tab" onClick={() => onTab('/phone')}>
                        <img src="http://1.15.87.105:11000/love/price/phone1.png" alt=""/> 拍照
                    </div>
                    <div className="zp-price-notice-overscroll" >
                        <ul id="noticeDom">
                            {notices.map((notice, index) => {
                               return <li key={index}>{notice}</li> 
                            })}
                        </ul>
                    </div>
                    <div className='zp-price-total' onClick={() => toZp()}>
                        本站已收录{goodsTotal}件商品, <span style={{color: '#5c79ed'}}>创作者：ZZ&CC</span>
                    </div>
                </div>
                <div className="zp-price-container">
                    <Outlet />
                </div>
            </div>
        </div>
    );
}

export default Goods;