export default {
    list: [
        {
            set_id: '001',
            title: '测试大屏01',
            url: 'http://1.15.87.105:11000/love/zp/family/001.png',
            isShowTitle: true, // 是否显示标题
            isShowHead: false, // 是否显示头部
            headContent: {}, // 头部内容
            isShowFooter: false,  // 显示底部
            footerContent: {},  // 底部内容
            bgImg: '',  // 背景图
            direction: '',  // 方向
            interval: '',  // 间隔
            layout: '4X3', // 布局
            column: 4,
            row: 3,
            unitArray: [
                {
                    size: '1X1',
                    colspan: '1',
                    rowspan: '1',
                    x: '0',
                    y: '0',
                    tree: {
                        id: 'TypeProportion',
                        name: '各项占比',
                    },
                    setData: {
                        size: '1X1',
                        types: ['beauty', 'clothe', 'house_property'],
                    }
                },
                {
                    size: '2X1',
                    colspan: '2',
                    rowspan: '1',
                    x: '1',
                    y: '0',
                    tree: {
                        id: 'DataContrast',
                        name: '柱状图对比',
                    },
                    setData: {
                        size: '2X1',
                        dimension: 'year',
                        year: 'year2',
                    }
                },
                {
                    size: '1X1',
                    colspan: '1',
                    rowspan: '1',
                    x: '0',
                    y: '1',
                    tree: {
                        id: 'Image',
                        name: '图片',
                    },
                    setData: {
                        size: '1X1',
                        url: '',
                    }
                },
                {
                    size: '1X1',
                    colspan: '1',
                    rowspan: '1',
                    x: '1',
                    y: '1',
                    tree: {
                        id: 'Calendar',
                        name: '日历',
                    },
                    setData: {
                        size: '1X1',
                        events: []
                    }
                },
            ], // 各自单元数据
        },
        {
            set_id: '002',
            title: '测试大屏02',
            url: 'http://1.15.87.105:11000/love/zp/family/002.png',
            isShowTitle: false, // 是否显示标题
            isShowHead: false, // 是否显示头部
            headContent: {}, // 头部内容
            isShowFooter: false,  // 显示底部
            footerContent: {},  // 底部内容
            bgImg: '',  // 背景图
            direction: '',  // 方向
            interval: '',  // 间隔
            layout: '3X2', // 布局
            column: 3,
            row: 2,
            unitArray: [
                {
                    size: '1X1',
                    colspan: '1',
                    rowspan: '1',
                    x: '0',
                    y: '0',
                    tree: {
                        id: 'TypeProportion',
                        name: '各项占比',
                    },
                    setData: {
                        size: '1X1',
                        types: ['beauty', 'clothe', 'house_property'],
                    },
                },
                {
                    size: '2X2',
                    colspan: '2',
                    rowspan: '2',
                    x: '1',
                    y: '0',
                    tree: {
                        id: 'DataContrast',
                        name: '柱状图对比',
                    },
                    setData: {
                        size: '2X2',
                        dimension: 'month',
                        month: 'month4',
                    },
                },
            ], // 各自单元数据
        }
    ]
}