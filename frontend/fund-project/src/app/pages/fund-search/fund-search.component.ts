import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-fund-search',
  templateUrl: './fund-search.component.html',
  styleUrls: ['./fund-search.component.less']
})
export class FundSearchComponent implements OnInit {
    fundData: any = {
        code: 161725, // 基金编号
        pinglv: 7, // 定投频率
        asset: 7813, // 账户资产
        netValue: 0.9738, // 净值
        zhenfuDay: 5, // 参考振幅天数
        time: new Date(), // 时间
        ownAssets: 10000,
        investMoney: 3000, // 每次定投金额
        earnRatio: 0.053, // 收益率
        lineDay: 250, // 参考均线天数
        type: '2', // 策略类型
    }
    fundReault: any = {
        all: '-',
        rate: '-',
        price: '-',
        mean: '-',
        wave: '-',
        lastYield: '-'
    };
    tabType: any = '1';

    constructor(
        private router: Router,
    ) { }

    ngOnInit(): void {
    }

    onTab(type: any) {
        this.tabType = type;
    }

    onSearch(type: any) {
        let urlData = '';
        if (type === 'search') {
            const inputs = ['code', 'time', 'pinglv', 'investMoney', 'asset', 'netValue', 'earnRatio', 'lineDay', 'zhenfuDay', 'type'];
            urlData = 'http://1.15.87.105:12500/fund/invest' + "?data=" + this.formatData(inputs).join('$')
        } else if (type === 'start') {
            const inputs = ['code', 'asset', 'ownAssets', 'earnRatio', 'netValue', 'pinglv', 'investMoney', 'lineDay', 'zhenfuDay', 'type'];
            urlData = 'http://1.15.87.105:12500/fund/start' + "?data=" + this.formatData(inputs).join('$')
        }
        const that = this;
        
        let xhr = new XMLHttpRequest();
        xhr.open('GET', urlData, true);
        xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
        xhr.send();
        xhr.onreadystatechange = function() {
            if(xhr.readyState === 4 && xhr.status ===200 && type === 'search'){
                console.log(xhr.responseText);
                alert('请求成功！');
                const data = JSON.parse(xhr.responseText);
                const res = ['all', 'rate', 'price', 'mean', 'wave', 'lastYield'];
                for (let m = 0; m < res.length; m++) {
                    that.fundReault[res[m]] = data[res[m]];
                }
                
            } else {
                alert('请求失败！');
            }
        }
        xhr.onerror = function (error) {
            console.log(error);
            // alert('接口调用失败！');
        }
    }

    formatData(inputs: any) {
        let requestValue = [];
        for (let i = 0; i < inputs.length; i++) {
            if (inputs[i] === 'time') {
                requestValue.push(this.getDateFormat(this.fundData.time, 'yyyyMMdd'));
            } else {
                requestValue.push(this.fundData[inputs[i]]);
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

    onClear() {
        this.fundData = {
            code: 161725, // 基金编号
            pinglv: 7, // 定投频率
            asset: 7813, // 账户资产
            netValue: 0.9738, // 净值
            zhenfuDay: 5, // 参考振幅天数
            time: new Date(), // 时间
            ownAssets: 10000,
            investMoney: 3000, // 每次定投金额
            earnRatio: 0.053, // 收益率
            lineDay: 250, // 参考均线天数
            type: '2', // 策略类型
        }
    }

    onSingleClear(id: any) {
        this.fundData[id] = '';
    }

    toFundDetail() {
        this.router.navigate(['/fund-detail'])
    }

}
