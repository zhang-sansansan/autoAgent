import { afterEach, describe, expect, it, vi } from 'vitest';
import { AiAgentDrawService } from './ai-agent-draw-service';

describe('AiAgentDrawService', () => {
  afterEach(() => vi.unstubAllGlobals());
  it('does not disguise a failed list request as empty data', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new Error('offline')));
    await expect(AiAgentDrawService.queryDrawConfigList({ pageNum: 1 })).rejects.toThrow();
  });
  it('encodes config ids in get requests', async () => {
    const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({ code: '0000', info: 'ok', data: null }), { status: 200 }));
    vi.stubGlobal('fetch', fetchMock);
    await AiAgentDrawService.getDrawConfig('a/b');
    expect(fetchMock).toHaveBeenCalledWith(expect.stringContaining('a%2Fb'), expect.anything());
  });
});
