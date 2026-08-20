import { afterEach, describe, expect, it, vi } from 'vitest';
import { aiClientApiAdminService } from './ai-client-api-admin-service';

describe('AiClientApiAdminService', () => {
  afterEach(() => vi.unstubAllGlobals());
  it('uses the PUT method exposed by the backend update endpoints', async () => {
    const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({ code: '0000', info: 'ok', data: true }), { status: 200 }));
    vi.stubGlobal('fetch', fetchMock);
    await aiClientApiAdminService.updateAiClientApiById({ id: 1, apiId: 'openai', baseUrl: 'https://example.com', status: 1 });
    expect(fetchMock).toHaveBeenCalledWith(expect.stringContaining('/update-by-id'), expect.objectContaining({ method: 'PUT' }));
  });
});
