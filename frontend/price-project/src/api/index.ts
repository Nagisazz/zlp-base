import http from '@/api/http';
import { Result } from '@/domain/http';
import apiConstatns from '@/api/constants';

// 刷新token信息
export function updateTokenApi(): Promise<Result> {
  return http.get(apiConstatns.URL.refreshToken);
}

// 上传文件
export function uploadFile(param: any, query: any): Promise<Result> {
  return http.postFile(apiConstatns.URL.uploadFile + '?' + query, 'post', param);
}

// 获取文件流
export function getFile(param: any): any {
  return apiConstatns.URL.getFile + '?' + param;
}

// 获取缩略图文件流   img直接访问地址获取缩略图
export function getPreviewFile(param: any): any {
  return apiConstatns.URL.getPreviewFile + '?' + param;
}

// 删除文件
export function delFile(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.delFile, param);
}

/** 获取商品收录总数 */
export function getGoodsTotal(): Promise<Result> {
  return http.get(apiConstatns.URL.goodTotal);
}

/** 获取商品查询 */
export function getGoodsSearch(param: any): Promise<Result> {
  return http.get(apiConstatns.URL.goodSearch + '?' + param);
}

