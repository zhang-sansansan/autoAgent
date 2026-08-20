/**
 * 管理员用户API服务
 */

import { API_ENDPOINTS, DEFAULT_HEADERS } from '../config';
import { requestJson } from './request-service';

// 定义登录请求数据类型
export interface AdminUserLoginRequestDTO {
  username: string;
  password: string;
}

// 用户新增/编辑请求
export interface AdminUserRequestDTO {
  id?: number;
  userId?: string;
  username?: string;
  password?: string;
  status?: number;
}

// 用户查询请求
export interface AdminUserQueryRequestDTO {
  username?: string;
  status?: number;
  pageNum?: number;
  pageSize?: number;
}

// 用户响应
export interface AdminUserResponseDTO {
  id: number;
  userId: string;
  username: string;
  status: number;
  createTime?: string;
  updateTime?: string;
}

// 定义API响应格式
export interface ApiResponse<T> {
  code: string;
  info: string;
  data: T;
}

/**
 * 管理员用户API服务类
 */
export class AdminUserService {
  private static readonly BASE_URL = API_ENDPOINTS.ADMIN_USER.BASE;

  /**
   * 验证管理员用户登录
   * @param loginData 登录数据
   * @returns Promise<boolean> 登录是否成功
   */
  static async validateAdminUserLogin(loginData: AdminUserLoginRequestDTO): Promise<boolean> {
    return requestJson<boolean>(
      `${this.BASE_URL}${API_ENDPOINTS.ADMIN_USER.VALIDATE_LOGIN}`,
      { method: 'POST', headers: DEFAULT_HEADERS, body: JSON.stringify(loginData) }
    );
  }

  /** 查询用户列表 */
  static async queryUserList(payload: AdminUserQueryRequestDTO): Promise<AdminUserResponseDTO[]> {
    try {
      const response = await fetch(`${this.BASE_URL}${API_ENDPOINTS.ADMIN_USER.QUERY_LIST}`, {
        method: 'POST',
        headers: DEFAULT_HEADERS,
        body: JSON.stringify(payload),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result: ApiResponse<AdminUserResponseDTO[]> = await response.json();
      if (result.code === '0000') {
        return result.data || [];
      }
      throw new Error(result.info || '查询失败');
    } catch (error) {
      console.error('查询用户列表失败:', error);
      throw error;
    }
  }

  /** 新增用户 */
  static async createUser(payload: AdminUserRequestDTO): Promise<boolean> {
    try {
      const response = await fetch(`${this.BASE_URL}${API_ENDPOINTS.ADMIN_USER.CREATE}`, {
        method: 'POST',
        headers: DEFAULT_HEADERS,
        body: JSON.stringify(payload),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result: ApiResponse<boolean> = await response.json();
      if (result.code === '0000') {
        return result.data || false;
      }
      throw new Error(result.info || '新增失败');
    } catch (error) {
      console.error('新增用户失败:', error);
      throw error;
    }
  }

  /** 更新用户 */
  static async updateUser(payload: AdminUserRequestDTO): Promise<boolean> {
    try {
      const response = await fetch(`${this.BASE_URL}${API_ENDPOINTS.ADMIN_USER.UPDATE_BY_ID}`, {
        method: 'PUT',
        headers: DEFAULT_HEADERS,
        body: JSON.stringify(payload),
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result: ApiResponse<boolean> = await response.json();
      if (result.code === '0000') {
        return result.data || false;
      }
      throw new Error(result.info || '更新失败');
    } catch (error) {
      console.error('更新用户失败:', error);
      throw error;
    }
  }

  /** 删除用户 */
  static async deleteUserById(id: number): Promise<boolean> {
    try {
      const response = await fetch(`${this.BASE_URL}${API_ENDPOINTS.ADMIN_USER.DELETE_BY_ID}/${id}`, {
        method: 'DELETE',
        headers: DEFAULT_HEADERS,
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const result: ApiResponse<boolean> = await response.json();
      if (result.code === '0000') {
        return result.data || false;
      }
      throw new Error(result.info || '删除失败');
    } catch (error) {
      console.error('删除用户失败:', error);
      throw error;
    }
  }
}
