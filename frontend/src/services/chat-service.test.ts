import { afterEach, describe, expect, it, vi } from 'vitest';

import { ChatService } from './chat-service';

describe('ChatService.streamAutoAgent', () => {
  afterEach(() => vi.unstubAllGlobals());

  it('emits only content deltas and ignores the complete event', async () => {
    const encoder = new TextEncoder();
    const chunks = [
      'data: {"type":"content","content":"你","completed":false}\n\n',
      'data: {"type":"content","content":"好","completed":false}\n\n' +
        'data: {"type":"complete","content":"执行完成","completed":true}\n\n',
    ];
    let index = 0;
    vi.stubGlobal(
      'fetch',
      vi.fn().mockResolvedValue({
        ok: true,
        status: 200,
        body: {
          getReader: () => ({
            read: vi.fn(async () =>
              index < chunks.length
                ? { done: false, value: encoder.encode(chunks[index++]) }
                : { done: true, value: undefined },
            ),
          }),
        },
      }),
    );
    const onChunk = vi.fn();

    await ChatService.streamAutoAgent(
      { aiAgentId: 'agent-1', message: '测试', sessionId: 'session-1' },
      onChunk,
    );

    expect(onChunk.mock.calls.map(([content]) => content)).toEqual(['你', '好']);
  });
});
