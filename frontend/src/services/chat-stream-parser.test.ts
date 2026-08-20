import { describe, expect, it } from 'vitest';

import { ChatStreamParser } from './chat-stream-parser';

describe('ChatStreamParser', () => {
  it('parses an SSE JSON event split across network chunks', () => {
    const parser = new ChatStreamParser();

    expect(parser.push('data: {"type":"content","content":"你')).toEqual([]);
    expect(parser.push('好","completed":false}\n\n')).toEqual([
      { type: 'content', content: '你好', completed: false },
    ]);
  });

  it('parses multiple events from one network chunk', () => {
    const parser = new ChatStreamParser();

    expect(
      parser.push(
        'data: {"type":"content","content":"A","completed":false}\n\n' +
          'data: {"type":"complete","content":"执行完成","completed":true}\n\n',
      ),
    ).toEqual([
      { type: 'content', content: 'A', completed: false },
      { type: 'complete', content: '执行完成', completed: true },
    ]);
  });

  it('flushes the final event when the stream ends without a blank line', () => {
    const parser = new ChatStreamParser();

    expect(parser.push('data: {"type":"content","content":"完成"}')).toEqual([]);
    expect(parser.finish()).toEqual([{ type: 'content', content: '完成' }]);
  });

  it('rejects malformed event JSON instead of exposing it as chat content', () => {
    const parser = new ChatStreamParser();

    expect(() => parser.push('data: {bad json}\n\n')).toThrow('响应协议解析失败');
  });
});
