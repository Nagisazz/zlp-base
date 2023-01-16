import { getGoodsSearch } from '@/api';
import { Col, message, Row, Spin } from 'antd';
import Checkbox from 'antd/lib/checkbox/Checkbox';
import Search from 'antd/lib/input/Search';
import React, { useEffect, useRef, useState } from 'react';
import '@/pages/goods/price.less';

const Price = () => {
    const [loading, setLoading] = useState(false);
    const [searchInfo, setSearchInfo] = useState('');
    const newSearchInfo = useRef(searchInfo);
    const [historyList, setHistoryList] = useState<any[]>(['苏泊尔', '斯凯奇', '美的 冰箱', '斯凯奇 鞋']);
    const newHistoryList = useRef(historyList);
    const [hotsList, setHotsList] = useState<any[]>(['苏泊尔', '斯凯奇', '美的 冰箱', '斯凯奇 鞋']);
    const [totalPrice, setTotalPrice] = useState(0);
    const newTotalPrice = useRef(totalPrice);
    const [selectGoodsList, setSelectGoodsList] = useState<any[]>([]);
    const newSelectGoodsList = useRef(selectGoodsList);
    const [goodGroupList, setGoodGroupList] = useState<any[]>([]);
    const newGoodGroupList = useRef(goodGroupList);

    const setSearchVal = (e: any) => {
        newSearchInfo.current = e.target.value;
        setSearchInfo(newSearchInfo.current);
    }

    const onEnter = (e: any) => {
        onSearch(e.target.value);
    }

    // 查询
    const onSearch = async (val?: any) => {
        // setLoading(true);
        try {
            const info = val ? val : newSearchInfo.current;
			const { data } = await getGoodsSearch('name=' + info);
            console.log(data);
            if (data && data.length > 0) {
                if (newHistoryList.current.indexOf(info) < 0) {
                    newHistoryList.current.unshift(info);
                    localStorage.setItem('historyList', JSON.stringify(newHistoryList.current));
                }
                setHistoryList([...newHistoryList.current]); // 没有的话往历史记录里添加
                data.map((obj: any) => {
                    obj.select = 'N';
                })
            } else {
                message.warning('暂未查询到该类商品！');
            }
            const group = {
                name: info,
                id: data && data[0] ? data[0].id : '',
                isCheck: false,
                sort: 'asc',
                list: data
            }
            if (data.length > 0) {
                newGoodGroupList.current.push(group);
                setGoodGroupList([...newGoodGroupList.current]);
            }
		} finally {
			setLoading(false);
		}
    }

    useEffect(() => {
        if (localStorage.getItem('historyList')) {
            const history = localStorage.getItem('historyList') as any;
            newHistoryList.current = JSON.parse(history);
            setHistoryList([...newHistoryList.current]);
        }
	}, []);

    // 清空历史记录
    const delHistorys = () => {
        setHistoryList([]);
    }

    // 清空热搜
    const delHots = () => {
        setHotsList([]);
    }

    // 删除已选的商品
    const delSelected = (e: any, isCalc?: any) => {
        const newList = newSelectGoodsList.current.filter((good) => {
            return good.id !== e.id;
        })
        setSelectGoodsList(newList);
        newSelectGoodsList.current = newList;
        newGoodGroupList.current.map((obj1) => {
            obj1.list.map((obj2: any) => {
                if (obj2.id === e.id) {
                    obj2.select = 'N';
                }
            })
        })
        setGoodGroupList([...newGoodGroupList.current]);
        if (!isCalc) { // 有些循环调用此方法的情景下，不需要在这里计算金额
            reCalcPrice();
        }
    }

    // 重新计算已选商品价钱
    const reCalcPrice = () => {
        let total = 0;
        newSelectGoodsList.current.map((good) => {
            total = total + Number(good.price);
        })
        setTotalPrice(total);
        newTotalPrice.current = total;
    }

    // 全选，全不选
    const selectAllChange = (group: any) => {
        const idx = newGoodGroupList.current.findIndex((obj) => {
            return obj.id === group.id;
        })
        newGoodGroupList.current[idx].isCheck = !newGoodGroupList.current[idx].isCheck;
        setGoodGroupList([...newGoodGroupList.current]);
        if (newGoodGroupList.current[idx].isCheck) { // 全选
            newGoodGroupList.current[idx].list.map((obj: any) => {
                obj.select = 'Y';
                newSelectGoodsList.current.push(obj);
            })
        } else { // 全不选
            newGoodGroupList.current[idx].list.map((obj: any) => {
                obj.select = 'N';
                delSelected(obj, 'no');
            })
        }
        reCalcPrice();
    };

    // 对分类商品排序
    const onSort = (group: any) => {
        const idx = newGoodGroupList.current.findIndex((obj) => {
            return obj.id === group.id;
        })
        newGoodGroupList.current[idx].sort = newGoodGroupList.current[idx].sort === 'asc' ? 'desc' : 'asc';
        newGoodGroupList.current[idx].list.sort((a: any, b: any) => {
            if (newGoodGroupList.current[idx].sort === 'desc') {
                return b.price - a.price;
            } else {
                return a.price - b.price;
            }
        })
        setGoodGroupList([...newGoodGroupList.current]);
    }

    // 删除分类商品
    const onDelGroup = (group: any) => {
        const idx = newGoodGroupList.current.findIndex((obj) => {
            return obj.id === group.id;
        })
        newGoodGroupList.current[idx].list.map((obj2: any) => {
            if (obj2.select === 'Y') {
                delSelected(obj2);
            }
        })
        const newList = newGoodGroupList.current.filter((obj) => {
            return obj.id !== group.id;
        })
        newGoodGroupList.current = newList;
        setGoodGroupList([...newGoodGroupList.current]);
    }

    // 选择、取消选择分类商品
    const onSelectGood = (group: any, good: any) => {
        const idx = newGoodGroupList.current.findIndex((obj) => {
            return obj.id === group.id;
        })
        newGoodGroupList.current[idx].list.map((obj2: any) => {
            if (good.id === obj2.id) {
                if (obj2.select === 'Y') {
                    newGoodGroupList.current[idx].isCheck = false;
                    obj2.select = 'N';
                    delSelected(obj2, 'no');
                } else {
                    obj2.select = 'Y';
                    newSelectGoodsList.current.push(obj2);
                }
            }
        })
        setGoodGroupList([...newGoodGroupList.current]);
        reCalcPrice();
    }

    return (
        <Spin spinning={loading} tip="Loading...">
            <Row gutter={22}>
                <Col className="gutter-row" span={8} >
                    <div className="zp-price-content-left">
                        <Search placeholder="请输入..." value={searchInfo} enterButton="查询" size="middle" 
                            onChange={setSearchVal} onSearch={onSearch} onPressEnter={onEnter}/>
                        <div className='tip'>*可以输入商品或品牌，中间用空格连接叠加查询</div>
                        <div className='tip'>ex: 斯凯奇 鞋；ex：苏泊尔 美的</div>
                        <div className='zp-price-notice'>
                            <p>历史记录 <i className='iconfont zp-delete' onClick={() => delHistorys()}></i> </p>
                            <div className='tags'>
                                {historyList.map((his, idx) => {
                                    return <span className='tag' key={idx} onClick={() => {onSearch(his)}}>{his}</span>
                                })}
                            </div>
                        </div>
                        <div className='zp-price-notice'>
                            <p>热搜 <i className='iconfont zp-delete' onClick={() => delHots()}></i></p>
                            <div className='tags'>
                                {hotsList.map((hot, idx) => {
                                        return <span className='tag' key={idx} onClick={() => {onSearch(hot)}}>{hot}</span>
                                })}
                            </div>
                        </div>
                    </div>
                </Col>
                <Col className="gutter-row" span={16} >
                    <div className="zp-price-content-right">
                        <div className='zp-price-selecteds'>
                            总计<span className='price'>{totalPrice}</span>元，
                            已选：
                            {selectGoodsList.map((sel, idx) => {
                                return <div className='zp-price-selected' key={idx}>{sel.name} ￥{sel.price}
                                        <span onClick={() => delSelected(sel)}>X</span>
                                    </div>
                            })}
                        </div>
                        {goodGroupList.length === 0 ? 
                            <div className='no-data'> 
                                <i className='iconfont zp-zanwushuju'></i> <div>暂无数据，请先下查询条件！</div> 
                            </div>
                            :
                            <div className='zp-price-goods-box'>
                                {goodGroupList.map((group, idx) => {
                                    return  <div className='zp-price-goods' key={idx}>
                                        <div className='goods-title'>
                                            <span className='title-txt'>{group.name}</span>     
                                            <Checkbox checked={group.isCheck} onChange={() => selectAllChange(group)}>全选</Checkbox>
                                            <div className='operation' onClick={() => onSort(group)}>
                                                <i className='iconfont zp-paixu1'></i>按价格排序
                                            </div>
                                            <div className='operation' onClick={() => onDelGroup(group)}>
                                                <i className='iconfont zp-delete'></i>删除该类商品
                                            </div>
                                        </div>
                                        <div className='goods'>
                                            {group.list.map((good: any, idx2: number) => {
                                                return <div className={`good ${good.select === 'Y' ? 'isSelectGood' :'' }`}  key={idx2} 
                                                            onClick={() => onSelectGood(group, good)}>
                                                        {good.name} ￥{good.price}
                                                    </div>
                                            })}
                                        </div>
                                    </div>
                                })}
                            </div>
                        }
                    </div>
                </Col>
            </Row>
        </Spin>
    );
}

export default Price;