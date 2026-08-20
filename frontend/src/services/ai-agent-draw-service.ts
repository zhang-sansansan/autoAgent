import { API_ENDPOINTS, DEFAULT_HEADERS } from '../config';
import { requestJson } from './request-service';

export interface AiAgentDrawConfigResponseDTO {
  id?: number; configId: string; configName: string; description?: string; agentId: string;
  configData?: string; version?: number; status?: number; createBy?: string; updateBy?: string;
  createTime?: string; updateTime?: string;
}
export interface AiAgentDrawConfigQueryRequestDTO { configId?: string; configName?: string; agentId?: string; status?: number; pageNum?: number; pageSize?: number; }

export class AiAgentDrawService {
  private static readonly BASE_URL = API_ENDPOINTS.AI_AGENT_DRAW.BASE;

  static queryDrawConfigList(payload: AiAgentDrawConfigQueryRequestDTO): Promise<AiAgentDrawConfigResponseDTO[]> {
    return requestJson(`${this.BASE_URL}${API_ENDPOINTS.AI_AGENT_DRAW.QUERY_LIST}`, { method: 'POST', headers: DEFAULT_HEADERS, body: JSON.stringify(payload) });
  }
  static getDrawConfig(configId: string): Promise<AiAgentDrawConfigResponseDTO | null> {
    return requestJson(`${this.BASE_URL}${API_ENDPOINTS.AI_AGENT_DRAW.GET_CONFIG}/${encodeURIComponent(configId)}`, { method: 'GET', headers: DEFAULT_HEADERS });
  }
  static deleteDrawConfig(configId: string): Promise<boolean> {
    return requestJson(`${this.BASE_URL}${API_ENDPOINTS.AI_AGENT_DRAW.DELETE_CONFIG}/${encodeURIComponent(configId)}`, { method: 'DELETE', headers: DEFAULT_HEADERS });
  }
  static saveConfig(payload: { configId?: string; configName: string; description?: string; agentId?: string; configData: string }): Promise<boolean> {
    return requestJson(`${this.BASE_URL}${API_ENDPOINTS.AI_AGENT_DRAW.SAVE_CONFIG}`, { method: 'POST', headers: DEFAULT_HEADERS, body: JSON.stringify(payload) });
  }
}
