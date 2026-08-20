import { afterEach, describe, expect, it, vi } from 'vitest';

import { AiAgentService } from './ai-agent-service';

describe('AiAgentService', () => {
  afterEach(() => vi.unstubAllGlobals());

  it('calls only the backend-supported agent armory endpoint', async () => {
    const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({ code: '0000', info: 'ok', data: true }), { status: 200 }));
    vi.stubGlobal('fetch', fetchMock);
    await expect(AiAgentService.armoryAgent('agent-1')).resolves.toBe(true);
    expect(fetchMock).toHaveBeenCalledWith(expect.stringContaining('/armory_agent'), expect.objectContaining({ body: JSON.stringify({ agentId: 'agent-1' }) }));
    expect('armoryApi' in AiAgentService).toBe(false);
  });
});
