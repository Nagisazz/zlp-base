import http from '@/api/http'
import { Result } from '@/domain/http';
import apiConstatns from './constants';

// 注册
export function registerApi(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.REGISTER, param);
}

// 登录
export function loginApi(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.LOGIN, param);
}

// 获取用户信息
export function getUserInfoApi(param: any): Promise<Result> {
  return http.get(apiConstatns.URL.GETUSERINFO, param);
}

// 更新用户信息
export function updateUserInfoApi(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.UPDATEUSERINFO, param);
}

// 刷新token信息
export function updateTokenApi(): Promise<Result> {
  return http.get(apiConstatns.URL.REFRESHTOKEN);
}















