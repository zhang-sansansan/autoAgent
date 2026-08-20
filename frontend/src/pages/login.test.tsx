import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { beforeEach, describe, expect, it, vi } from 'vitest';

import { AdminUserService } from '../services/admin-user-service';
import LoginPage from './login';

vi.mock('../services/admin-user-service', async () => {
  const actual = await vi.importActual<typeof import('../services/admin-user-service')>('../services/admin-user-service');
  return { ...actual, AdminUserService: { validateAdminUserLogin: vi.fn() } };
});

const renderLogin = () => render(
  <MemoryRouter initialEntries={['/login']}>
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/dashboard" element={<div>工作台已打开</div>} />
    </Routes>
  </MemoryRouter>
);

describe('LoginPage', () => {
  beforeEach(() => vi.mocked(AdminUserService.validateAdminUserLogin).mockReset());

  it('submits the default administrator immediately without creating a fake token', async () => {
    vi.mocked(AdminUserService.validateAdminUserLogin).mockResolvedValue(true);
    renderLogin();

    fireEvent.click(screen.getByRole('button', { name: '使用管理员账号登录' }));

    await waitFor(() => expect(AdminUserService.validateAdminUserLogin).toHaveBeenCalledWith({ username: 'admin', password: '123456' }));
    expect(await screen.findByText('工作台已打开')).toBeInTheDocument();
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('keeps credential rejection distinct from a network failure', async () => {
    vi.mocked(AdminUserService.validateAdminUserLogin).mockResolvedValue(false);
    renderLogin();
    fireEvent.change(screen.getByLabelText('账号'), { target: { value: 'bad' } });
    fireEvent.change(screen.getByLabelText('密码'), { target: { value: 'bad' } });
    fireEvent.click(screen.getByRole('button', { name: '登录' }));
    expect(await screen.findByText('账号或密码错误')).toBeInTheDocument();
  });
});
