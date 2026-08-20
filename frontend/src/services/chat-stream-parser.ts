export interface AutoAgentStreamEvent {
  type: 'content' | 'analysis' | 'execution' | 'supervision' | 'summary' | 'error' | 'complete';
  content?: string;
  completed?: boolean;
  sessionId?: string;
  timestamp?: number;
  step?: number;
  subType?: string;
}

export class ChatStreamParser {
  private buffer = '';

  push(text: string): AutoAgentStreamEvent[] {
    this.buffer += text.replace(/\r\n/g, '\n');
    const frames = this.buffer.split('\n\n');
    this.buffer = frames.pop() ?? '';

    return frames.filter((frame) => frame.trim()).map((frame) => this.parseFrame(frame));
  }

  finish(): AutoAgentStreamEvent[] {
    if (!this.buffer.trim()) return [];
    const frame = this.buffer;
    this.buffer = '';
    return [this.parseFrame(frame)];
  }

  private parseFrame(frame: string): AutoAgentStreamEvent {
    const data = frame
      .split('\n')
      .filter((line) => line.startsWith('data:'))
      .map((line) => line.slice(5).trimStart())
      .join('\n');

    try {
      return JSON.parse(data) as AutoAgentStreamEvent;
    } catch {
      throw new Error('响应协议解析失败');
    }
  }
}
