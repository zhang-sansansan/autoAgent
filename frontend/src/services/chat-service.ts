/**
 * Agent 对话服务（SSE 流式）
 */
import { API_ENDPOINTS, DEFAULT_HEADERS } from '../config';
import { requestJson } from './request-service';
import { AutoAgentStreamEvent, ChatStreamParser } from './chat-stream-parser';

export interface AiAgentVO {
  agentId: string;
  agentName: string;
  description?: string;
  channel?: string;
  strategy?: string;
  status?: number;
}

export interface AutoAgentRequestDTO {
  aiAgentId: string;
  message: string;
  sessionId: string;
  maxStep?: number;
}

export interface ApiResponse<T> {
  code: string;
  info: string;
  data: T;
}

export class ChatService {
  private static readonly AGENT_BASE = API_ENDPOINTS.AI_AGENT.BASE;

  /** 查询可用智能体列表 */
  static async queryAvailableAgents(): Promise<AiAgentVO[]> {
    return requestJson<AiAgentVO[]>(`${this.AGENT_BASE}${API_ENDPOINTS.AI_AGENT.QUERY_AVAILABLE_AGENTS}`, {
      method: 'GET',
      headers: DEFAULT_HEADERS,
    });
  }

  /**
   * 流式对话（SSE）
   * @param payload 对话请求
   * @param onChunk 收到内容块回调
   * @param signal 中止信号（用于停止）
   */
  static async streamAutoAgent(
    payload: AutoAgentRequestDTO,
    onChunk: (text: string) => void,
    signal?: AbortSignal
  ): Promise<void> {
    const response = await fetch(`${this.AGENT_BASE}${API_ENDPOINTS.AI_AGENT.AUTO_AGENT}`, {
      method: 'POST',
      headers: DEFAULT_HEADERS,
      body: JSON.stringify(payload),
      signal,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    if (!response.body) {
      throw new Error('当前浏览器不支持流式响应');
    }

    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    const parser = new ChatStreamParser();

    const consume = (events: AutoAgentStreamEvent[]) => {
      for (const event of events) {
        if (event.type === 'complete') continue;
        if (event.type === 'error') throw new Error(event.content || 'Agent 执行失败');
        if (event.content) onChunk(event.content);
      }
    };

    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      consume(parser.push(decoder.decode(value, { stream: true })));
    }

    consume(parser.push(decoder.decode()));
    consume(parser.finish());
  }
}
