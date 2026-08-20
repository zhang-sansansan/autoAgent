const LOGIN_KEY = 'ann-agent-studio:logged-in';
const USER_KEY = 'ann-agent-studio:user';

export interface AuthenticatedUser {
  username: string;
  userId?: string;
}

export const saveAuthenticatedUser = (user: AuthenticatedUser): void => {
  localStorage.setItem(LOGIN_KEY, 'true');
  localStorage.setItem(USER_KEY, JSON.stringify(user));

  localStorage.setItem('isLoggedIn', 'true');
  localStorage.setItem('userInfo', JSON.stringify(user));
  localStorage.removeItem('token');
};

export const getCurrentUser = (): AuthenticatedUser | null => {
  const rawUser = localStorage.getItem(USER_KEY) || localStorage.getItem('userInfo');
  if (!rawUser) return null;

  try {
    const user = JSON.parse(rawUser) as Partial<AuthenticatedUser>;
    return typeof user.username === 'string' && user.username.trim()
      ? { username: user.username, ...(user.userId ? { userId: user.userId } : {}) }
      : null;
  } catch {
    return null;
  }
};

export const isAuthenticated = (): boolean => {
  const loggedIn =
    localStorage.getItem(LOGIN_KEY) === 'true' || localStorage.getItem('isLoggedIn') === 'true';
  return loggedIn && getCurrentUser() !== null;
};

export const clearAuth = (): void => {
  [LOGIN_KEY, USER_KEY, 'isLoggedIn', 'userInfo', 'token'].forEach((key) =>
    localStorage.removeItem(key)
  );
};
