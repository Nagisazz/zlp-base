import axios from 'axios';

import { Result } from '@/domain/http'

// 默认30秒超时
axios.defaults.timeout = 30000
// 超时重复请求次数
const retry = 0

let appStore: any =  null

function toResult(url: string,httpResponseErr: any): Result {
  let errMsg = ''
  if (httpResponseErr && httpResponseErr.response) {
    switch (httpResponseErr.response.status) {
      case 400:
        errMsg = '错误请求';
        break;
      case 401:
        errMsg = '未授权，请重新登录';
        // if(!isSecurity(url) && appStore.getters['Security/offline'] && !routerConstants.PATH.LOGIN) {
        //   ElementUI.MessageBox.alert('您尚未登录或登录时间过长,请重新登录!', '系统提示', {
        //     confirmButtonText: '确定',
        //     callback: (action) => routeToLogin()
        //   })
        // } else {
        //   routeToLogin()
        // }
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
      default:
        errMsg = `连接错误${httpResponseErr.response.status}`
    }
  } else {
    errMsg = '连接到服务器失败'
  }
  return Result.Fail(errMsg)
}

// function isSecurity(url: string) {
//   return url === apiConstatns.URL.LOGIN
//             || url === apiConstatns.URL.LOAD.USER_INFO
// }
export default {
  
  attachStore(store: any) {
    appStore = store
  },

  /** get操作 */
  get(url: string,param?: any): Promise<Result> {
    return this.request(url,'get',param)
  },
  /** post操作 */
  post(url: string,param?: any): Promise<Result> {
    return this.request(url,'post',param)
  },
  /** request  */
  request(url: string, method: any, param?: any): Promise<Result> {
        // 在离线状态下只有Login操作能够执行
        // if(appStore.getters['Security/offline'] 
        //     && !isSecurity(url)) {
        //     return Promise.resolve(Result.CANCEL)
        // }
        if(param) {
            return new Promise((resolve) => {
                axios({ method, url, data: param }).then((res) => {
                  resolve(new Result(res.data.resolve()))
                }).catch((err) => toResult(url,err))
            })
        } else {
            return new Promise((resolve) => {
                axios({
                    method,
                    url,
                }).then((res) => {
                    resolve(new Result(res.data.resolve()))
                }).catch((err) => toResult(url,err))
            })
        }
  },

  // 请求服务器上的数据
  getMock(url: string,): Promise<any> {
    return this.requestMock(url, 'get')
  },

  /** request  */
  requestMock(url: string, method: any): Promise<any> {
    return new Promise((resolve) => {
      axios({
          method,
          url,
      }).then((res) => {
        // console.log(eval("(" + res.data + ")"));
          resolve(res.data)
      }).catch((err) => toResult(url,err))
    })
  },

}

// 拦截request,设置全局请求为ajax请求  设置默认请求头 X-Requested-With: XMLHttpRequest是给服务器用的，用于区别 AJAX 请求(异步)
// 还是普通（同步）的请求（一般指表单提交）的。 x-requested-with 赋予 ‘XMLHttpRequest’ 
// 值表示这是一个ajax请求，而如果值为null的话 表示一个普通的请求，服务器用来检测是否为异步 如果服务器没做任何针对的反馈那么都一样。
axios.interceptors.request.use((config: any) => {
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    return config;
})

axios.interceptors.response.use(undefined, function axiosRetryInterceptor(err) {
    const config = err.config;
    // If config does not exist or the retry option is not set, reject

    if(!config) {
      return Promise.reject(err);
    }
    // Set the variable for keeping track of the retry count
    config.__retryCount = config.__retryCount || 0;
    
    // Check if we've maxed out the total number of retries
    if(err.response.status === 401 || config.__retryCount >= retry) {
        // Reject with the error
        return Promise.reject(err);
    }
    
    // Increase the retry count
    config.__retryCount += 1;
    
    // Create new promise to handle exponential backoff
    const backoff = new Promise((resolve) => {
        setTimeout(() => {
            resolve('success');
        }, config.retryDelay || 1);
    });
    
    // Return the promise in which recalls axios to retry the request
    return backoff.then(() => {
        return axios(config);
    });
});

