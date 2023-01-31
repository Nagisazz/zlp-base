import { Injectable } from '@angular/core';
import apiConstatns from './constants';
import { HttpserviceService } from './httpservice.service';

@Injectable({
    providedIn: 'root'
})
export class HttpIndexService {

    constructor(
      private httpService: HttpserviceService,
    ) {
        
    }

    updateTokenApi(): Promise<any> {
        return this.httpService.get(apiConstatns.URL.refreshToken);
    }

    // 持续计算
    investFund(param: any): Promise<any> {
        return this.httpService.get(apiConstatns.URL.investFund + '?' + param);
    }

    // 单次计算
    startFund(param: any): Promise<any> {
        return this.httpService.get(apiConstatns.URL.startFund + '?' + param);
    }

    // 基金列表
    fundList(param: any): Promise<any> {
        return this.httpService.get(apiConstatns.URL.fundList);
    }

    // 基金详情
    fundInfo(param: any): Promise<any> {
        return this.httpService.get(apiConstatns.URL.fundInfo + '?' + param);
    }

    // 基金信息更新
    fundUpdate(param: any): Promise<any> {
        return this.httpService.get(apiConstatns.URL.fundUpdate + '?' + param);
    }

    // 基金停止
    fundStop(param: any): Promise<any> {
        return this.httpService.get(apiConstatns.URL.fundStop + '?' + param);
    }

}
