import { API_ENDPOINTS, DEFAULT_HEADERS } from '../config';
import { requestJson } from './request-service';

export class AiAgentService {
  static armoryAgent(agentId: string): Promise<boolean> {
    return requestJson<boolean>(`${API_ENDPOINTS.AI_AGENT.BASE}${API_ENDPOINTS.AI_AGENT.ARMORY_AGENT}`, {
      method: 'POST',
      headers: DEFAULT_HEADERS,
      body: JSON.stringify({ agentId }),
    });
  }
}
