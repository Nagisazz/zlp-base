import http from '@/api/http'
import { Result } from '@/domain/http';
import apiConstatns from './constants';

// 注册
export function registerApi(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.register, param);
}

// 登录
export function loginApi(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.login, param);
}

// 获取用户信息
export function getUserInfoApi(param: any): Promise<Result> {
  return http.get(apiConstatns.URL.getUserInfo, param);
}

// 更新用户信息
export function updateUserInfoApi(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.updateInfo, param);
}

// 更新用户密码
export function updateUserPassword(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.updatePassword, param);
}

// 刷新token信息
export function updateTokenApi(): Promise<Result> {
  return http.get(apiConstatns.URL.refreshToken);
}

// 上传文件
export function uploadFile(param: any, query: any): Promise<Result> {
  return http.postFile(apiConstatns.URL.uploadFile + '?' + query, 'post', param);
}















