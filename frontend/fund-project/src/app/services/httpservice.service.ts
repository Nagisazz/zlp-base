import { Injectable, ReflectiveInjector } from '@angular/core';
import axios from 'axios';
import actions from '../qiankun/action';
import apiConstatns from './constants';
import { InfoService } from './info.service';
import { NzModalService } from 'ng-zorro-antd/modal';

@Injectable({
  providedIn: 'root'
})
export class HttpserviceService {

  constructor(
    private infoService: InfoService,
    private modal: NzModalService
  ) {
    axios.defaults.timeout = 30000
    axios.interceptors.request.use((config: any) => {
      config.headers['X-Requested-With'] = 'XMLHttpRequest';
      this.infoService.setInfo$.subscribe((res: any) => {
        console.log(res);
        let token = '';
        if (res.token) {
          token = res.token
        } else {
          const ssKey = 'platform.login';
          const infoCache = sessionStorage.getItem(ssKey);
          token = infoCache? JSON.parse(infoCache).token : '';
        }
        config.headers['Authorization'] = token; // 重新设置请求头生效，想到绑定的值是响应式的，所以更新值即可
      })
      
      return config;
    })

    const retry = 0
    axios.interceptors.response.use(undefined, function axiosRetryInterceptor(err) {
      const config = err.config;
      if(!config) {
        return Promise.reject(err);
      }
      config.__retryCount = config.__retryCount || 0;
      if(err.response.status === 401 || config.__retryCount >= retry) {
          return Promise.reject(err);
      }
      config.__retryCount += 1;
      const backoff = new Promise((resolve) => {
          setTimeout(() => {
              resolve('success');
          }, config.retryDelay || 1);
      });
      return backoff.then(() => {
          return axios(config);
      });
  });
  }

  /** get操作 */
  get(url: string,param?: any): Promise<any> {
    return this.request(url,'get',param)
  }
  
  /** post操作 */
  post(url: string,param?: any): Promise<any> {
    return this.request(url,'post',param)
  }

  /** request  */
  request(url: string, method: any, param?: any): Promise<any> {
    if(param) {
        return new Promise((resolve) => {
            axios({ 
              method,
              url,
              data: param,
            }).then((res) => {
              if (res.data.status === 200) {
                resolve(res.data);
              } else if (res.data.status === 412) { // 412请求过程中发现token失效
                this.infoService.changeInfo(this.infoService._loginInfo, 'token');
                if (url.indexOf('platform/account/refresh') === -1) {
                  this.freshToken().then((res: any) => { // 新token获取后重新调用原接口
                    if (res === 'success') {
                      this.request(url, method, param)
                    } else if (res === 'toLogin') {
                      // 如果2个token都过期则重新登录
                      actions.setGlobalState({ showSign: true });
                    }
                  })
                }
              } else {
                this.toResult(url, 'success', res.data);
              }
            }).catch((err) => {
              if (err.response.data.status === 412) { // 412请求过程中发现token失效
                this.infoService.changeInfo(this.infoService._loginInfo, 'token');
                if (url.indexOf('platform/account/refresh') === -1) {
                  this.freshToken().then((res: any) => { // 新token获取后重新调用原接口
                    if (res === 'success') {
                      this.request(url, method, param)
                    } else if (res === 'toLogin') {
                      // 如果2个token都过期则重新登录
                      actions.setGlobalState({ showSign: true });
                    }
                  })
                }
              }
            })
        })
    } else {
        return new Promise((resolve) => {
            axios({
                method,
                url,
            }).then((res) => {
              if (res.data.status === 200) {
                resolve(res.data);
              } else if (res.data.status === 412) { // 412请求过程中发现token失效
                this.infoService.changeInfo(this.infoService._loginInfo, 'token');
                if (url.indexOf('platform/account/refresh') === -1) {
                  this.freshToken().then((res: any) => { // 新token获取后重新调用原接口
                    if (res === 'success') {
                      this.request(url, method, param)
                    } else if (res === 'toLogin') {
                      // 如果2个token都过期则重新登录
                      actions.setGlobalState({ showSign: true });
                    }
                  })
                }
              } else {
                this.toResult(url, 'success', res.data);
              }
            }).catch((err) => {
              // toResult(url, 'error', err);
              if (err.response && err.response.data.status === 412) { // 412请求过程中发现token失效
                this.infoService.changeInfo(this.infoService._loginInfo, 'token');
                if (url.indexOf('platform/account/refresh') === -1) {
                  this.freshToken().then((res: any) => { // 新token获取后重新调用原接口
                    if (res === 'success') {
                      this.request(url, method, param)
                    } else if (res === 'toLogin') {
                      // 如果2个token都过期则重新登录
                      actions.setGlobalState({ showSign: true });
                    }
                  })
                }
              }
            })
        })
    }
  }

