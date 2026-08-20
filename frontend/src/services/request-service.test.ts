import { describe, expect, it, vi } from 'vitest';

import { ApiRequestError, requestJson } from './request-service';

const response = (body: unknown, ok = true, status = 200) =>
  ({
    ok,
    status,
    json: async () => body,
  }) as Response;

describe('requestJson', () => {
  it('returns data from a successful backend envelope', async () => {
    const fetcher = vi.fn().mockResolvedValue(
      response({ code: '0000', info: 'success', data: { count: 7 } })
    );

    await expect(requestJson('/stats', {}, fetcher)).resolves.toEqual({ count: 7 });
  });

  it('preserves a real empty result instead of treating it as a failure', async () => {
    const fetcher = vi.fn().mockResolvedValue(
      response({ code: '0000', info: 'success', data: [] })
    );

    await expect(requestJson('/items', {}, fetcher)).resolves.toEqual([]);
  });

  it('reports backend business failures separately', async () => {
    const fetcher = vi.fn().mockResolvedValue(
      response({ code: '1001', info: '配置不存在', data: null })
    );

    await expect(requestJson('/config', {}, fetcher)).rejects.toMatchObject({
      name: 'ApiRequestError',
      kind: 'business',
      message: '配置不存在',
      code: '1001',
    } satisfies Partial<ApiRequestError>);
  });

  it('reports HTTP failures separately', async () => {
    const fetcher = vi.fn().mockResolvedValue(response({}, false, 503));

    await expect(requestJson('/items', {}, fetcher)).rejects.toMatchObject({
      kind: 'http',
      status: 503,
    } satisfies Partial<ApiRequestError>);
  });

  it('reports network failures separately', async () => {
    const fetcher = vi.fn().mockRejectedValue(new TypeError('Failed to fetch'));

    await expect(requestJson('/items', {}, fetcher)).rejects.toMatchObject({
      kind: 'network',
      message: '网络连接失败，请稍后重试',
    } satisfies Partial<ApiRequestError>);
  });
});
