import { afterEach, describe, expect, it, vi } from 'vitest';

import { AdminUserService } from './admin-user-service';

describe('AdminUserService login', () => {
  afterEach(() => vi.unstubAllGlobals());

  it('returns false only for rejected credentials', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: true, json: async () => ({ code: '0000', info: 'success', data: false }) }));
    await expect(AdminUserService.validateAdminUserLogin({ username: 'bad', password: 'bad' })).resolves.toBe(false);
  });

  it('does not disguise a network failure as rejected credentials', async () => {
    vi.stubGlobal('fetch', vi.fn().mockRejectedValue(new TypeError('Failed to fetch')));
    await expect(AdminUserService.validateAdminUserLogin({ username: 'admin', password: '123456' })).rejects.toMatchObject({ kind: 'network' });
  });
});
