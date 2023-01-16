import { Component, OnInit } from '@angular/core';
import * as echarts from 'echarts';
import { throwNzModalContentAlreadyAttachedError } from 'ng-zorro-antd/modal';

@Component({
  selector: 'app-fund-detail',
  templateUrl: './fund-detail.component.html',
  styleUrls: ['./fund-detail.component.less']
})
export class FundDetailComponent implements OnInit {

    fundList: any = [
        {
            balance: 4746.2,
            buyAmount: 5000,
            code: "012414",
            createTime: "2022-10-12T10:21:04",
            frequence: 11,
            id: 1,
            investAmount: 500,
            investMoney: 500,
            investNum: 0,
            lastInvestTime: "2022-10-06T00:00:00",
            mean: 250,
            name: "招商中证白酒指数(LOF)C",
            profileAmount: -253.8,
            saleAmount: 0,
            type: '2',
            updateTime: "2022-10-12T10:26:08",
            valid: 1,
            wave: 5,
            worth: 0.9738,
            yields: -0.0508,
        },
        {
            id: 2,
            name: '名称2',
            code: 'www',
            balance: '30000', // 账户资产
            worth: '1.04211', // 净值
            yields: '12%', // 收益率
            profileAmount: '2013', // 总收益
            buyAmount: '37000', // 总买入金额
            saleAmount: '7000', // 总卖出金额
            investAmount: '30000', // 总投资金额
            investNum: '', // 定投次数
            frequence: '2', // 定投频率
            investMoney: '1090', // 定投基准金额
            mean: '800', // 定投金额
            wave: '8', // 定投比率
            type: '2', // 策略类型
            valid: 1, // 是否有效 1是0否
            createTime: "2022-10-12T10:21:04", // 开始时间
            updateTime: "2022-10-12T10:26:08", // 更新时间
            lastInvestTime: "2022-10-06T00:00:00", // 上次定投日期
        }
    ];
    moreModal: any = false; // 显示更多开窗
    detailModal: any = false; // 查看详情开窗
    fundInfo: any = {}; // 显示更多数据
    moreEdit: any = false; // 显示更多中--是否可编辑
    noEchartData: any = false; // 无图表数据

    constructor() { }

    ngOnInit(): void {
        this.getInitList();
    }

    getInitList() {
        const that = this;
        let urlData = 'http://1.15.87.105:12500/fund/list';
        let xhr = new XMLHttpRequest();
        xhr.open('GET', urlData, true);
        xhr.setRequestHeader('X-Requested-With','XMLHttpRequest')
        xhr.send();
        xhr.onreadystatechange = function() {
        if(xhr.readyState === 4 && xhr.status ===200){
            that.fundList = JSON.parse(xhr.responseText).data;
        }
        }
        xhr.onerror = function (error) {
        }
    }

    handleCancel(type: string): void {
        if (type === 'more') {
        this.moreModal = false;
        } else {
        this.detailModal = false;
        }
    }

    calcRate(val: any) {
        return (val * 100).toFixed(2) + '%';
    }

