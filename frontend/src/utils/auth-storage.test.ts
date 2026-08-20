import { describe, expect, it } from 'vitest';

import {
  clearAuth,
  getCurrentUser,
  isAuthenticated,
  saveAuthenticatedUser,
} from './auth-storage';

describe('local authentication state', () => {
  it('stores the current user without creating a fake server token', () => {
    saveAuthenticatedUser({ username: 'admin' });

    expect(isAuthenticated()).toBe(true);
    expect(getCurrentUser()).toEqual({ username: 'admin' });
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('clears all current and legacy login markers on logout', () => {
    localStorage.setItem('token', 'legacy-token');
    saveAuthenticatedUser({ username: 'admin' });

    clearAuth();

    expect(isAuthenticated()).toBe(false);
    expect(getCurrentUser()).toBeNull();
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('does not authenticate malformed stored user data', () => {
    localStorage.setItem('ann-agent-studio:logged-in', 'true');
    localStorage.setItem('ann-agent-studio:user', '{broken');

    expect(isAuthenticated()).toBe(false);
    expect(getCurrentUser()).toBeNull();
  });
});
