import http from '@/api/http'
import { Result } from '@/domain/http';
import apiConstatns from './constants';

/** 
 * 获取平平的文章列表
 */
export function getKanBan(param: any): Promise<Result> {
  return http.post(apiConstatns.URL.FAMILY_KANBAN, param);
}

// 获取home数据
export function getHomeData(): Promise<any> {
  return http.getMock('http://1.15.87.105:11000/love/zp/home/zp-data.json?t=' + new Date().getTime());
}