    tranformShowTime(item_date: any) {
        let date = new Date(item_date);
        let YY = date.getFullYear() + "-";
        let MM =
            (date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1) + "-";
        let DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        let hh =
            (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
        let mm =
            (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes());
        let ss =
            date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
        return YY + MM + DD + " " + hh + mm;
    }

    tranformTime(item_date: any) {
        let date = new Date(item_date);
        let YY = date.getFullYear() + "";
        let MM =
            (date.getMonth() + 1 < 10
            ? "0" + (date.getMonth() + 1)
            : date.getMonth() + 1) + "";
        let DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        return YY + MM + DD;
    }

    showMore(data: any) {
        this.moreModal = true;
        this.moreEdit = false;
        this.fundInfo = this.fundList.filter((obj: any) => {
            return obj.id === data.id;
        })[0];
    }

    onEdit() {
        this.moreEdit = true;
    }

    onSave() {
        let urlData = '';
        let that = this;

        urlData = 'http://1.15.87.105:12500/fund/update' + "?data=" + this.fundInfo.id + '$' + this.formatData().join('$');
        console.log(urlData);
                
        let xhr = new XMLHttpRequest();
        xhr.open('GET', urlData, true);
        xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
        xhr.send();
        xhr.onreadystatechange = function() {
            if(xhr.readyState === 4 && xhr.status ===200){
                alert('编辑成功！');
                that.moreModal = false;
            }
        }
        xhr.onerror = function (error) {
            // alert('接口调用失败！');
        }
                // $.ajax({
                //     url: urlData,//请求的地址
                //     type: 'get',//请求的方式
                //     data: '',//携带到后端的参数
                //     dataType: 'json',// 期望后端返回的数据类型
                //     headers:{'Content-Type':'application/x-www-form-urlencoded;charset=utf8'},
                //     beforeSend:function (request) {
                //         console.log('加载中。。。');
                //     },
                //     success: function (res) {
                //         alert('编辑成功！');
                //         that.hiddenEdit('update');
                //     },//成功的回调函数 res就是后端响应回来的数据
                //     error: function (res) {
                //     },
                // })
    }

    formatData() {
        const inputs = ['frequence', 'investMoney', 'mean', 'wave', 'type', 'lastInvestTime'];
        let requestValue = [];
        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i] === 'lastInvestTime') {
                requestValue.push(this.getDateFormat(this.fundInfo.lastInvestTime, 'yyyyMMdd'));
            } else {
                requestValue.push(this.fundInfo[inputs[i]]);
            }
        }
        return requestValue;
    }

    getDateFormat(date: Date, format: string = 'yyyy-MM-dd HH:mm:ss') {
        const pad = (n: number): string => (n < 10 ? `0${n}` : n.toString());
        return format
            .replace('yyyy', date.getFullYear().toString())
            .replace('MM', pad(date.getMonth() + 1))
            .replace('dd', pad(date.getDate()))
            .replace('HH', pad(date.getHours()))
            .replace('mm', pad(date.getMinutes()))
            .replace('ss', pad(date.getSeconds()));
    }

    onStop(data: any) {
        let that = this;
        let urlData = 'http://1.15.87.105:12500/fund/stop' + "?fundId=" + data.id
        let xhr = new XMLHttpRequest();
        xhr.open('GET', urlData, true);
        xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
        xhr.send();
        xhr.onreadystatechange = function() {
            if(xhr.readyState === 4 && xhr.status ===200){
                console.log('改定投停止执行');
                that.getInitList();
            }
        }
        xhr.onerror = function (error) {
            console.log(error)
        }
    }

    toDetail(data: any) {
        this.detailModal = true;
        let urlData = 'http://1.15.87.105:12500/fund/info' + "?fundId=" + data.id
        let xhr = new XMLHttpRequest();
        xhr.open('GET', urlData, true);
        xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
        xhr.send();
        const that = this;
        xhr.onreadystatechange = function() {
            if(xhr.readyState === 4 && xhr.status === 200){
                const data = JSON.parse(xhr.responseText);
                const x: any = [];
                const worth: any = [];
                const balance: any = [];
                const investAmount: any = [];
                if (data.data && data.data.length > 0) {
                    that.noEchartData = true;
                    data.data.map((obj: any) => {
                        x.push(that.tranformShowTime(obj.createTime));
                        worth.push(obj.worth);
                        balance.push(obj.balance);
                        investAmount.push(obj.investAmount);
                    })
                    that.setEcharts(x, worth, balance, investAmount);
                }
            }
        }
        xhr.onerror = function (error) {
        }
    }

    setEcharts(x: any, worth: any, balance: any, investAmount: any) {
        var myChart = echarts.init(document.getElementById('fundEcharts') as HTMLElement);
        var option;
        option = {
            grid: {
                bottom: 80,
                top: 30
            },
            toolbox: {
                feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                restore: {},
                saveAsImage: {}
                }
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                type: 'cross',
                animation: false,
                label: {
                    backgroundColor: '#505765'
                }
                }
            },
            legend: {
                data: ['净值', '账户资产', '投资金额'],
            },
            dataZoom: [
                {
                    show: true,
                    realtime: true,
                    start: 50,
                    end: 100
                },
                {
                    type: 'inside',
                    realtime: true,
                    start: 50,
                    end: 100
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    axisLine: { onZero: false },
                    data: x || ['2009/6/03', '2009/6/05', '2009/6/08', '2009/6/10', '2009/6/12', '2009/6/16']
                }
            ],
            yAxis: [
                {
                    name: '净值（元）',
                    type: 'value',
                    axisLine: {
                        show: true
                    },
                    axisTick: {
                        show: true
                    },
                    splitLine: {
                        show: false
                    }
                },
                {
                    name: '账户资产/投资金额（元）',
                    type: 'value',
                    axisLine: {
                        show: true
                    },
                    axisTick: {
                        show: true
                    },
                    splitLine: {
                        show: false
                    }
                }
            ],
            series: [
                {
                    name: '净值',
                    type: 'line',
                    lineStyle: {
                        width: 2
                    },
                    data: worth || [1.03, 1.11, 1.23, 1.06, 1.33, 1.18, 1.20]
                },
                {
                    name: '账户资产',
                    type: 'line',
                    yAxisIndex: 1,
                    lineStyle: {
                        width: 2
                    },
                    data: balance || [150, 230, 224, 218, 135, 147, 260]
                },
                {
                    name: '投资金额',
                    type: 'line',
                    yAxisIndex: 1,
                    lineStyle: {
                        width: 2
                    },
                    data: investAmount || [1500, 1100, 900, 700, 550, 900, 880]
                }
            ]
        };
        myChart.setOption(option);
    }

}