  // 刷新token值
  freshToken(): Promise<any> {
    return new Promise((resolve) => {
      const url = apiConstatns.URL.refreshToken;
      const method=  'GET';
      axios({
        method,
        url
      }).then((res: any) => {
        console.log(res.data.data);
        if (res.data.status === 200) {
          this.infoService.changeInfo(res.data.data);
          actions.setGlobalState({ token: res.data.data.token });
          actions.setGlobalState({ refreshToken: res.data.data.refreshToken });
          resolve('success');
        } else if (res.data.status === 412) { // 412请求过程中发现token失效
          resolve('toLogin');
        } else {
          resolve('error');
        }
      }).catch((err: any) => {})
    })
  }

  toResult(url: string, type: string , httpResponseErr: any): any {
    let errMsg = '';
    const errInfo = type === 'success' ? httpResponseErr : httpResponseErr.response;
    let showTip = true;
    if (errInfo) {
      switch (errInfo.status) {
        case 401:
        errMsg = '登录过期，请重新登录';
        showTip = false;
        this.modal.info({
          nzTitle: '提示',
          nzContent: '您尚未登录或登录时间过长,请重新登录!',
          nzOkText: '确定',
          nzOnOk: () => {
            actions.setGlobalState({ showSign: true });
          }
        });
        break;
        case 403:
          errMsg = '拒绝访问'
          break;
        case 404:
          errMsg = '请求错误,未找到该资源'
          break;
        case 405:
          errMsg = '请求方法未允许'
          break;
        case 408:
          errMsg = '请求超时'
          break;
        case 500:
          errMsg = '服务器端出错'
          break;
        case 501:
          errMsg = '网络未实现'
          break;
        case 502:
          errMsg = '网络错误'
          break;
        case 503:
          errMsg = '服务不可用'
          break;
        case 504:
          errMsg = '网络超时'
          break;
        case 505:
          errMsg = 'http版本不支持该请求'
          break;
        // default:
        //   errMsg = errInfo.message
        //   break;
      }
    } else {
      // errMsg = '连接到服务器失败'
    }
    if (showTip) {
      // message.warning(errMsg);
      console.log(errMsg);
    }
  }
  


}

// 默认30秒超时
// axios.defaults.timeout = 30000
// 超时重复请求次数
// const retry = 0
// 拦截request,设置全局请求为ajax请求  设置默认请求头 X-Requested-With: XMLHttpRequest是给服务器用的，用于区别 AJAX 请求(异步)
// 还是普通（同步）的请求（一般指表单提交）的。 x-requested-with 赋予 ‘XMLHttpRequest’ 
// 值表示这是一个ajax请求，而如果值为null的话 表示一个普通的请求，服务器用来检测是否为异步 如果服务器没做任何针对的反馈那么都一样。
// axios.interceptors.request.use((config: any) => {
//     config.headers['X-Requested-With'] = 'XMLHttpRequest';
//     infoService.setInfo$.subscribe((res: any) => {
//       console.log(res);
//     })
//     infoScription = infoService.setInfo$.subscribe((res: any) => {
//       console.log(res);
//       let token = '';
//       if (infoService._loginInfo) {
//         token = infoService._loginInfo.token
//       }
//       config.headers['Authorization'] = token; // 重新设置请求头生效，想到绑定的值是响应式的，所以更新值即可
//     })
    
//     return config;
// })

// axios.interceptors.response.use(undefined, function axiosRetryInterceptor(err) {
//     const config = err.config;
//     // If config does not exist or the retry option is not set, reject

//     if(!config) {
//       return Promise.reject(err);
//     }
//     // Set the variable for keeping track of the retry count
//     config.__retryCount = config.__retryCount || 0;
    
//     // Check if we've maxed out the total number of retries
//     if(err.response.status === 401 || config.__retryCount >= retry) {
//         // Reject with the error
//         return Promise.reject(err);
//     }
    
//     // Increase the retry count
//     config.__retryCount += 1;
    
//     // Create new promise to handle exponential backoff
//     const backoff = new Promise((resolve) => {
//         setTimeout(() => {
//             resolve('success');
//         }, config.retryDelay || 1);
//     });
    
//     // Return the promise in which recalls axios to retry the request
//     return backoff.then(() => {
//         return axios(config);
//     });
// });


