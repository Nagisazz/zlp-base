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

/** 创建文件 */
export function createFile(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.createFile, param);
}

/** 创建纯文本文件 */
export function createTxt(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.createTxt, param);
}

/** 更新纯文本文件 */
export function updateFile(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.updateFile, param);
}

/** 删除文件 */
export function deleteFile(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.deleteFile, param);
}

/** 创建文件夹 */
export function createFolder(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.createFolder, param);
}

/** 更新文件夹 */
export function updateFolder(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.updateFolder, param);
}

/** 删除文件夹 */
export function deleteFolder(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.deleteFolder, param);
}

/** 根据父id查找目录列表 */
export function getFileList(param: any): Promise<Result> {
  return http.get(apiConstatns.URL.findList + '?' + param);
}

/** 生成单个文件分享码 */
export function singleShareCode(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.singleShareCode, param);
}

/** 生成多个文件分享码 */
export function multiShareCode(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.multiShareCode, param);
}

/** 预览分享目录 */
export function previewShare(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.previewShare, param);
}

/** 保存分享的文件 */
export function saveShareFile(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.saveShareFile, param);
}
