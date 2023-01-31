import axios from 'axios';
import { Result } from '@/domain/http';
import { updateTokenApi } from '@/api/index';
import { Action, ElMessage, ElMessageBox } from 'element-plus';
import store from '@/store/index';
import apiConstatns from './constants';

// 刷新token值
function freshToken(): any {
  // refresh接口请求头中的token需要传refreshToken
  // axios.interceptors.request.use((config: any) => {
  //   if (config.method === 'get') {
  //     config.data = true; // axios对于get请求是不给设置请求头的，所以需要传个data跳过判断
  //   }
  //   config.headers['Authorization'] = store.state.Security.authorizedUser.refreshToken;
  //   return config;
  // }, err => Promise.reject(err))
  return new Promise((resolve) => {
    updateTokenApi().then((res) => {
      if (res.status === 200) {
        store.commit("Security/REFRESHTOKEN", res.data);
        resolve('success');
      } else if (res.status === 412) {
        resolve('toLogin');
      }
    }, error => {
      resolve('error');
    })
  })
}


function toResult(url: string, type: string , httpResponseErr: any): any {
  console.log(httpResponseErr);
  let errMsg = '';
  const errInfo = type === 'success' ? httpResponseErr : httpResponseErr.response;
  let showTip = true;
  if (errInfo) {
    switch (errInfo.status) {
      case 400:
        errMsg = errInfo.message;
        break;
      case 401:
        errMsg = '登录过期，请重新登录';
        showTip = false;
        ElMessageBox.alert('您尚未登录或登录时间过长,请重新登录!', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          callback: (action: Action) => {
            if (action === 'confirm') {
              store.commit("Security/SHOWSIGN", { showSign: true, signType: 'in' });
            }
          },
        })
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
        errMsg = `连接错误`
    }
  } else {
    errMsg = '连接到服务器失败'
  }
  if (showTip) {
    ElMessage({
      message: errMsg,
      type: 'warning',
    })
  }
}

export default {

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
    console.log(url, param);
    if(param) {
        return new Promise((resolve) => {
            axios({ 
              method,
              url,
              data: param,
            }).then((res) => {
              console.log(res, 'res');
              if (res.data.status === 200) {
                resolve(res.data);
              } else if (res.data.status === 412) { // 412请求过程中发现token失效
                store.state.Security.authorizedUser.token = store.state.Security.authorizedUser.refreshToken;
                if (url.indexOf('platform/account/refresh') === -1) {
                  freshToken().then((res: any) => { // 新token获取后重新调用原接口
                    if (res === 'success') {
                      this.request(url, method, param)
                    } else if (res === 'toLogin') {
                      // 如果2个token都过期则重新登录
                      store.commit("Security/SHOWSIGN", { showSign: true, signType: 'in' });
                    }
                  })
                }
              } else {
                toResult(url, 'success', res.data);
              }
            }).catch((err) => toResult(url, 'error', err))
        })
    } else {
        return new Promise((resolve) => {
            axios({
                method,
                url,
            }).then((res) => {
              console.log(res, 'res');
              if (res.data.status === 200) {
                resolve(res.data);
              } else if (res.data.status === 412) { // 412请求过程中发现token失效
                store.state.Security.authorizedUser.token = store.state.Security.authorizedUser.refreshToken;
                if (url.indexOf('platform/account/refresh') === -1) {
                  freshToken().then((res: any) => { // 新token获取后重新调用原接口
                    if (res === 'success') {
                      this.request(url, method, param)
                    } else if (res === 'toLogin') {
                      // 如果2个token都过期则重新登录
                      store.commit("Security/SHOWSIGN", { showSign: true, signType: 'in' });
                    }
                  })
                }
              } else {
                toResult(url, 'success', res.data);
              }
            }).catch((err) => toResult(url, 'error', err))
        })
    }
  },

  // 上传文件请求
  postFile(url: string, method: string, param?: any): Promise<Result> {
    return new Promise((resolve) => {
      axios({ 
        method,
        url,
        data: param,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        },
      }).then((res) => {
        if (res.data.status === 200) {
          resolve(res.data);
        } else if (res.data.status === 412) { // 412请求过程中发现token失效
          store.state.Security.authorizedUser.token = store.state.Security.authorizedUser.refreshToken;
          freshToken().then((res: any) => { // 新token获取后重新调用原接口
            if (res === 'success') {
              this.postFile(url, method, param)
            } else if (res === 'toLogin') {
              // 如果2个token都过期则重新登录
              store.commit("Security/SHOWSIGN", { showSign: true, signType: 'in' });
            }
          })
        } else {
          toResult(url, 'success', res.data);
        }
      }).catch((err) => toResult(url, 'error', err))
    })
  }
}


// 默认30秒超时
axios.defaults.timeout = 30000
// 超时重复请求次数
const retry = 0

// 拦截request,设置全局请求为ajax请求  设置默认请求头 X-Requested-With: XMLHttpRequest是给服务器用的，用于区别 AJAX 请求(异步)
// 还是普通（同步）的请求（一般指表单提交）的。 x-requested-with 赋予 ‘XMLHttpRequest’ 
// 值表示这是一个ajax请求，而如果值为null的话 表示一个普通的请求，服务器用来检测是否为异步 如果服务器没做任何针对的反馈那么都一样。
axios.interceptors.request.use((config: any) => {
    config.headers['X-Requested-With'] = 'XMLHttpRequest';
    let token = '';
    if (store.state.Security.authorizedUser && store.state.Security.authorizedUser.token) {
      token = store.state.Security.authorizedUser.token;
    }
    if (config.url.indexOf(apiConstatns.URL.register) > -1 || config.url.indexOf(apiConstatns.URL.login) > -1) {
      config.headers['Authorization'] = '';
    } else {
      config.headers['Authorization'] = token; // 重新设置请求头生效，想到绑定的值是响应式的，所以更新值即可
    }
    
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



