import { fireEvent, render, screen } from '@testing-library/react';
import { MemoryRouter, useLocation } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';

import { CommandCenter } from './CommandCenter';

const LocationProbe = () => <span data-testid="location">{useLocation().pathname}</span>;

describe('CommandCenter', () => {
  it('filters page destinations and navigates without executing an action', () => {
    const onClose = vi.fn();
    render(
      <MemoryRouter initialEntries={['/dashboard']}>
        <CommandCenter open onClose={onClose} />
        <LocationProbe />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText('搜索页面或功能'), {
      target: { value: '编排' },
    });
    fireEvent.click(screen.getByRole('button', { name: /Agent 编排/ }));

    expect(screen.getByTestId('location')).toHaveTextContent('/agent-config');
    expect(onClose).toHaveBeenCalledOnce();
  });

  it('shows an understandable empty state', () => {
    render(
      <MemoryRouter>
        <CommandCenter open onClose={() => undefined} />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText('搜索页面或功能'), {
      target: { value: '删除模型' },
    });

    expect(screen.getByText('没有匹配的页面')).toBeInTheDocument();
  });
});
