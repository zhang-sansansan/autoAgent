import { afterEach, describe, expect, it, vi } from 'vitest';

import { DataStatisticsService } from './data-statistics-service';

describe('DataStatisticsService', () => {
  afterEach(() => vi.unstubAllGlobals());

  it('returns the real backend statistics envelope', async () => {
    const data = { activeAgentCount: 2, clientCount: 3, mcpToolCount: 4, systemPromptCount: 5, ragOrderCount: 6, advisorCount: 7, modelCount: 8, todayRequestCount: 0, successRate: 95.5, runningTaskCount: 0 };
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: true, json: async () => ({ code: '0000', info: 'success', data }) }));
    await expect(DataStatisticsService.getDataStatistics()).resolves.toEqual(data);
  });

  it('does not turn a request failure into fake zero statistics', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('Failed to fetch')));
    await expect(DataStatisticsService.getDataStatistics()).rejects.toMatchObject({ kind: 'network' });
  });
});
