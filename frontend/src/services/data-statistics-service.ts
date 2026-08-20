/**
 * 数据统计API服务
 */

import { API_CONFIG, DEFAULT_HEADERS } from '../config/api';
import { requestJson } from './request-service';

// 定义数据统计响应数据类型
export interface DataStatisticsResponseDTO {
  activeAgentCount: number;
  clientCount: number;
  mcpToolCount: number;
  systemPromptCount: number;
  ragOrderCount: number;
  advisorCount: number;
  modelCount: number;
  todayRequestCount: number;
  successRate: number;
  runningTaskCount: number;
}

// 定义API响应格式
export interface ApiResponse<T> {
  code: string;
  info: string;
  data: T;
}

/**
 * 数据统计API服务类
 */
export class DataStatisticsService {
  private static readonly BASE_URL = `${API_CONFIG.BASE_DOMAIN}/api/v1/admin/data/statistics`;

  /**
   * 获取系统数据统计
   */
  static async getDataStatistics(): Promise<DataStatisticsResponseDTO> {
    return requestJson<DataStatisticsResponseDTO>(`${this.BASE_URL}/get-data-statistics`, {
      method: 'GET',
      headers: DEFAULT_HEADERS,
    });
  }
}
