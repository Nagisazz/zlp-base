import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
const ssKey = 'platform.login';
const infoCache = sessionStorage.getItem(ssKey);

@Injectable()
export class InfoService {
    public _loginInfo: any = infoCache === null ? null : JSON.parse(infoCache);
    public setInfo$: BehaviorSubject<any> = new BehaviorSubject({});

    public changeInfo(info: any, type?: any): void {
        if (type === 'token') {
            this._loginInfo.token = info.refreshToken;
        } else {
            this._loginInfo = info;
            const ssKey = 'platform.login';
            sessionStorage.setItem(ssKey, JSON.stringify(info));
        }
        console.log(this._loginInfo);
        this.setInfo$.next(this._loginInfo);
    }


}
